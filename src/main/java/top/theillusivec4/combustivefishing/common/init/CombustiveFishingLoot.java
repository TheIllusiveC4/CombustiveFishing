package top.theillusivec4.combustivefishing.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class CombustiveFishingLoot {

    public static final ResourceLocation NETHER_FISHING = create("nether_fishing");
    public static final ResourceLocation LAVA_FISHING = create("lava_fishing");
    public static final ResourceLocation COMBUSTIVE_COD = create("entity/combustive_cod");

    private static ResourceLocation create(String path) {
        return new ResourceLocation(CombustiveFishing.MODID, path);
    }

    public static void registerLootTables() {
        LootTableList.register(COMBUSTIVE_COD);
    }
}