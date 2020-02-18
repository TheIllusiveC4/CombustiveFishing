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

package top.theillusivec4.combustivefishing.common.entity.ai;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class FindLavaGoal extends Goal {

  private final CreatureEntity creature;

  public FindLavaGoal(CreatureEntity creature) {
    this.creature = creature;
  }

  @Override
  public boolean shouldExecute() {
    return this.creature.onGround && !this.creature.world.getFluidState(new BlockPos(this.creature))
        .isTagged(FluidTags.LAVA);
  }

  @Override
  public void startExecuting() {
    BlockPos blockpos = null;

    for (BlockPos blockpos1 : BlockPos.MutableBlockPos
        .getAllInBoxMutable(MathHelper.floor(this.creature.posX - 2.0D),
            MathHelper.floor(this.creature.posY - 2.0D),
            MathHelper.floor(this.creature.posZ - 2.0D),
            MathHelper.floor(this.creature.posX + 2.0D), MathHelper.floor(this.creature.posY),
            MathHelper.floor(this.creature.posZ + 2.0D))) {

      if (this.creature.world.getFluidState(blockpos1).isTagged(FluidTags.LAVA)) {
        blockpos = blockpos1;
        break;
      }
    }

    if (blockpos != null) {
      this.creature.getMoveHelper()
          .setMoveTo((double) blockpos.getX(), (double) blockpos.getY(), (double) blockpos.getZ(),
              1.0D);
    }

  }
}
