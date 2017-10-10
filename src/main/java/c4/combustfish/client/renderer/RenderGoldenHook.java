/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.client.renderer;

import c4.combustfish.common.entities.EntityGoldenHook;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderFish;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderGoldenHook extends RenderFish
{
    public static final Factory FACTORY = new Factory();

    public RenderGoldenHook(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    public static class Factory implements IRenderFactory<EntityGoldenHook> {

        @Override
        public Render<? super EntityGoldenHook> createRenderFor(RenderManager manager) {
            return new RenderGoldenHook(manager);
        }
    }
}
