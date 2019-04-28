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

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaFish;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBucketLavaFish extends ItemBucket {

    private final EntityType<?> fishType;

    public ItemBucketLavaFish(EntityType<?> fishTypeIn, Fluid fluid) {
        super(fluid, new Item.Properties().maxStackSize(1).group(ItemGroup.MISC));
        this.setRegistryName(fishTypeIn.getRegistryName() + "_bucket");
        this.fishType = fishTypeIn;
    }

    @Override
    public void onLiquidPlaced(World worldIn, ItemStack p_203792_2_, BlockPos pos) {
        if (!worldIn.isRemote) {
            this.placeFish(worldIn, p_203792_2_, pos);
        }
    }

    @Override
    protected void playEmptySound(@Nullable EntityPlayer player, IWorld worldIn, @Nonnull BlockPos pos) {
        worldIn.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY_FISH, SoundCategory.NEUTRAL, 1.0F, 0.5F);
    }

    private void placeFish(World worldIn, ItemStack p_205357_2_, BlockPos pos) {
        Entity entity = this.fishType.spawn(worldIn, p_205357_2_, null, pos, true, false);
        if (entity != null) {
            ((AbstractLavaFish)entity).setFromBucket(true);
        }
    }
}
