/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.proxy;

import c4.combustfish.common.items.ItemCooledCod;
import c4.combustfish.common.items.ItemMagmaString;
import c4.combustfish.common.util.init.CombustFishEntities;
import c4.combustfish.common.entities.EntityThrownCombustiveCod;
import c4.combustfish.common.util.init.CombustFishItems;
import c4.combustfish.common.items.ItemCombustiveCod;
import c4.combustfish.common.items.ItemGoldenRod;
import c4.combustfish.common.EventHandler;
import c4.combustfish.common.util.init.CombustFishLoot;
import com.progwml6.natura.shared.NaturaCommons;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
        CombustFishEntities.init();
        CombustFishLoot.init();
    }

    public void init(FMLInitializationEvent e) {

        MinecraftForge.EVENT_BUS.register(new EventHandler());
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(CombustFishItems.combustiveCod, new BehaviorProjectileDispense()
        {
            /**
             * Return the projectile entity spawned by this dispense behavior.
             */
            protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn)
            {
                return new EntityThrownCombustiveCod(worldIn, position.getX(), position.getY(), position.getZ());
            }
        });
    }

    public void postInit(FMLPostInitializationEvent e) {

        OreDictionary.registerOre("stringFire", CombustFishItems.magmaString);

        if (Loader.isModLoaded("natura")) {
            OreDictionary.registerOre("stringFire", NaturaCommons.flameString);
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> e) {

        e.getRegistry().register(new ItemGoldenRod());
        e.getRegistry().register(new ItemCombustiveCod());
        e.getRegistry().register(new ItemCooledCod());
        e.getRegistry().register(new ItemMagmaString());
    }
}
