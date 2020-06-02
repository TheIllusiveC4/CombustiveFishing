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

import javax.annotation.Nonnull;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.ThrownCombustiveCodEntity;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingItems;
import top.theillusivec4.combustivefishing.common.registry.RegistryReference;

public class CombustiveCodItem extends HotFishItem {

  public CombustiveCodItem() {
    super(new Item.Properties().maxStackSize(16).group(ItemGroup.MISC));
    this.setRegistryName(CombustiveFishing.MODID, RegistryReference.COMBUSTIVE_COD);
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      @Nonnull Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    if (!playerIn.abilities.isCreativeMode) {
      itemstack.shrink(1);
    }
    worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
        SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F,
        0.4F / (random.nextFloat() * 0.4F + 0.8F));

    if (!worldIn.isRemote) {
      ThrownCombustiveCodEntity throwncod = new ThrownCombustiveCodEntity(worldIn, playerIn);
      throwncod.setItem(itemstack);
      throwncod.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
      worldIn.addEntity(throwncod);
    }
    playerIn.addStat(Stats.ITEM_USED.get(this));
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  protected Item getCooledItem() {
    return CombustiveFishingItems.COOLED_COD;
  }
}
