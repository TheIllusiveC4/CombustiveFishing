/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.util.init;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.client.renderer.RenderGoldenHook;
import c4.combustfish.client.renderer.RenderThrownCombustiveCod;
import c4.combustfish.common.entities.EntityGoldenHook;
import c4.combustfish.common.entities.EntityThrownCombustiveCod;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CombustFishEntities {

    public static void init() {

        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(CombustiveFishing.MODID, "thrown_combustive_cod"), EntityThrownCombustiveCod.class, "Thrown Combustion Cod", id++, CombustiveFishing.instance, 64, 10, true);
        EntityRegistry.registerModEntity(new ResourceLocation(CombustiveFishing.MODID, "golden_hook"), EntityGoldenHook.class, "Golden Hook", id++, CombustiveFishing.instance, 64, 10, true);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {

        RenderingRegistry.registerEntityRenderingHandler(EntityThrownCombustiveCod.class, RenderThrownCombustiveCod.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityGoldenHook.class, RenderGoldenHook.FACTORY);
    }
}
