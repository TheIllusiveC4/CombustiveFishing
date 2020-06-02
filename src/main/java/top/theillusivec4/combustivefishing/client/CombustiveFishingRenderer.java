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

package top.theillusivec4.combustivefishing.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import top.theillusivec4.combustivefishing.client.renderer.CombustiveCodRenderer;
import top.theillusivec4.combustivefishing.client.renderer.SwordfishRenderer;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingEntities;

public class CombustiveFishingRenderer {

  public static void register() {
    RenderingRegistry
        .registerEntityRenderingHandler(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD,
            (renderManager) -> new SpriteRenderer<>(renderManager,
                Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry.registerEntityRenderingHandler(CombustiveFishingEntities.BLAZING_BOBBER,
        FishRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(CombustiveFishingEntities.COMBUSTIVE_COD,
        CombustiveCodRenderer::new);
    RenderingRegistry.registerEntityRenderingHandler(CombustiveFishingEntities.SEARING_SWORDFISH,
        SwordfishRenderer::new);
  }
}
