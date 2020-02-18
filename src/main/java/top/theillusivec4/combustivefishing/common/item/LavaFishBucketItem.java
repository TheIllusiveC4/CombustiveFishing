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

package top.theillusivec4.combustivefishing.common.item;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaFishEntity;

public class LavaFishBucketItem extends BucketItem {

  private final Supplier<? extends EntityType<?>> fishTypeSupplier;

  public LavaFishBucketItem(Supplier<? extends EntityType<?>> fishTypeIn,
      Supplier<? extends Fluid> fluidIn) {
    super(fluidIn, new Item.Properties().maxStackSize(1).group(ItemGroup.MISC));
    this.fishTypeSupplier = fishTypeIn;
    this.setRegistryName(fishTypeIn.get().getRegistryName() + "_bucket");
  }

  @Override
  public void onLiquidPlaced(World worldIn, ItemStack p_203792_2_, BlockPos pos) {
    if (!worldIn.isRemote) {
      this.placeFish(worldIn, p_203792_2_, pos);
    }
  }

  @Override
  protected void playEmptySound(@Nullable PlayerEntity player, IWorld worldIn,
      @Nonnull BlockPos pos) {
    worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F,
        0.5F);
  }

  private void placeFish(World worldIn, ItemStack stack, BlockPos pos) {
    Entity entity = this.getFishType()
        .spawn(worldIn, stack, null, pos, SpawnReason.BUCKET, true, false);

    if (entity != null) {
      ((AbstractLavaFishEntity) entity).setFromBucket(true);
    }
  }

  protected EntityType<?> getFishType() {
    return fishTypeSupplier.get();
  }
}
