package top.theillusivec4.combustivefishing.common.init;

import net.minecraft.entity.EntityType;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.EntityBlazingHook;
import top.theillusivec4.combustivefishing.common.entity.EntityCombustiveCod;
import top.theillusivec4.combustivefishing.common.entity.EntityThrownCombustiveCod;

public class CombustiveFishingEntities {

    public static final EntityType<EntityCombustiveCod> COMBUSTIVE_COD;
    public static final EntityType<EntityThrownCombustiveCod> THROWN_COMBUSTIVE_COD;
    public static final EntityType<EntityBlazingHook> BLAZING_BOBBER;

    static {
        COMBUSTIVE_COD = EntityType.Builder.create(EntityCombustiveCod.class, EntityCombustiveCod::new)
                .tracker(80, 3, true)
                .build("combustive_cod");
        COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, "combustive_cod");

        THROWN_COMBUSTIVE_COD = EntityType.Builder.create(EntityThrownCombustiveCod.class, EntityThrownCombustiveCod::new)
                .tracker(64, 10, true)
                .build("thrown_combustive_cod");
        THROWN_COMBUSTIVE_COD.setRegistryName(CombustiveFishing.MODID, "thrown_combustive_cod");

        BLAZING_BOBBER = EntityType.Builder.createNothing(EntityBlazingHook.class)
                .disableSerialization()
                .disableSummoning()
                .tracker(64, 5, true)
                .build("blazing_bobber");
        BLAZING_BOBBER.setRegistryName(CombustiveFishing.MODID, "blazing_bobber");
    }
}
