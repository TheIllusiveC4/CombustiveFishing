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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import top.theillusivec4.combustivefishing.client.renderer.CombustiveCodRenderer;
import top.theillusivec4.combustivefishing.client.renderer.SwordfishRenderer;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingItems;

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

    ItemModelsProperties
        .registerProperty(CombustiveFishingItems.BLAZING_FISHING_ROD, new ResourceLocation("cast"),
            (p_239422_0_, p_239422_1_, p_239422_2_) -> {
              if (p_239422_2_ == null) {
                return 0.0F;
              } else {
                boolean flag = p_239422_2_.getHeldItemMainhand() == p_239422_0_;
                boolean flag1 = p_239422_2_.getHeldItemOffhand() == p_239422_0_;
                if (p_239422_2_.getHeldItemMainhand().getItem() instanceof FishingRodItem) {
                  flag1 = false;
                }

                return (flag || flag1) && p_239422_2_ instanceof PlayerEntity
                    && ((PlayerEntity) p_239422_2_).fishingBobber != null ? 1.0F : 0.0F;
              }
            });
  }
}
