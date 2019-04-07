package top.theillusivec4.combustivefishing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.entity.EntityType;
import net.minecraft.init.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSpawnEgg;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.combustivefishing.client.RenderCombustiveCod;
import top.theillusivec4.combustivefishing.client.RenderThrownCombustiveCod;
import top.theillusivec4.combustivefishing.common.entity.EntityBlazingHook;
import top.theillusivec4.combustivefishing.common.entity.EntityCombustiveCod;
import top.theillusivec4.combustivefishing.common.entity.EntityThrownCombustiveCod;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;
import top.theillusivec4.combustivefishing.common.item.ItemBlazingFishingRod;
import top.theillusivec4.combustivefishing.common.item.ItemBucketLavaFish;
import top.theillusivec4.combustivefishing.common.item.ItemCombustiveCod;
import top.theillusivec4.combustivefishing.common.item.ItemCooledCod;

@Mod(CombustiveFishing.MODID)
public class CombustiveFishing {

    public static final String MODID = "combustivefishing";

    public CombustiveFishing() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void setup(final FMLCommonSetupEvent evt) {
        CombustiveFishingLoot.registerLootTables();
    }

    private void clientSetup(final FMLClientSetupEvent evt) {
        CombustiveFishingEntities.registerEntityRenders();
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {

        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> evt) {
            evt.getRegistry().registerAll(
                    new ItemBucketLavaFish(CombustiveFishingEntities.COMBUSTIVE_COD, Fluids.LAVA),
                    new ItemBlazingFishingRod(),
                    new ItemCombustiveCod(),
                    new ItemSpawnEgg(CombustiveFishingEntities.COMBUSTIVE_COD, 16699430, 8804608, new Item.Properties().group(ItemGroup.MISC)).setRegistryName(MODID, "combustive_cod_spawn_egg"),
                    new ItemCooledCod());
        }

        @SubscribeEvent
        public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> evt) {
            evt.getRegistry().registerAll(
                    CombustiveFishingEntities.COMBUSTIVE_COD,
                    CombustiveFishingEntities.BLAZING_BOBBER,
                    CombustiveFishingEntities.THROWN_COMBUSTIVE_COD);
        }
    }
}
