package top.theillusivec4.combustivefishing.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.FishRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import top.theillusivec4.combustivefishing.client.renderer.CombustiveCodRender;
import top.theillusivec4.combustivefishing.client.renderer.SwordfishRender;
import top.theillusivec4.combustivefishing.common.entity.BlazingFishingBobberEntity;
import top.theillusivec4.combustivefishing.common.entity.CombustiveCodEntity;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;
import top.theillusivec4.combustivefishing.common.entity.ThrownCombustiveCodEntity;

public class CombustiveFishingRenderer {

  public static void register() {
    RenderingRegistry.registerEntityRenderingHandler(ThrownCombustiveCodEntity.class,
        (renderManager) -> new SpriteRenderer<>(renderManager,
            Minecraft.getInstance().getItemRenderer()));
    RenderingRegistry
        .registerEntityRenderingHandler(BlazingFishingBobberEntity.class, FishRenderer::new);
    RenderingRegistry
        .registerEntityRenderingHandler(CombustiveCodEntity.class, CombustiveCodRender::new);
    RenderingRegistry
        .registerEntityRenderingHandler(SearingSwordfishEntity.class, SwordfishRender::new);
  }
}
