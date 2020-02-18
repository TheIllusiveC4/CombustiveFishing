/*
 * Copyright (C) 2017-2019  C4
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

import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

/**
 * Created using Tabula 7.0.0
 */
public class SwordfishModel<T extends Entity> extends EntityModel<T> {

  public RendererModel body;
  public RendererModel tail;
  public RendererModel fin_tail;
  public RendererModel sword;
  public RendererModel bill;
  public RendererModel fin_top;
  public RendererModel fin_right;
  public RendererModel fin_left;

  public SwordfishModel() {
    this.textureWidth = 64;
    this.textureHeight = 32;
    this.fin_tail = new RendererModel(this, 19, 0);
    this.fin_tail.setRotationPoint(0.0F, 20.0F, 9.0F);
    this.fin_tail.addBox(-0.5F, -4.0F, 0.0F, 1, 8, 3, 0.0F);
    this.fin_right = new RendererModel(this, 45, 0);
    this.fin_right.setRotationPoint(-1.0F, 22.5F, -3.0F);
    this.fin_right.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
    this.setRotateAngle(fin_right, 0.0F, 0.0F, 0.3490658503988659F);
    this.fin_top = new RendererModel(this, 39, 0);
    this.fin_top.setRotationPoint(0.0F, 20.0F, 0.0F);
    this.fin_top.addBox(-0.5F, -6.0F, -3.0F, 1, 4, 2, 0.0F);
    this.setRotateAngle(fin_top, -0.3490658503988659F, 0.0F, 0.0F);
    this.fin_left = new RendererModel(this, 51, 0);
    this.fin_left.setRotationPoint(1.0F, 22.5F, -3.0F);
    this.fin_left.addBox(-0.5F, 0.0F, -1.0F, 1, 4, 2, 0.0F);
    this.setRotateAngle(fin_left, 0.0F, 0.0F, -0.3490658503988659F);
    this.body = new RendererModel(this, 0, 0);
    this.body.setRotationPoint(0.0F, 20.0F, 0.0F);
    this.body.addBox(-1.5F, -3.0F, -6.5F, 3, 6, 13, 0.0F);
    this.tail = new RendererModel(this, 0, 0);
    this.tail.setRotationPoint(0.0F, 20.0F, 6.0F);
    this.tail.addBox(-1.0F, -1.5F, 0.0F, 2, 3, 4, 0.0F);
    this.sword = new RendererModel(this, 27, 0);
    this.sword.setRotationPoint(0.0F, 20.0F, 0.0F);
    this.sword.addBox(-0.5F, -0.5F, -16.0F, 1, 1, 10, 0.0F);
    this.bill = new RendererModel(this, 27, 0);
    this.bill.setRotationPoint(0.0F, 20.0F, 0.0F);
    this.bill.addBox(-1.0F, -1.5F, -9.0F, 2, 3, 3, 0.0F);
  }

  @Override
  public void render(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
      float netHeadYaw, float headPitch, float scale) {
    this.fin_tail.render(scale);
    this.fin_right.render(scale);
    this.fin_top.render(scale);
    this.fin_left.render(scale);
    this.body.render(scale);
    this.tail.render(scale);
    this.sword.render(scale);
    this.bill.render(scale);
  }

  /**
   * This is a helper function from Tabula to set the rotation of model parts
   */
  public void setRotateAngle(RendererModel modelRenderer, float x, float y, float z) {
    modelRenderer.rotateAngleX = x;
    modelRenderer.rotateAngleY = y;
    modelRenderer.rotateAngleZ = z;
  }

  @Override
  public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount,
      float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
    rotate(entityIn, ageInTicks, headPitch, netHeadYaw, this.fin_tail, this.fin_left,
        this.fin_right, this.fin_top, this.body, this.tail, this.sword, this.bill);

    if (Entity.horizontalMag(entityIn.getMotion()) > 1.0E-7D) {
      this.tail.rotateAngleY = -0.1F * MathHelper.cos(ageInTicks * 0.3F);
      this.fin_tail.rotateAngleY = -0.2F * MathHelper.cos(ageInTicks * 0.3F);
    }
  }

  private void rotate(T entityIn, float ageInTicks, float headPitch, float netHeadYaw,
      RendererModel... rendererModels) {

    for (RendererModel rendererModel : rendererModels) {
      rendererModel.rotateAngleX = headPitch * ((float) Math.PI / 180F);
      rendererModel.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);

      if (Entity.horizontalMag(entityIn.getMotion()) > 1.0E-7D) {
        rendererModel.rotateAngleX += -0.05F + -0.05F * MathHelper.cos(ageInTicks * 0.3F);
      }
    }
  }
}
