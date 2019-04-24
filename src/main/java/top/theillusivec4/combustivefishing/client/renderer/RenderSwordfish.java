package top.theillusivec4.combustivefishing.client.renderer;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
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
}
