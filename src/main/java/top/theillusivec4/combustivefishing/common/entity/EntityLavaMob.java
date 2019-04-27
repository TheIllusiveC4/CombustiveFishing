package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

public abstract class EntityLavaMob extends EntityWaterMob {

    protected EntityLavaMob(EntityType<?> type, World world) {
        super(type, world);
        this.isImmuneToFire = true;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 1 + this.world.rand.nextInt(6);
    }

    @Override
    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, @Nonnull DamageSource source) {

        if (this.isInLava()) {
            ResourceLocation resourcelocation = this.getLootTable();

            if (resourcelocation != null) {
                LootTable loottable = this.world.getServer().getLootTableManager().getLootTableFromLocation(resourcelocation);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer) this.world)).withLootedEntity(this).withDamageSource(source).withPosition(new BlockPos(this));
                if (wasRecentlyHit && this.attackingPlayer != null) {
                    lootcontext$builder = lootcontext$builder.withPlayer(this.attackingPlayer).withLuck(this.attackingPlayer.getLuck());
                    for (ItemStack itemstack : loottable.generateLootForPools(this.rand, lootcontext$builder.build())) {
                        ItemHandlerHelper.giveItemToPlayer(this.attackingPlayer, itemstack);
                    }
                }
            }
        } else {
            super.dropLoot(wasRecentlyHit, lootingModifier, source);
        }
    }

    @Override
    protected void updateAir(int currentAir) {

        if (this.isAlive() && !this.isInLava()) {
            this.setAir(currentAir - 1);

            if (this.isInWater()) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, Integer.MAX_VALUE);
            } else if (this.getAir() == -20 || this.isWet()) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }
    }

    @Nullable
    @Override
    public EntityItem entityDropItem(ItemStack stack, float offsetY) {

        if (stack.isEmpty()) {
            return null;
        } else {
            EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + (double)offsetY, this.posZ, stack);
            entityitem.setDefaultPickupDelay();
            entityitem.setInvulnerable(true);
            ObfuscationReflectionHelper.setPrivateValue(Entity.class, entityitem, true, "field_70178_ae");

            if (captureDrops() != null) {
                captureDrops().add(entityitem);
            } else {
                this.world.spawnEntity(entityitem);
            }
            return entityitem;
        }
    }
}
