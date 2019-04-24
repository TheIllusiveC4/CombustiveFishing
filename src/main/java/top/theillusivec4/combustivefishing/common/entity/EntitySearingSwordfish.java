package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.SoundEvents;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import top.theillusivec4.combustivefishing.common.entity.ai.EntityAIFindLava;
import top.theillusivec4.combustivefishing.common.entity.ai.EntityAILavaJump;
import top.theillusivec4.combustivefishing.common.entity.ai.pathing.PathNavigateLavaSwimmer;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntitySearingSwordfish extends EntityLavaMob implements IMob {

    public EntitySearingSwordfish(World world) {
        super(CombustiveFishingEntities.SEARING_SWORDFISH, world);
        this.setSize(0.9F, 0.6F);
        this.moveHelper = new MoveHelper(this);
        this.lookHelper = new EntityDolphinHelper(this, 10);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAIFindLava(this));
        this.tasks.addTask(4, new EntityAIWanderSwim(this, 1.0D, 10));
        this.tasks.addTask(4, new EntityAILookIdle(this));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(5, new EntityAILavaJump(this, 10));
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(14.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)1.0F);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) {
        return new PathNavigateLavaSwimmer(this, worldIn);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), (float)((int)this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue()));

        if (flag) {
            this.applyEnchantments(this, entityIn);
            this.playSound(SoundEvents.ENTITY_DOLPHIN_ATTACK, 1.0F, 1.0F);
        }
        return flag;
    }

    @Override
    public float getEyeHeight() {
        return 0.3F;
    }

    @Override
    public int getVerticalFaceSpeed() {
        return 1;
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return 1;
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {

        if (this.isServerWorld() && this.isInLava()) {
            this.moveRelative(strafe, vertical, forward, this.getAIMoveSpeed());
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double)0.9F;
            this.motionY *= (double)0.9F;
            this.motionZ *= (double)0.9F;
            if (this.getAttackTarget() == null) {
                this.motionY -= 0.005D;
            }
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    @Override
    public boolean canSpawn(IWorld worldIn, boolean fromSpawner) {
        return this.posY > 45.0D && this.posY < (double)worldIn.getSeaLevel() && worldIn.getBiome(new BlockPos(this)) != Biomes.OCEAN || worldIn.getBiome(new BlockPos(this)) != Biomes.DEEP_OCEAN && super.canSpawn(worldIn, fromSpawner);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_DOLPHIN_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_DOLPHIN_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? SoundEvents.ENTITY_DOLPHIN_AMBIENT_WATER : SoundEvents.ENTITY_DOLPHIN_AMBIENT;
    }

    @Nonnull
    @Override
    protected SoundEvent getSplashSound() {
        return SoundEvents.ENTITY_DOLPHIN_SPLASH;
    }

    @Nonnull
    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_DOLPHIN_SWIM;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return LootTableList.ENTITIES_DOLPHIN;
    }

    static class MoveHelper extends EntityMoveHelper {
        private final EntitySearingSwordfish swordfish;

        public MoveHelper(EntitySearingSwordfish entity) {
            super(entity);
            this.swordfish = entity;
        }

        public void tick() {

            if (this.swordfish.isInLava()) {
                this.swordfish.motionY += 0.005D;
            }

            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.swordfish.getNavigator().noPath()) {
                double d0 = this.posX - this.swordfish.posX;
                double d1 = this.posY - this.swordfish.posY;
                double d2 = this.posZ - this.swordfish.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 < (double)2.5000003E-7F) {
                    this.entity.setMoveForward(0.0F);
                } else {
                    float f = (float)(MathHelper.atan2(d2, d0) * (double)(180F / (float)Math.PI)) - 90.0F;
                    this.swordfish.rotationYaw = this.limitAngle(this.swordfish.rotationYaw, f, 10.0F);
                    this.swordfish.renderYawOffset = this.swordfish.rotationYaw;
                    this.swordfish.rotationYawHead = this.swordfish.rotationYaw;
                    float f1 = (float)(this.speed * this.swordfish.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
                    if (this.swordfish.isInLava()) {
                        this.swordfish.setAIMoveSpeed(f1 * 0.02F);
                        float f2 = -((float)(MathHelper.atan2(d1, (double)MathHelper.sqrt(d0 * d0 + d2 * d2)) * (double)(180F / (float)Math.PI)));
                        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85.0F, 85.0F);
                        this.swordfish.rotationPitch = this.limitAngle(this.swordfish.rotationPitch, f2, 5.0F);
                        float f3 = MathHelper.cos(this.swordfish.rotationPitch * ((float)Math.PI / 180F));
                        float f4 = MathHelper.sin(this.swordfish.rotationPitch * ((float)Math.PI / 180F));
                        this.swordfish.moveForward = f3 * f1;
                        this.swordfish.moveVertical = -f4 * f1;
                    } else {
                        this.swordfish.setAIMoveSpeed(f1 * 0.1F);
                    }

                }
            } else {
                this.swordfish.setAIMoveSpeed(0.0F);
                this.swordfish.setMoveStrafing(0.0F);
                this.swordfish.setMoveVertical(0.0F);
                this.swordfish.setMoveForward(0.0F);
            }
        }
    }
}
