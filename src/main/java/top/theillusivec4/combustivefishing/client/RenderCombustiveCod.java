package top.theillusivec4.combustivefishing.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.model.ModelCod;
import net.minecraft.entity.passive.EntityCod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.EntityCombustiveCod;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RenderCombustiveCod extends RenderLiving<EntityCombustiveCod> {
    private static final ResourceLocation COD_LOCATION = new ResourceLocation(CombustiveFishing.MODID, "textures/entity/combustive_cod.png");

    public RenderCombustiveCod(RenderManager renderManager) {
        super(renderManager, new ModelCod(), 0.2F);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityCombustiveCod entity) {
        return COD_LOCATION;
    }

    @Override
    protected void applyRotations(EntityCombustiveCod entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
        GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
        if (!entityLiving.isInLava()) {
            GlStateManager.translatef(0.1F, 0.1F, -0.1F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }

    }
}