/*
 * Copyright (C) 2017-2019  C4
 *
 * This file is part of Combustive Fishing, a mod made for Minecraft.
 *
 * Combustive Fishing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Combustive Fishing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Combustive Fishing.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Particles;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;

import javax.annotation.Nonnull;

public class ThrownCombustiveCodEntity extends EntityThrowable {

    public ThrownCombustiveCodEntity(World worldIn) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, worldIn);
    }

    public ThrownCombustiveCodEntity(World worldIn, EntityLivingBase throwerIn) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, throwerIn, worldIn);
    }

    public ThrownCombustiveCodEntity(World worldIn, double x, double y, double z) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, x, y, z, worldIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {

        if (id == 3) {
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(Particles.FLAME, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {

        if (result.entity != null) {
            result.entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setFireDamage(), 1.0F);
        }

        if (!this.world.isRemote) {
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, 1, true, true);
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }
}
