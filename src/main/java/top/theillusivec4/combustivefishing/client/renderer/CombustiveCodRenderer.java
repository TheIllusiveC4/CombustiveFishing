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

import com.mojang.blaze3d.matrix.MatrixStack;
import javax.annotation.Nonnull;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.CodModel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.CombustiveCodEntity;

public class CombustiveCodRenderer extends
    MobRenderer<CombustiveCodEntity, CodModel<CombustiveCodEntity>> {

  private static final ResourceLocation COD_LOCATION = new ResourceLocation(CombustiveFishing.MODID,
      "textures/entity/combustive_cod.png");

  public CombustiveCodRenderer(EntityRendererManager renderManager) {
    super(renderManager, new CodModel<>(), 0.3F);
  }

  @Nonnull
  @Override
  public ResourceLocation getEntityTexture(@Nonnull CombustiveCodEntity entity) {
    return COD_LOCATION;
  }

  @Override
  protected void applyRotations(CombustiveCodEntity entityLiving,
      @Nonnull MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
    super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
    float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
    BlockState state = entityLiving.getBlockState();
    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f));

    if (!state.getFluidState().isTagged(FluidTags.LAVA)) {
      matrixStackIn.translate(0.1F, 0.1F, -0.1F);
      matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
    }
  }
}