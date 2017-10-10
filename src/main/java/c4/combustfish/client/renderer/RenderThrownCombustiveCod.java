/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.client.renderer;

import c4.combustfish.common.entities.EntityThrownCombustiveCod;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class RenderThrownCombustiveCod extends Render<EntityThrownCombustiveCod>
{
    protected final Item item;
    private final RenderItem itemRenderer;
    private ResourceLocation entityTexture = new ResourceLocation("combustfish:textures/items/combustive_cod.png");
    public static final Factory FACTORY = new Factory();

    public RenderThrownCombustiveCod(RenderManager renderManagerIn, Item itemIn, RenderItem itemRendererIn)
    {
        super(renderManagerIn);
        this.item = itemIn;
        this.itemRenderer = itemRendererIn;
    }

    /**
     * Renders the desired {@code T} type Entity.
     */
    public void doRender(EntityThrownCombustiveCod entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float)(this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.bindTexture(entityTexture);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        this.itemRenderer.renderItem(this.getStackToRender(entity), ItemCameraTransforms.TransformType.GROUND);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    public ItemStack getStackToRender(EntityThrownCombustiveCod entityIn)
    {
        return new ItemStack(this.item);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityThrownCombustiveCod entity) {
        return entityTexture;
    }

    public static class Factory implements IRenderFactory<EntityThrownCombustiveCod> {

        @Override
        public Render<? super EntityThrownCombustiveCod> createRenderFor(RenderManager manager) {
            return new RenderThrownCombustiveCod(manager, CombustFishItems.combustiveCod, Minecraft.getMinecraft().getRenderItem());
        }
    }
}
