package top.theillusivec4.combustivefishing.common.init;

import net.minecraft.util.ResourceLocation;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class CombustiveFishingLoot {

    public static final ResourceLocation NETHER_FISHING = create("nether_fishing");
    public static final ResourceLocation LAVA_FISHING = create("lava_fishing");

    private static ResourceLocation create(String path) {
        return new ResourceLocation(CombustiveFishing.MODID, path);
    }
}
