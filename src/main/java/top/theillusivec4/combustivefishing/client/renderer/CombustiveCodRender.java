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

package top.theillusivec4.combustivefishing.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CodModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.CombustiveCodEntity;

public class CombustiveCodRender extends
    MobRenderer<CombustiveCodEntity, CodModel<CombustiveCodEntity>> {

  private static final ResourceLocation COD_LOCATION = new ResourceLocation(CombustiveFishing.MODID,
      "textures/entity/combustive_cod.png");

  public CombustiveCodRender(EntityRendererManager renderManager) {
    super(renderManager, new CodModel<>(), 0.3F);
  }

  @Nullable
  @Override
  protected ResourceLocation getEntityTexture(@Nonnull CombustiveCodEntity entity) {
    return COD_LOCATION;
  }

  @Override
  protected void applyRotations(CombustiveCodEntity entityLiving, float ageInTicks,
      float rotationYaw, float partialTicks) {
    super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
    float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
    GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);
    if (!entityLiving.isInWater()) {
      GlStateManager.translatef(0.1F, 0.1F, -0.1F);
      GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
    }

  }
}