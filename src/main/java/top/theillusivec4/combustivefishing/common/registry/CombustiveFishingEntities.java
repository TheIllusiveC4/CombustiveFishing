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

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaFishEntity;
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
    COMBUSTIVE_COD = EntityType.Builder.<CombustiveCodEntity>create(
        (entityType, world) -> new CombustiveCodEntity(world), EntityClassification.WATER_CREATURE)
        .size(0.5F, 0.3F).immuneToFire().setTrackingRange(80).setUpdateInterval(3).immuneToFire()
        .setShouldReceiveVelocityUpdates(true).build(RegistryReference.COMBUSTIVE_COD);
    COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, RegistryReference.COMBUSTIVE_COD);
    GlobalEntityTypeAttributes
        .put(COMBUSTIVE_COD, AbstractLavaFishEntity.registerAttributes().func_233813_a_());

    SEARING_SWORDFISH = EntityType.Builder.<SearingSwordfishEntity>create(
        (entityType, world) -> new SearingSwordfishEntity(world),
        EntityClassification.WATER_CREATURE).size(0.9F, 0.6F).setTrackingRange(80)
        .setUpdateInterval(3).setShouldReceiveVelocityUpdates(true).immuneToFire()
        .build(RegistryReference.SEARING_SWORDFISH);
    SEARING_SWORDFISH.setRegistryName(CombustiveFishing.MODID, RegistryReference.SEARING_SWORDFISH);
    GlobalEntityTypeAttributes
        .put(SEARING_SWORDFISH, SearingSwordfishEntity.registerAttribute().func_233813_a_());

    THROWN_COMBUSTIVE_COD = EntityType.Builder.<ThrownCombustiveCodEntity>create(
        (entityType, world) -> new ThrownCombustiveCodEntity(world), EntityClassification.MISC)
        .size(0.25F, 0.25F).setTrackingRange(64).setUpdateInterval(10).immuneToFire()
        .setShouldReceiveVelocityUpdates(true).setCustomClientFactory(
            ((spawnEntity, world) -> new ThrownCombustiveCodEntity(world, spawnEntity.getPosX(),
                spawnEntity.getPosY(), spawnEntity.getPosZ()))).build(RegistryReference.THROWN_COD);
    THROWN_COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, RegistryReference.THROWN_COD);

    BLAZING_BOBBER = EntityType.Builder.<BlazingFishingBobberEntity>create((entityType, world) -> {
      if (world.isRemote()) {
        return new BlazingFishingBobberEntity(world);
      } else {
        return new BlazingFishingBobberEntity(
            new FakePlayer((ServerWorld) world, new GameProfile(UUID.randomUUID(), "")), world, 0,
            0);
      }
    }, EntityClassification.MISC).size(0.25F, 0.25F).disableSerialization().disableSummoning()
        .setTrackingRange(64).immuneToFire().setUpdateInterval(5)
        .setShouldReceiveVelocityUpdates(true).build(RegistryReference.BLAZING_BOBBER);
    BLAZING_BOBBER.setRegistryName(CombustiveFishing.MODID, RegistryReference.BLAZING_BOBBER);
  }
}
