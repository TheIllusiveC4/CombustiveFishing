/*
 * Copyright (C) 2017-2019  C4
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

package top.theillusivec4.combustivefishing.common.entity;

import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.entity.ai.pathing.LavaSwimmerPathNavigator;

public abstract class AbstractLavaFishEntity extends LavaMobEntity {

  private static final DataParameter<Boolean> FROM_BUCKET = EntityDataManager
      .createKey(AbstractLavaFishEntity.class, DataSerializers.BOOLEAN);

  public AbstractLavaFishEntity(EntityType<? extends AbstractLavaFishEntity> type, World worldIn) {
    super(type, worldIn);
    this.moveController = new MoveHelperController(this);
  }

  @Override
  protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
    return sizeIn.height * 0.65F;
  }

  @Override
  protected void registerAttributes() {
    super.registerAttributes();
    this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(3.0D);
  }

  @Override
  public boolean preventDespawn() {
    return this.isFromBucket();
  }

  public static boolean canSpawn(EntityType<? extends AbstractLavaFishEntity> type, IWorld worldIn,
      SpawnReason reason, BlockPos blockPos, Random randomIn) {
    return worldIn.getBlockState(blockPos).getBlock() == Blocks.LAVA
        && worldIn.getBlockState(blockPos.up()).getBlock() == Blocks.LAVA;
  }

  @Override
  public boolean canDespawn(double distanceToClosestPlayer) {
    return !this.isFromBucket() && !this.hasCustomName();
  }

  @Override
  public int getMaxSpawnedInChunk() {
    return 8;
  }

  @Override
  protected void registerData() {
    super.registerData();
    this.dataManager.register(FROM_BUCKET, false);
  }

  private boolean isFromBucket() {
    return this.dataManager.get(FROM_BUCKET);
  }

  public void setFromBucket(boolean p_203706_1_) {
    this.dataManager.set(FROM_BUCKET, p_203706_1_);
  }

  @Override
  public void writeAdditional(CompoundNBT compound) {
    super.writeAdditional(compound);
    compound.putBoolean("FromBucket", this.isFromBucket());
  }

  @Override
  public void readAdditional(CompoundNBT compound) {
    super.readAdditional(compound);
    this.setFromBucket(compound.getBoolean("FromBucket"));
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
    this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.6D, 1.4D,
        EntityPredicates.NOT_SPECTATING::test));
    this.goalSelector.addGoal(4, new AbstractLavaFishEntity.SwimGoal(this));
  }

  @Nonnull
  @Override
  protected PathNavigator createNavigator(@Nonnull World worldIn) {
    return new LavaSwimmerPathNavigator(this, worldIn);
  }

  @Override
  public void travel(@Nonnull Vec3d moveVector) {

    if (this.isServerWorld() && this.isInLava()) {
      this.moveRelative(0.01F, moveVector);
      this.move(MoverType.SELF, this.getMotion());
      this.setMotion(this.getMotion().scale(0.9D));

      if (this.getAttackTarget() == null) {
        this.setMotion(this.getMotion().add(0.0D, -0.005D, 0.0D));
      }
    } else {
      super.travel(moveVector);
    }

  }

  @Override
  public void livingTick() {

    if (!this.isInLava() && this.onGround && this.collidedVertically) {
      this.setMotion(this.getMotion().add((this.rand.nextFloat() * 2.0F - 1.0F) * 0.05F, 0.4F, (this.rand.nextFloat() * 2.0F - 1.0F) * 0.05F));
      this.onGround = false;
      this.isAirBorne = true;
      this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getSoundPitch());
    }
    super.livingTick();
  }

  @Override
  protected boolean processInteract(PlayerEntity player, Hand hand) {
    ItemStack itemstack = player.getHeldItem(hand);

    if (itemstack.getItem() == Items.LAVA_BUCKET && this.isAlive()) {
      this.playSound(SoundEvents.ITEM_BUCKET_FILL_FISH, 1.0F, 1.0F);
      itemstack.shrink(1);
      ItemStack itemstack1 = this.getFishBucket();
      this.setBucketData(itemstack1);
      if (!this.world.isRemote) {
        CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayerEntity) player, itemstack1);
      }

      if (itemstack.isEmpty()) {
        player.setHeldItem(hand, itemstack1);
      } else if (!player.inventory.addItemStackToInventory(itemstack1)) {
        player.dropItem(itemstack1, false);
      }

      this.remove();
      return true;
    } else {
      return super.processInteract(player, hand);
    }
  }

  protected void setBucketData(ItemStack bucket) {

    if (this.hasCustomName()) {
      bucket.setDisplayName(this.getCustomName());
    }
  }

  protected abstract ItemStack getFishBucket();

  protected boolean func_212800_dy() {
    return true;
  }

  protected abstract SoundEvent getFlopSound();

  @Nonnull
  @Override
  protected SoundEvent getSwimSound() {
    return SoundEvents.ENTITY_FISH_SWIM;
  }

  static class SwimGoal extends RandomSwimmingGoal {

    private final AbstractLavaFishEntity fish;

    public SwimGoal(AbstractLavaFishEntity fish) {
      super(fish, 1.0D, 40);
      this.fish = fish;
    }

    public boolean shouldExecute() {
      return this.fish.func_212800_dy() && super.shouldExecute();
    }
  }

  static class MoveHelperController extends MovementController {

    private final AbstractLavaFishEntity fish;

    MoveHelperController(AbstractLavaFishEntity fish) {
      super(fish);
      this.fish = fish;
    }

    public void tick() {
      if (this.fish.areEyesInFluid(FluidTags.LAVA)) {
        this.fish.setMotion(this.fish.getMotion().add(0.0D, 0.005D, 0.0D));
      }

      if (this.action == MovementController.Action.MOVE_TO && !this.fish.getNavigator().noPath()) {
        double d0 = this.posX - this.fish.posX;
        double d1 = this.posY - this.fish.posY;
        double d2 = this.posZ - this.fish.posZ;
        double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        d1 = d1 / d3;
        float f = (float) (MathHelper.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;
        this.fish.rotationYaw = this.limitAngle(this.fish.rotationYaw, f, 90.0F);
        this.fish.renderYawOffset = this.fish.rotationYaw;
        float f1 = (float) (this.speed * this.fish
            .getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue());
        this.fish.setAIMoveSpeed(MathHelper.lerp(0.125F, this.fish.getAIMoveSpeed(), f1));
        this.fish.setMotion(
            this.fish.getMotion().add(0.0D, (double) this.fish.getAIMoveSpeed() * d1 * 0.1D, 0.0D));
      } else {
        this.fish.setAIMoveSpeed(0.0F);
      }
    }
  }
}
