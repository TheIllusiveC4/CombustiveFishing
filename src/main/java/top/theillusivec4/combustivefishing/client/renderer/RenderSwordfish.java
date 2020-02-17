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

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.client.model.ModelSwordfish;
import top.theillusivec4.combustivefishing.common.entity.SearingSwordfishEntity;

import javax.annotation.Nonnull;

public class RenderSwordfish extends RenderLiving<SearingSwordfishEntity> {

    private static final ResourceLocation SWORDFISH_LOCATION = new ResourceLocation(CombustiveFishing.MODID, "textures/entity/swordfish.png");

    public RenderSwordfish(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelSwordfish(), 0.7F);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull SearingSwordfishEntity entity) {
        return SWORDFISH_LOCATION;
    }

    @Override
    protected void applyRotations(SearingSwordfishEntity entityLiving, float ageInTicks, float rotationYaw, float partialTicks) {
        super.applyRotations(entityLiving, ageInTicks, rotationYaw, partialTicks);
        float f = 4.3F * MathHelper.sin(0.6F * ageInTicks);
        GlStateManager.rotatef(f, 0.0F, 1.0F, 0.0F);

        if (!entityLiving.isInLava()) {
            GlStateManager.translatef(0.1F, 0.1F, -0.1F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }
    }
}
