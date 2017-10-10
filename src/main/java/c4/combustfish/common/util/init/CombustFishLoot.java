package c4.combustfish.common.util.init;

import c4.combustfish.CombustiveFishing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public class CombustFishLoot {

    public static void init() {

        register("lava_fishing");
        register("lava_fishing/fish");
        register("lava_fishing/junk");
        register("nether_fishing");
        register("nether_fishing/fish");
        register("nether_fishing/junk");
        register("nether_fishing/treasure");
        register("inject/zombie_pigman");
    }

    private static void register(String resourcePathIn) {
        LootTableList.register(new ResourceLocation(CombustiveFishing.MODID, resourcePathIn));
    }
}
