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

package top.theillusivec4.combustivefishing.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import top.theillusivec4.combustivefishing.common.entity.EntitySearingSwordfish;

public class EntityAILavaJump extends EntityAIBase {
    private static final int[] field_211697_a = new int[]{0, 1, 4, 5, 6, 7};
    private final EntitySearingSwordfish swordfish;
    private final int chance;
    private boolean outOfWater;

    public EntityAILavaJump(EntitySearingSwordfish swordfish, int chance) {
        this.swordfish = swordfish;
        this.chance = chance;
        this.setMutexBits(5);
    }

    @Override
    public boolean shouldExecute() {

        if (this.swordfish.getRNG().nextInt(this.chance) != 0) {
            return false;
        } else {
            EnumFacing enumfacing = this.swordfish.getAdjustedHorizontalFacing();
            int i = enumfacing.getXOffset();
            int j = enumfacing.getZOffset();
            BlockPos blockpos = new BlockPos(this.swordfish);

            for(int k : field_211697_a) {

                if (!this.canSwimIn(blockpos, i, j, k) || !this.canLeapIn(blockpos, i, j, k)) {
                    return false;
                }
            }
            return true;
        }
    }

    private boolean canSwimIn(BlockPos pos, int dx, int dz, int horizontalMultiplier) {
        BlockPos blockpos = pos.add(dx * horizontalMultiplier, 0, dz * horizontalMultiplier);
        return this.swordfish.world.getFluidState(blockpos).isTagged(FluidTags.LAVA) && !this.swordfish.world.getBlockState(blockpos).getMaterial().blocksMovement();
    }

    private boolean canLeapIn(BlockPos pos, int dx, int dz, int horizontalMultiplier) {
        return this.swordfish.world.getBlockState(pos.add(dx * horizontalMultiplier, 1, dz * horizontalMultiplier)).isAir() && this.swordfish.world.getBlockState(pos.add(dx * horizontalMultiplier, 2, dz * horizontalMultiplier)).isAir();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return (!(this.swordfish.motionY * this.swordfish.motionY < (double)0.03F) || this.swordfish.rotationPitch == 0.0F || !(Math.abs(this.swordfish.rotationPitch) < 10.0F) || !this.swordfish.isInWater()) && !this.swordfish.onGround;
    }

    @Override
    public boolean isInterruptible() {
        return false;
    }

    @Override
    public void startExecuting() {
        EnumFacing enumfacing = this.swordfish.getAdjustedHorizontalFacing();
        this.swordfish.motionX += (double)enumfacing.getXOffset() * 0.6D;
        this.swordfish.motionY += 0.7D;
        this.swordfish.motionZ += (double)enumfacing.getZOffset() * 0.6D;
        this.swordfish.getNavigator().clearPath();
    }

    @Override
    public void resetTask() {
        this.swordfish.rotationPitch = 0.0F;
    }

    @Override
    public void tick() {
        boolean flag = this.outOfWater;

        if (!flag) {
            IFluidState ifluidstate = this.swordfish.world.getFluidState(new BlockPos(this.swordfish));
            this.outOfWater = ifluidstate.isTagged(FluidTags.LAVA);
        }

        if (this.outOfWater && !flag) {
            this.swordfish.playSound(SoundEvents.ENTITY_DOLPHIN_JUMP, 1.0F, 1.0F);
        }

        if (this.swordfish.motionY * this.swordfish.motionY < (double)0.03F && this.swordfish.rotationPitch != 0.0F) {
            this.swordfish.rotationPitch = this.updateRotation(this.swordfish.rotationPitch, 0.0F, 0.2F);
        } else {
            double d2 = Math.sqrt(this.swordfish.motionX * this.swordfish.motionX + this.swordfish.motionY * this.swordfish.motionY + this.swordfish.motionZ * this.swordfish.motionZ);
            double d0 = Math.sqrt(this.swordfish.motionX * this.swordfish.motionX + this.swordfish.motionZ * this.swordfish.motionZ);
            double d1 = Math.signum(-this.swordfish.motionY) * Math.acos(d0 / d2) * (double)(180F / (float)Math.PI);
            this.swordfish.rotationPitch = (float)d1;
        }

    }

    protected float updateRotation(float p_205147_1_, float p_205147_2_, float p_205147_3_) {
        float f;

        for(f = p_205147_2_ - p_205147_1_; f < -180.0F; f += 360.0F) {
            ;
        }

        while(f >= 180.0F) {
            f -= 360.0F;
        }
        return p_205147_1_ + p_205147_3_ * f;
    }
}