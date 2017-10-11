/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish;

import c4.combustfish.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(   modid = CombustiveFishing.MODID,
        name = CombustiveFishing.MODNAME,
        version = CombustiveFishing.MODVER,
        dependencies = "required-after:forge@[14.23.0.2491,)",
        useMetadata = true,
        acceptedMinecraftVersions = "[1.12.2, 1.13)")

public class CombustiveFishing {

    public static final String MODID = "combustfish";
    public static final String MODNAME = "Combustive Fishing";
    public static final String MODVER = "1.0.1";

    @SidedProxy(clientSide = "c4.combustfish.proxy.ClientProxy", serverSide = "c4.combustfish.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static CombustiveFishing instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        proxy.preInit(e);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent e) {
        proxy.postInit(e);
    }
}
