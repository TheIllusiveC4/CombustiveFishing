/*
 * Copyright (c) 2017-2020 C4
 *
 * This file is part of Combustive Fishing, a mod made for Minecraft.
 *
 * Combustive Fishing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Combustive Fishing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Combustive Fishing.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.combustivefishing.client.model;

import com.google.common.collect.ImmutableList;
import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Created using Tabula 7.0.0
 */
public class SwordfishModel<T extends Entity> extends SegmentedModel<T> {

  public ModelRenderer body;
  public ModelRenderer tail;
  public ModelRenderer fin_tail;
  public ModelRenderer sword;
  public ModelRenderer bill;
  public ModelRenderer fin_top;
  public ModelRenderer fin_right;
  public ModelRenderer fin_left;

  public SwordfishModel() {
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

  @Nonnull
  @Override
  public Iterable<ModelRenderer> getParts() {
    return ImmutableList
        .of(this.fin_tail, this.fin_right, this.fin_top, this.fin_left, this.body, this.tail,
            this.sword, this.bill);
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public void setRotationAngles(@Nonnull T entityIn, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch) {
    this.rotate(entityIn, ageInTicks, headPitch, netHeadYaw, this.fin_tail, this.fin_left,
        this.fin_right, this.fin_top, this.body, this.tail, this.sword, this.bill);

    if (Entity.horizontalMag(entityIn.getMotion()) > 1.0E-7D) {
      this.tail.rotateAngleY = -0.1F * MathHelper.cos(ageInTicks * 0.3F);
      this.fin_tail.rotateAngleY = -0.2F * MathHelper.cos(ageInTicks * 0.3F);
    }
  }

  private void rotate(T entityIn, float ageInTicks, float headPitch, float netHeadYaw,
      ModelRenderer... rendererModels) {

    for (ModelRenderer rendererModel : rendererModels) {
      rendererModel.rotateAngleX = headPitch * ((float) Math.PI / 180F);
      rendererModel.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);

      if (Entity.horizontalMag(entityIn.getMotion()) > 1.0E-7D) {
        rendererModel.rotateAngleX += -0.05F + -0.05F * MathHelper.cos(ageInTicks * 0.3F);
      }
    }
  }
}
