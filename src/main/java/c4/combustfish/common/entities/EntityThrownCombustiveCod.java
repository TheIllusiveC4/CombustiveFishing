/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.entities;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityThrownCombustiveCod extends EntitySnowball {

    public EntityThrownCombustiveCod(World worldIn) { super(worldIn); }

    public EntityThrownCombustiveCod(World worldIn, EntityLivingBase throwIn) {
        super(worldIn, throwIn);
    }

    public EntityThrownCombustiveCod(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 3)
        {
            for (int i = 0; i < 8; ++i)
            {
                this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onImpact(RayTraceResult result)
    {
        if (result.entityHit != null)
        {
            result.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setFireDamage(), 1.0F);
        }

        if (!this.world.isRemote)
        {
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, 1, true, true);
            this.world.setEntityState(this, (byte)3);
            this.setDead();
        }
    }
}
