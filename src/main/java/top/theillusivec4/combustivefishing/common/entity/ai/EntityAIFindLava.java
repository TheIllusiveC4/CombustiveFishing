package top.theillusivec4.combustivefishing.common.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class EntityAIFindLava extends EntityAIBase {
    private final EntityCreature creature;

    public EntityAIFindLava(EntityCreature creature) {
        this.creature = creature;
    }

    @Override
    public boolean shouldExecute() {
        return this.creature.onGround && !this.creature.world.getFluidState(new BlockPos(this.creature)).isTagged(FluidTags.LAVA);
    }

    @Override
    public void startExecuting() {
        BlockPos blockpos = null;

        for(BlockPos blockpos1 : BlockPos.MutableBlockPos.getAllInBoxMutable(MathHelper.floor(this.creature.posX - 2.0D), MathHelper.floor(this.creature.posY - 2.0D), MathHelper.floor(this.creature.posZ - 2.0D), MathHelper.floor(this.creature.posX + 2.0D), MathHelper.floor(this.creature.posY), MathHelper.floor(this.creature.posZ + 2.0D))) {

            if (this.creature.world.getFluidState(blockpos1).isTagged(FluidTags.LAVA)) {
                blockpos = blockpos1;
                break;
            }
        }

        if (blockpos != null) {
            this.creature.getMoveHelper().setMoveTo((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), 1.0D);
        }

    }
}
