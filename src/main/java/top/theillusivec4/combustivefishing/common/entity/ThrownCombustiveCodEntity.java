/*
 * Copyright (c) 2017-2020 C4
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

import javax.annotation.Nonnull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingItems;

public class ThrownCombustiveCodEntity extends ProjectileItemEntity {

  public ThrownCombustiveCodEntity(World worldIn) {
    super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, worldIn);
  }

  public ThrownCombustiveCodEntity(World worldIn, LivingEntity throwerIn) {
    super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, throwerIn, worldIn);
  }

  public ThrownCombustiveCodEntity(World worldIn, double x, double y, double z) {
    super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, x, y, z, worldIn);
  }

  @Nonnull
  @Override
  protected Item getDefaultItem() {
    return CombustiveFishingItems.COMBUSTIVE_COD;
  }

  private IParticleData makeParticle() {
    ItemStack itemstack = this.func_213882_k();
    return itemstack.isEmpty() ? ParticleTypes.FLAME
        : new ItemParticleData(ParticleTypes.ITEM, itemstack);
  }

  @OnlyIn(Dist.CLIENT)
  @Override
  public void handleStatusUpdate(byte id) {
    if (id == 3) {
      IParticleData iparticledata = this.makeParticle();

      for (int i = 0; i < 8; ++i) {
        this.world
            .addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D,
                0.0D);
      }
    }

  }

  @Override
  protected void onImpact(@Nonnull RayTraceResult result) {

    if (result.getType() == RayTraceResult.Type.ENTITY) {
      Entity entity = ((EntityRayTraceResult) result).getEntity();
      entity.attackEntityFrom(
          DamageSource.causeThrownDamage(this, this.func_234616_v_()).setFireDamage(), 1.0F);
    }

    if (!this.world.isRemote) {
      this.world.createExplosion(null, this.getPosX(), this.getPosY(), this.getPosZ(), 1, true,
          Mode.BREAK);
      this.world.setEntityState(this, (byte) 3);
      this.remove();
    }
  }

  @Nonnull
  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }
}
