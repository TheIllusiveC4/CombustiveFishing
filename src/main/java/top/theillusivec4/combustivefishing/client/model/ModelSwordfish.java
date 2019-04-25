package top.theillusivec4.combustivefishing.client.model;

import net.minecraft.client.renderer.entity.model.ModelBase;
import net.minecraft.client.renderer.entity.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Created using Tabula 7.0.0
 */
public class ModelSwordfish extends ModelBase {

    public ModelRenderer body;
    public ModelRenderer tail;
    public ModelRenderer fin_tail;
    public ModelRenderer sword;
    public ModelRenderer bill;
    public ModelRenderer fin_top;
    public ModelRenderer fin_right;
    public ModelRenderer fin_left;

    public ModelSwordfish() {
        this.textureWidth = 64;
        this.textureHeight = 32;
        this.fin_tail = new ModelRenderer(this, 19, 0);
        this.fin_tail.setRotationPoint(0.0F, 20.0F, 9.0F);
        this.fin_tail.addBox(-0.5F, -4.0F, 0.0F, 1, 8, 3, 0.0F);
        this.fin_right = new ModelRenderer(this, 45, 0);
        this.fin_right.setRotationPoint(-1.0F, 22.5F, -3.0F);
        this.fin_right.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(fin_right, 0.0F, 0.0F, 0.3490658503988659F);
        this.fin_top = new ModelRenderer(this, 39, 0);
        this.fin_top.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.fin_top.addBox(-0.5F, -6.0F, -3.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(fin_top, -0.3490658503988659F, 0.0F, 0.0F);
        this.fin_left = new ModelRenderer(this, 51, 0);
        this.fin_left.setRotationPoint(1.0F, 22.5F, -3.0F);
        this.fin_left.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
        this.setRotateAngle(fin_left, 0.0F, 0.0F, -0.3490658503988659F);
        this.body = new ModelRenderer(this, 0, 0);
        this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.body.addBox(-1.5F, -3.0F, -6.5F, 3, 6, 13, 0.0F);
        this.tail = new ModelRenderer(this, 0, 0);
        this.tail.setRotationPoint(0.0F, 20.0F, 6.0F);
        this.tail.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 4, 0.0F);
        this.sword = new ModelRenderer(this, 27, 0);
        this.sword.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.sword.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 10, 0.0F);
        this.bill = new ModelRenderer(this, 27, 0);
        this.bill.setRotationPoint(0.0F, 20.0F, 0.0F);
        this.bill.addBox(-1.0F, -1.5F, -9.0F, 2, 3, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        this.fin_tail.render(f5);
        this.fin_right.render(f5);
        this.fin_top.render(f5);
        this.fin_left.render(f5);
        this.body.render(f5);
        this.tail.render(f5);
        this.sword.render(f5);
        this.bill.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn) {
        float f = 1.0F;

        if (!entityIn.isInLava()) {
            f = 1.5F;
        }
        this.fin_tail.rotateAngleY = -f * 0.45F * MathHelper.sin(0.6F * ageInTicks);
    }
}
