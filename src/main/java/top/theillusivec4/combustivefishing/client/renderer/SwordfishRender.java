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

package top.theillusivec4.combustivefishing.client.renderer;

import com.mojang.blaze3d.platform.GlStateManager;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.client.model.SwordfishModel;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;

public class SwordfishRender extends
    MobRenderer<SearingSwordfishEntity, SwordfishModel<SearingSwordfishEntity>> {

  private static final ResourceLocation SWORDFISH_LOCATION = new ResourceLocation(
      CombustiveFishing.MODID, "textures/entity/swordfish.png");

  public SwordfishRender(EntityRendererManager renderManagerIn) {
    super(renderManagerIn, new SwordfishModel<>(), 0.7F);
  }

  @Nullable
  @Override
  protected ResourceLocation getEntityTexture(@Nonnull SearingSwordfishEntity entity) {
    return SWORDFISH_LOCATION;
  }

  @Override
  protected void preRenderCallback(@Nonnull SearingSwordfishEntity entitylivingbaseIn,
      float partialTickTime) {
    GlStateManager.scalef(1.0F, 1.0F, 1.0F);
  }

  @Override
  protected void applyRotations(SearingSwordfishEntity entityLiving, float ageInTicks,
      float rotationYaw, float partialTicks) {
    super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
  }
}
