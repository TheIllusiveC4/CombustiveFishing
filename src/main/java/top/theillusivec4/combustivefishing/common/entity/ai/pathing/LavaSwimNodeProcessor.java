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

package top.theillusivec4.combustivefishing.common.entity.ai.pathing;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class LavaSwimNodeProcessor extends SwimNodeProcessor {

  public LavaSwimNodeProcessor() {
    super(false);
  }

  @Override
  public int func_222859_a(@Nonnull PathPoint[] p_222859_1_, @Nonnull PathPoint p_222859_2_) {
    int i = 0;

    for (Direction direction : Direction.values()) {
      PathPoint pathpoint = this.getLavaNode(p_222859_2_.x + direction.getXOffset(),
          p_222859_2_.y + direction.getYOffset(), p_222859_2_.z + direction.getZOffset());

      if (pathpoint != null && !pathpoint.visited) {
        p_222859_1_[i++] = pathpoint;
      }
    }
    return i;
  }

  @Nullable
  private PathPoint getLavaNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
    PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
    return pathnodetype != PathNodeType.LAVA ? null
        : this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_);
  }

  @Nonnull
  @Override
  public PathNodeType getPathNodeType(IBlockReader blockaccessIn, int x, int y, int z) {
    BlockPos blockpos = new BlockPos(x, y, z);
    FluidState ifluidstate = blockaccessIn.getFluidState(blockpos);
    BlockState blockstate = blockaccessIn.getBlockState(blockpos);

    if (ifluidstate.isEmpty() && blockstate.isAir(blockaccessIn, blockpos)) {
      return PathNodeType.BREACH;
    } else {
      return ifluidstate.isTagged(FluidTags.LAVA) ? PathNodeType.LAVA : PathNodeType.BLOCKED;
    }
  }

  private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
    BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable();

    for (int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i) {

      for (int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j) {

        for (int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k) {
          FluidState ifluidstate = this.blockaccess
              .getFluidState(blockpos$mutableblockpos.setPos(i, j, k));
          BlockState blockstate = this.blockaccess
              .getBlockState(blockpos$mutableblockpos.setPos(i, j, k));

          if (ifluidstate.isEmpty() && blockstate.isAir(blockaccess, blockpos$mutableblockpos)) {
            return PathNodeType.BREACH;
          }

          if (!ifluidstate.isTagged(FluidTags.LAVA)) {
            return PathNodeType.BLOCKED;
          }
        }
      }
    }
    return PathNodeType.LAVA;
  }
}
