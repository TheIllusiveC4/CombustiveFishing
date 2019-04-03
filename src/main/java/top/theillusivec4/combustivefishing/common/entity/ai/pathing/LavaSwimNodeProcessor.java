package top.theillusivec4.combustivefishing.common.entity.ai.pathing;

import net.minecraft.block.state.IBlockState;
import net.minecraft.fluid.IFluidState;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.pathfinding.PathType;
import net.minecraft.pathfinding.SwimNodeProcessor;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LavaSwimNodeProcessor extends SwimNodeProcessor {

    private boolean isSwordfish;

    public LavaSwimNodeProcessor(boolean isSwordfish) {
        super(isSwordfish);
        this.isSwordfish = isSwordfish;
    }

    @Override
    public int findPathOptions(@Nonnull PathPoint[] pathOptions, @Nonnull PathPoint currentPoint, @Nonnull PathPoint targetPoint, float maxDistance) {
        int i = 0;

        for(EnumFacing enumfacing : EnumFacing.values()) {
            PathPoint pathpoint = this.getLavaNode(currentPoint.x + enumfacing.getXOffset(), currentPoint.y + enumfacing.getYOffset(), currentPoint.z + enumfacing.getZOffset());
            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
        }

        return i;
    }

    @Nullable
    private PathPoint getLavaNode(int p_186328_1_, int p_186328_2_, int p_186328_3_) {
        PathNodeType pathnodetype = this.isFree(p_186328_1_, p_186328_2_, p_186328_3_);
        return (!this.isSwordfish || pathnodetype != PathNodeType.BREACH) && pathnodetype != PathNodeType.LAVA ? null : this.openPoint(p_186328_1_, p_186328_2_, p_186328_3_);
    }

    @Nonnull
    @Override
    public PathNodeType getPathNodeType(IBlockReader blockaccessIn, int x, int y, int z) {
        BlockPos blockpos = new BlockPos(x, y, z);
        IFluidState ifluidstate = blockaccessIn.getFluidState(blockpos);
        IBlockState iblockstate = blockaccessIn.getBlockState(blockpos);
        if (ifluidstate.isEmpty() && iblockstate.allowsMovement(blockaccessIn, blockpos.down(), PathType.WATER) && iblockstate.isAir(blockaccessIn, blockpos)) {
            return PathNodeType.BREACH;
        } else {
            return ifluidstate.isTagged(FluidTags.LAVA) && iblockstate.allowsMovement(blockaccessIn, blockpos, PathType.WATER) ? PathNodeType.LAVA : PathNodeType.BLOCKED;
        }
    }

    private PathNodeType isFree(int p_186327_1_, int p_186327_2_, int p_186327_3_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = p_186327_1_; i < p_186327_1_ + this.entitySizeX; ++i) {
            for(int j = p_186327_2_; j < p_186327_2_ + this.entitySizeY; ++j) {
                for(int k = p_186327_3_; k < p_186327_3_ + this.entitySizeZ; ++k) {
                    IFluidState ifluidstate = this.blockaccess.getFluidState(blockpos$mutableblockpos.setPos(i, j, k));
                    IBlockState iblockstate = this.blockaccess.getBlockState(blockpos$mutableblockpos.setPos(i, j, k));
                    if (ifluidstate.isEmpty() && iblockstate.allowsMovement(this.blockaccess, blockpos$mutableblockpos.down(), PathType.WATER) && iblockstate.isAir()) {
                        return PathNodeType.BREACH;
                    }

                    if (!ifluidstate.isTagged(FluidTags.LAVA)) {
                        return PathNodeType.BLOCKED;
                    }
                }
            }
        }

        IBlockState iblockstate1 = this.blockaccess.getBlockState(blockpos$mutableblockpos);
        if (iblockstate1.allowsMovement(this.blockaccess, blockpos$mutableblockpos, PathType.WATER)) {
            return PathNodeType.LAVA;
        } else {
            return PathNodeType.BLOCKED;
        }
    }
}
