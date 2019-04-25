package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;

import javax.annotation.Nullable;

public class EntitySearingSwordfish extends AbstractLavaFish {

    public EntitySearingSwordfish(World world) {
        super(CombustiveFishingEntities.SEARING_SWORDFISH, world);
        this.setSize(0.9F, 0.6F);
    }

    @Override
    protected boolean processInteract(EntityPlayer player, EnumHand hand) {
        return false;
    }

    @Override
    protected ItemStack getFishBucket() {
        return ItemStack.EMPTY;
    }

    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return CombustiveFishingLoot.COMBUSTIVE_COD;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }
}
