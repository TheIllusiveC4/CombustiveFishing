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

import javax.annotation.Nonnull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;

public class CombustiveCodEntity extends AbstractLavaGroupFishEntity {

  public CombustiveCodEntity(World world) {
    super(CombustiveFishingEntities.COMBUSTIVE_COD, world);
  }

  @Override
  protected ItemStack getFishBucket() {
    return new ItemStack(CombustiveFishingItems.COMBUSTIVE_COD_BUCKET);
  }

  @Nonnull
  @Override
  protected ResourceLocation getLootTable() {
    return CombustiveFishingLoot.COMBUSTIVE_COD;
  }

  @Override
  protected SoundEvent getAmbientSound() {
    return SoundEvents.ENTITY_COD_AMBIENT;
  }

  @Override
  protected SoundEvent getDeathSound() {
    return SoundEvents.ENTITY_COD_DEATH;
  }

  @Override
  protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
    return SoundEvents.ENTITY_COD_HURT;
  }

  @Override
  protected SoundEvent getFlopSound() {
    return SoundEvents.ENTITY_COD_FLOP;
  }
}
