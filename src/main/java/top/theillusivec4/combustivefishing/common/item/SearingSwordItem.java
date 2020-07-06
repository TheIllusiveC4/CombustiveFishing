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
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.crafting.Ingredient;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingItems;
import top.theillusivec4.combustivefishing.common.registry.RegistryReference;

public class SearingSwordItem extends SwordItem {

  public SearingSwordItem() {
    super(new IItemTier() {
      @Override
      public int getMaxUses() {
        return 131;
      }

      @Override
      public float getEfficiency() {
        return 1.0F;
      }

      @Override
      public float getAttackDamage() {
        return 8.0F;
      }

      @Override
      public int getHarvestLevel() {
        return 1;
      }

      @Override
      public int getEnchantability() {
        return 0;
      }

      @Nonnull
      @Override
      public Ingredient getRepairMaterial() {
        return Ingredient.fromItems(CombustiveFishingItems.SWORDFISH_BILL);
      }
    }, 0, -1.4F, new Item.Properties().group(ItemGroup.COMBAT));
    this.setRegistryName(CombustiveFishing.MODID, RegistryReference.SEARING_SWORD);
  }

  @Override
  public boolean hitEntity(ItemStack stack, LivingEntity target, @Nonnull LivingEntity attacker) {

    if (!target.getType().isImmuneToFire()) {
      target.setFire(10);
    }
    return super.hitEntity(stack, target, attacker);
  }
}
