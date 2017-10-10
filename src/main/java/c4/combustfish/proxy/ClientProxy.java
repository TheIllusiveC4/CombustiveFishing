/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.proxy;

import c4.combustfish.common.util.init.CombustFishEntities;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {

        super.preInit(e);
        CombustFishEntities.initModels();
    }

    @Override
    public void init(FMLInitializationEvent e) {

        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        super.postInit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent e) {

        CombustFishItems.initModels();
    }

}
