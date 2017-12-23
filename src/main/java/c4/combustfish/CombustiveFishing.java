/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish;

import c4.combustfish.proxy.CommonProxy;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

@Mod(   modid = CombustiveFishing.MODID,
        name = CombustiveFishing.MODNAME,
        version = CombustiveFishing.MODVER,
        dependencies = "required-after:forge@[14.23.0.2491,)",
        acceptedMinecraftVersions = "[1.12, 1.13)",
        certificateFingerprint = "5d5b8aee896a4f5ea3f3114784742662a67ad32f")

public class CombustiveFishing {

    public static final String MODID = "combustfish";
    public static final String MODNAME = "Combustive Fishing";
    public static final String MODVER = "1.0.3.1";

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

    @Mod.EventHandler
    public void onFingerPrintViolation(FMLFingerprintViolationEvent evt) {
        FMLLog.log.log(Level.ERROR, "Invalid fingerprint detected! The file " + evt.getSource().getName() + " may have been tampered with. This version will NOT be supported by the author!");
    }
}
