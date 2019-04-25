package top.theillusivec4.combustivefishing.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.client.model.ModelSwordfish;
import top.theillusivec4.combustivefishing.common.entity.EntitySearingSwordfish;

import javax.annotation.Nonnull;

public class RenderSwordfish extends RenderLiving<EntitySearingSwordfish> {

    private static final ResourceLocation SWORDFISH_LOCATION = new ResourceLocation(CombustiveFishing.MODID, "textures/entity/swordfish.png");

    public RenderSwordfish(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSwordfish(), 0.7F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntitySearingSwordfish entity) {
        return SWORDFISH_LOCATION;
    }

    @Override
    protected void applyRotations(EntitySearingSwordfish entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
        GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);

        if (!entityLiving.isInLava()) {
            GlStateManager.translatef(0.1F, 0.1F, -0.1F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
