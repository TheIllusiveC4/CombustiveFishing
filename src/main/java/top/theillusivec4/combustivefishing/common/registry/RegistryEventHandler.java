package top.theillusivec4.combustivefishing.common.registry;

import java.util.List;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaFishEntity;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.item.BlazingFishingRodItem;
import top.theillusivec4.combustivefishing.common.item.CombustiveCodItem;
import top.theillusivec4.combustivefishing.common.item.CooledBillItem;
import top.theillusivec4.combustivefishing.common.item.CooledCodItem;
import top.theillusivec4.combustivefishing.common.item.BoneFishItem;
import top.theillusivec4.combustivefishing.common.item.LavaFishBucketItem;
import top.theillusivec4.combustivefishing.common.item.SearingSwordItem;
import top.theillusivec4.combustivefishing.common.item.SwordfishBillItem;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryEventHandler {

  @SubscribeEvent
  public static void onItemsRegistry(final RegistryEvent.Register<Item> evt) {
    evt.getRegistry().registerAll(
        new LavaFishBucketItem(() -> CombustiveFishingEntities.COMBUSTIVE_COD, () -> Fluids.LAVA),
        new BlazingFishingRodItem(), new CombustiveCodItem(),
        new SpawnEggItem(CombustiveFishingEntities.COMBUSTIVE_COD, 16699430, 8804608,
            new Item.Properties().group(ItemGroup.MISC))
            .setRegistryName(CombustiveFishing.MODID, "combustive_cod_spawn_egg"),
        new SpawnEggItem(CombustiveFishingEntities.SEARING_SWORDFISH, 13045262, 16757683,
            new Item.Properties().group(ItemGroup.MISC))
            .setRegistryName(CombustiveFishing.MODID, "searing_swordfish_spawn_egg"),
        new CooledCodItem(), new BoneFishItem(), new SwordfishBillItem(), new CooledBillItem(),
        new SearingSwordItem());
  }

  @SubscribeEvent
  public static void onEntityRegistry(final RegistryEvent.Register<EntityType<?>> evt) {
    evt.getRegistry().registerAll(CombustiveFishingEntities.COMBUSTIVE_COD,
        CombustiveFishingEntities.BLAZING_BOBBER, CombustiveFishingEntities.THROWN_COMBUSTIVE_COD,
        CombustiveFishingEntities.SEARING_SWORDFISH);
    EntitySpawnPlacementRegistry.PlacementType type = EntitySpawnPlacementRegistry.PlacementType
        .create("in_lava",
            (i, b, e) -> i.getBlockState(b).getFluidState().isTagged(FluidTags.LAVA));
    EntitySpawnPlacementRegistry.register(CombustiveFishingEntities.COMBUSTIVE_COD, type,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractLavaFishEntity::canSpawn);
    EntitySpawnPlacementRegistry.register(CombustiveFishingEntities.SEARING_SWORDFISH, type,
        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AbstractLavaFishEntity::canSpawn);

    for (Biome biome : BiomeDictionary.getBiomes(BiomeDictionary.Type.NETHER)) {
      List<SpawnListEntry> list = biome.getSpawns(EntityClassification.WATER_CREATURE);
      list.add(new Biome.SpawnListEntry(CombustiveFishingEntities.COMBUSTIVE_COD, 15, 3, 6));
      list.add(new Biome.SpawnListEntry(CombustiveFishingEntities.SEARING_SWORDFISH, 1, 1, 2));
    }
  }
}
