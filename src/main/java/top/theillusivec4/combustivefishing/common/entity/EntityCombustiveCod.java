package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;

import javax.annotation.Nullable;

public class EntityCombustiveCod extends AbstractLavaGroupFish {

    public EntityCombustiveCod(World world) {
        super(CombustiveFishingEntities.COMBUSTIVE_COD, world);
        this.setSize(0.5F, 0.3F);
    }

    @Override
    protected ItemStack getFishBucket() {
        return new ItemStack(CombustiveFishingItems.COMBUSTIVE_COD_BUCKET);
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
