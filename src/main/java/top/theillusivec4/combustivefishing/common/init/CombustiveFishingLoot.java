package top.theillusivec4.combustivefishing.common.init;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class CombustiveFishingLoot {

    public static final ResourceLocation NETHER_FISHING = create("gameplay/nether_fishing");
    public static final ResourceLocation LAVA_FISHING = create("gameplay/lava_fishing");
    public static final ResourceLocation COMBUSTIVE_COD = create("entity/combustive_cod");
    public static final ResourceLocation PIGMAN_INJECT = create("inject/zombie_pigman");
    public static final ResourceLocation SEARING_SWORDFISH = create("entity/searing_swordfish");

    private static ResourceLocation create(String path) {
        return new ResourceLocation(CombustiveFishing.MODID, path);
    }

    public static void registerLootTables() {
        LootTableList.register(COMBUSTIVE_COD);
        LootTableList.register(LAVA_FISHING);
        LootTableList.register(NETHER_FISHING);
        LootTableList.register(PIGMAN_INJECT);
        LootTableList.register(SEARING_SWORDFISH);
    }
}
