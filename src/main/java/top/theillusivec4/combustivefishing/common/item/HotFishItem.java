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

package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CauldronBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public abstract class HotFishItem extends Item {

  public HotFishItem(Item.Properties properties) {
    super(properties);
  }

  @Override
  public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
    World world = entity.world;
    double posX = entity.getPosX();
    double posY = entity.getPosY();
    double posZ = entity.getPosZ();
    BlockPos blockpos = entity.getPosition();
    BlockState state = world.getBlockState(blockpos);

    if (entity.isInWater()) {

      if (!world.isRemote) {

        world.addEntity(new ItemEntity(world, posX, posY, posZ,
            new ItemStack(this.getCooledItem(), entity.getItem().getCount())));
        entity.remove();
      }
      world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH,
          SoundCategory.NEUTRAL, 0.5F,
          2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
      return true;
    } else if (state.getBlock() == Blocks.CAULDRON) {
      int level = state.get(CauldronBlock.LEVEL);

      if (level > 0) {
        world.addEntity(new ItemEntity(world, posX, posY, posZ,
            new ItemStack(this.getCooledItem(), entity.getItem().getCount())));
        entity.remove();
      }
      return true;
    }
    return false;
  }

  protected abstract Item getCooledItem();
}
