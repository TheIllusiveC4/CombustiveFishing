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

package top.theillusivec4.combustivefishing.common.registry;

import net.minecraft.item.Item;
import net.minecraftforge.registries.ObjectHolder;
import top.theillusivec4.combustivefishing.CombustiveFishing;

@ObjectHolder(value = CombustiveFishing.MODID)
public class CombustiveFishingItems {

  @ObjectHolder(RegistryReference.COMBUSTIVE_COD_BUCKET)
  public static final Item COMBUSTIVE_COD_BUCKET;

  @ObjectHolder(RegistryReference.COMBUSTIVE_COD)
  public static final Item COMBUSTIVE_COD;

  @ObjectHolder(RegistryReference.COOLED_COD)
  public static final Item COOLED_COD;

  @ObjectHolder(RegistryReference.BONE_FISH)
  public static final Item BONE_FISH;

  @ObjectHolder(RegistryReference.SWORDFISH_BILL)
  public static final Item SWORDFISH_BILL;

  @ObjectHolder(RegistryReference.COOLED_BILL)
  public static final Item COOLED_BILL;

  @ObjectHolder(RegistryReference.BLAZING_FISHING_ROD)
  public static final Item BLAZING_FISHING_ROD;

  static {
    COMBUSTIVE_COD_BUCKET = null;
    COMBUSTIVE_COD = null;
    COOLED_COD = null;
    BONE_FISH = null;
    SWORDFISH_BILL = null;
    COOLED_BILL = null;
    BLAZING_FISHING_ROD = null;
  }
}
