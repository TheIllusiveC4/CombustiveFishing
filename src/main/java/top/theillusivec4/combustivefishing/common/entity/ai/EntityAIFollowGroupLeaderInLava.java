package top.theillusivec4.combustivefishing.common.entity.ai;

import net.minecraft.entity.ai.EntityAIBase;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaGroupFish;

import java.util.List;
import java.util.function.Predicate;

public class EntityAIFollowGroupLeaderInLava extends EntityAIBase {

    private final AbstractLavaGroupFish taskOwner;
    private int navigateTimer;
    private int field_212826_c;

    public EntityAIFollowGroupLeaderInLava(AbstractLavaGroupFish p_i49857_1_) {
        this.taskOwner = p_i49857_1_;
        this.field_212826_c = this.func_212825_a(p_i49857_1_);
    }

    protected int func_212825_a(AbstractLavaGroupFish p_212825_1_) {
        return 200 + p_212825_1_.getRNG().nextInt(200) % 20;
    }

    @Override
    public boolean shouldExecute() {

        if (this.taskOwner.func_212812_dE()) {
            return false;
        } else if (this.taskOwner.func_212802_dB()) {
            return true;
        } else if (this.field_212826_c > 0) {
            --this.field_212826_c;
            return false;
        } else {
            this.field_212826_c = this.func_212825_a(this.taskOwner);
            Predicate<AbstractLavaGroupFish> predicate = (p_212824_0_) -> p_212824_0_.func_212811_dD() || !p_212824_0_.func_212802_dB();
            List<AbstractLavaGroupFish> list = this.taskOwner.world.getEntitiesWithinAABB(this.taskOwner.getClass(), this.taskOwner.getBoundingBox().grow(8.0D, 8.0D, 8.0D), predicate);
            AbstractLavaGroupFish abstractgroupfish = list.stream().filter(AbstractLavaGroupFish::func_212811_dD).findAny().orElse(this.taskOwner);
            abstractgroupfish.func_212810_a(list.stream().filter((p_212823_0_) -> !p_212823_0_.func_212802_dB()));
            return this.taskOwner.func_212802_dB();
        }
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.taskOwner.func_212802_dB() && this.taskOwner.func_212809_dF();
    }

    @Override
    public void startExecuting() {
        this.navigateTimer = 0;
    }

    @Override
    public void resetTask() {
        this.taskOwner.func_212808_dC();
    }

    @Override
    public void tick() {
        if (--this.navigateTimer <= 0) {
            this.navigateTimer = 10;
            this.taskOwner.func_212805_dG();
        }
    }
}