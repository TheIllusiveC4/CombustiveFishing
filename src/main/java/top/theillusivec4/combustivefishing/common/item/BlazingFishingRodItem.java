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
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
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
import top.theillusivec4.combustivefishing.common.entity.BlazingFishingBobberEntity;

public class BlazingFishingRodItem extends FishingRodItem {

  public BlazingFishingRodItem() {
    super(new Item.Properties().defaultMaxDamage(128).group(ItemGroup.TOOLS));
    this.setRegistryName(CombustiveFishing.MODID, "blazing_fishing_rod");
  }

  @Nonnull
  @Override
  public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
      @Nonnull Hand handIn) {
    ItemStack itemstack = playerIn.getHeldItem(handIn);

    if (playerIn.fishingBobber != null) {
      if (!worldIn.isRemote) {
        int i = playerIn.fishingBobber.handleHookRetraction(itemstack);
        itemstack.damageItem(i, playerIn, (damager) -> damager.sendBreakAnimation(handIn));
      }
      playerIn.swingArm(handIn);
      worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
          SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F,
          0.4F / (random.nextFloat() * 0.4F + 0.8F));
    } else {
      worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(),
          SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F,
          0.4F / (random.nextFloat() * 0.4F + 0.8F));

      if (!worldIn.isRemote) {
        int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
        int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
        worldIn.addEntity(new BlazingFishingBobberEntity(playerIn, worldIn, j, k));
      }
      playerIn.swingArm(handIn);
      playerIn.addStat(Stats.ITEM_USED.get(this));
    }
    return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
  }

  @Override
  public int getItemEnchantability() {
    return 22;
  }
}
