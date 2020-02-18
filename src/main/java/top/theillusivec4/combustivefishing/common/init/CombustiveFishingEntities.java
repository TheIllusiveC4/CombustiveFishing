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
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.client.renderer.CombustiveCodRender;
import top.theillusivec4.combustivefishing.client.renderer.SwordfishRender;
import top.theillusivec4.combustivefishing.common.entity.BlazingFishingBobberEntity;
import top.theillusivec4.combustivefishing.common.entity.CombustiveCodEntity;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;
import top.theillusivec4.combustivefishing.common.entity.ThrownCombustiveCodEntity;
import top.theillusivec4.combustivefishing.common.registry.RegistryReference;

public class CombustiveFishingEntities {

  public static final EntityType<CombustiveCodEntity> COMBUSTIVE_COD;
  public static final EntityType<ThrownCombustiveCodEntity> THROWN_COMBUSTIVE_COD;
  public static final EntityType<BlazingFishingBobberEntity> BLAZING_BOBBER;
  public static final EntityType<SearingSwordfishEntity> SEARING_SWORDFISH;

  static {
    COMBUSTIVE_COD = EntityType.Builder
        .<CombustiveCodEntity>create((entityType, world) -> new CombustiveCodEntity(world),
            EntityClassification.WATER_CREATURE).size(0.5F, 0.3F).immuneToFire()
        .setTrackingRange(80).setUpdateInterval(3).setShouldReceiveVelocityUpdates(true)
        .build(RegistryReference.COMBUSTIVE_COD);
    COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, RegistryReference.COMBUSTIVE_COD);

    SEARING_SWORDFISH = EntityType.Builder
        .<SearingSwordfishEntity>create((entityType, world) -> new SearingSwordfishEntity(world),
            EntityClassification.WATER_CREATURE).setTrackingRange(80).setUpdateInterval(3)
        .setShouldReceiveVelocityUpdates(true).build(RegistryReference.SEARING_SWORDFISH);
    SEARING_SWORDFISH.setRegistryName(CombustiveFishing.MODID, RegistryReference.SEARING_SWORDFISH);

    THROWN_COMBUSTIVE_COD = EntityType.Builder
        .<ThrownCombustiveCodEntity>create((entityType, world) -> new ThrownCombustiveCodEntity(world),
            EntityClassification.MISC).setTrackingRange(64).setUpdateInterval(10)
        .setShouldReceiveVelocityUpdates(true).build(RegistryReference.THROWN_COD);
    THROWN_COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, RegistryReference.THROWN_COD);

    BLAZING_BOBBER = EntityType.Builder
        .<BlazingFishingBobberEntity>create((entityType, world) -> new BlazingFishingBobberEntity(world),
            EntityClassification.MISC).disableSerialization().disableSummoning()
        .setTrackingRange(64).setUpdateInterval(5).setShouldReceiveVelocityUpdates(true)
        .build(RegistryReference.BLAZING_BOBBER);
    BLAZING_BOBBER.setRegistryName(CombustiveFishing.MODID, RegistryReference.BLAZING_BOBBER);
  }

  public static void registerEntityRenders(Minecraft mc) {
    RenderingRegistry.registerEntityRenderingHandler(ThrownCombustiveCodEntity.class,
        (rendererManager) -> new SpriteRenderer<>(rendererManager, mc.getItemRenderer()));
    RenderingRegistry
        .registerEntityRenderingHandler(BlazingFishingBobberEntity.class, FishRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(CombustiveCodEntity.class, CombustiveCodRender::new);
    RenderingRegistry
        .registerEntityRenderingHandler(SearingSwordfishEntity.class, SwordfishRender::new);
  }
}
