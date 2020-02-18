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

package top.theillusivec4.combustivefishing.common.init;

import net.minecraft.util.ResourceLocation;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class CombustiveFishingLoot {

    public static final ResourceLocation NETHER_FISHING = create("gameplay/nether_fishing");
    public static final ResourceLocation LAVA_FISHING = create("gameplay/lava_fishing");
    public static final ResourceLocation COMBUSTIVE_COD = create("entity/combustive_cod");
    public static final ResourceLocation SEARING_SWORDFISH = create("entity/searing_swordfish");

    private static ResourceLocation create(String path) {
        return new ResourceLocation(CombustiveFishing.MODID, path);
    }
}
