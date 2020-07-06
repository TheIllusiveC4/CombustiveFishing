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

package top.theillusivec4.combustivefishing;

import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.combustivefishing.client.CombustiveFishingRenderer;
import top.theillusivec4.combustivefishing.common.CommonEventHandler;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaFishEntity;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingEntities;

@Mod(CombustiveFishing.MODID)
public class CombustiveFishing {

  public static final String MODID = "combustivefishing";
  public static final Logger LOGGER = LogManager.getLogger();

  public CombustiveFishing() {
    IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
    eventBus.addListener(this::setup);
    eventBus.addListener(this::postSetup);
    eventBus.addListener(this::clientSetup);
  }

  private void setup(final FMLCommonSetupEvent evt) {
    MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
  }

  private void postSetup(final FMLLoadCompleteEvent evt) {
    GlobalEntityTypeAttributes.put(CombustiveFishingEntities.COMBUSTIVE_COD,
        AbstractLavaFishEntity.registerAttributes().func_233813_a_());
    GlobalEntityTypeAttributes.put(CombustiveFishingEntities.SEARING_SWORDFISH,
        SearingSwordfishEntity.registerAttribute().func_233813_a_());
  }

  private void clientSetup(final FMLClientSetupEvent evt) {
    CombustiveFishingRenderer.register();
  }
}
