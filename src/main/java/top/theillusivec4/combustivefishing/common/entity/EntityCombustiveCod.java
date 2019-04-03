package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;

import javax.annotation.Nullable;

public class EntityCombustiveCod extends AbstractLavaGroupFish {

    public EntityCombustiveCod(World world) {
        super(EntityType.COD, world);
        this.setSize(0.5F, 0.3F);
    }

    protected ItemStack getFishBucket() {
        return new ItemStack(CombustiveFishingItems.COMBUSTIVE_COD_BUCKET);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_COD;
    }

    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }
}
