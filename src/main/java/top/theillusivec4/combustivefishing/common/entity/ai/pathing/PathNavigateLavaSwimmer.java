package top.theillusivec4.combustivefishing.common.entity.ai.pathing;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PathNavigateLavaSwimmer extends PathNavigateSwimmer {

    private boolean isSwordfish;

    public PathNavigateLavaSwimmer(EntityLiving entitylivingIn, World worldIn) {
        super(entitylivingIn, worldIn);
    }

    @Nonnull
    @Override
    protected PathFinder getPathFinder() {
        this.isSwordfish = false;
        this.nodeProcessor = new LavaSwimNodeProcessor(isSwordfish);
        return new PathFinder(this.nodeProcessor);
    }
}
