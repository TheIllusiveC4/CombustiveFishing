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

package top.theillusivec4.combustivefishing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;

@Mod(CombustiveFishing.MODID)
public class CombustiveFishing {

  public static final String MODID = "combustivefishing";
  public static final Logger LOGGER = LogManager.getLogger();

  public CombustiveFishing() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    eventBus.addListener(this::clientSetup);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    CombustiveFishingLoot.registerLootTables();
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    Minecraft mc = evt.getMinecraftSupplier().get();
    CombustiveFishingEntities.registerEntityRenders(mc);
  }

  @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
  public static class Events {

    @SubscribeEvent
    public static void onPigmanLootTableLoad(final LootTableLoadEvent evt) {

      if (evt.getName().equals(LootTables.)) {
        LootTable lootTable = evt.getTable();
        LootTable inject = evt.getLootTableManager()
            .getLootTableFromLocation(CombustiveFishingLoot.PIGMAN_INJECT);
        lootTable.addPool(inject.getPool("blazing_fishing_rod"));
        lootTable.addPool(inject.getPool("nether_fish"));
      }
    }
  }
}
