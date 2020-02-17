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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.client.renderer.RenderBlazingHook;
import top.theillusivec4.combustivefishing.client.renderer.RenderCombustiveCod;
import top.theillusivec4.combustivefishing.client.renderer.RenderSwordfish;
import top.theillusivec4.combustivefishing.common.entity.BlazingFishingBobberEntity;
import top.theillusivec4.combustivefishing.common.entity.CombustiveCodEntity;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;
import top.theillusivec4.combustivefishing.common.entity.ThrownCombustiveCodEntity;

public class CombustiveFishingEntities {

  public static final EntityType<CombustiveCodEntity> COMBUSTIVE_COD;
  public static final EntityType<ThrownCombustiveCodEntity> THROWN_COMBUSTIVE_COD;
  public static final EntityType<BlazingFishingBobberEntity> BLAZING_BOBBER;
  public static final EntityType<SearingSwordfishEntity> SEARING_SWORDFISH;

  static {
    COMBUSTIVE_COD = EntityType.Builder.create(CombustiveCodEntity.class, CombustiveCodEntity::new)
        .tracker(80, 3, true).build("combustive_cod");
    COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, "combustive_cod");

    SEARING_SWORDFISH = EntityType.Builder
        .create(SearingSwordfishEntity.class, SearingSwordfishEntity::new).tracker(80, 3, true)
        .build("combustive_cod");
    SEARING_SWORDFISH.setRegistryName(CombustiveFishing.MODID, "searing_swordfish");

    THROWN_COMBUSTIVE_COD = EntityType.Builder
        .create(ThrownCombustiveCodEntity.class, ThrownCombustiveCodEntity::new)
        .tracker(64, 10, true).build("thrown_combustive_cod");
    THROWN_COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, "thrown_combustive_cod");

    BLAZING_BOBBER = EntityType.Builder
        .create(BlazingFishingBobberEntity.class, BlazingFishingBobberEntity::new)
        .disableSerialization().disableSummoning().tracker(64, 5, true).build("blazing_bobber");
    BLAZING_BOBBER.setRegistryName(CombustiveFishing.MODID, "blazing_bobber");
  }

  public static void registerEntityRenders(Minecraft mc) {
    RenderingRegistry.registerEntityRenderingHandler(ThrownCombustiveCodEntity.class,
        (rendererManager) -> new SpriteRenderer<>(rendererManager, mc.getItemRenderer()));
    RenderingRegistry
        .registerEntityRenderingHandler(BlazingFishingBobberEntity.class, RenderBlazingHook::new);
    RenderingRegistry
        .registerEntityRenderingHandler(CombustiveCodEntity.class, RenderCombustiveCod::new);
    RenderingRegistry
        .registerEntityRenderingHandler(SearingSwordfishEntity.class, RenderSwordfish::new);
  }
}
