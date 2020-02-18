/*
 * Copyright (c) 2017-2020 C4
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

import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.entity.ai.FollowLavaSchoolLeaderGoal;

public abstract class AbstractLavaGroupFishEntity extends AbstractLavaFishEntity {

  private AbstractLavaGroupFishEntity groupLeader;
  private int groupSize = 1;

  public AbstractLavaGroupFishEntity(EntityType<? extends AbstractLavaGroupFishEntity> type,
      World world) {
    super(type, world);
  }

  @Override
  protected void registerGoals() {
    super.registerGoals();
    this.goalSelector.addGoal(5, new FollowLavaSchoolLeaderGoal(this));
  }

  @Override
  public int getMaxSpawnedInChunk() {
    return this.getMaxGroupSize();
  }

  public int getMaxGroupSize() {
    return super.getMaxSpawnedInChunk();
  }

  @Override
  protected boolean func_212800_dy() {
    return !this.hasGroupLeader();
  }

  public boolean hasGroupLeader() {
    return this.groupLeader != null && this.groupLeader.isAlive();
  }

  public AbstractLavaGroupFishEntity joinGroup(AbstractLavaGroupFishEntity leader) {
    this.groupLeader = leader;
    leader.increaseGroupSize();
    return leader;
  }

  public void leaveGroup() {
    this.groupLeader.decreaseGroupSize();
    this.groupLeader = null;
  }

  private void increaseGroupSize() {
    ++this.groupSize;
  }

  private void decreaseGroupSize() {
    --this.groupSize;
  }

  public boolean canGroupGrow() {
    return this.isGroupLeader() && this.groupSize < this.getMaxGroupSize();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.isGroupLeader() && this.world.rand.nextInt(200) == 1) {
      List<AbstractLavaFishEntity> list = this.world
          .getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
      if (list.size() <= 1) {
        this.groupSize = 1;
      }
    }

  }

  public boolean isGroupLeader() {
    return this.groupSize > 1;
  }

  public boolean inRangeOfGroupLeader() {
    return this.getDistanceSq(this.groupLeader) <= 121.0D;
  }

  public void moveToGroupLeader() {

    if (this.hasGroupLeader()) {
      this.getNavigator().tryMoveToEntityLiving(this.groupLeader, 1.0D);
    }
  }

  public void func_212810_a(Stream<AbstractLavaGroupFishEntity> p_212810_1_) {
    p_212810_1_.limit(this.getMaxGroupSize() - this.groupSize)
        .filter((p_212801_1_) -> p_212801_1_ != this)
        .forEach((p_212804_1_) -> p_212804_1_.joinGroup(this));
  }

  @Nullable
  public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn,
      SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
    super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

    if (spawnDataIn == null) {
      spawnDataIn = new AbstractLavaGroupFishEntity.GroupData(this);
    } else {
      this.joinGroup(((AbstractLavaGroupFishEntity.GroupData) spawnDataIn).field_212822_a);
    }
    return spawnDataIn;
  }

  public static class GroupData implements ILivingEntityData {

    public final AbstractLavaGroupFishEntity field_212822_a;

    public GroupData(AbstractLavaGroupFishEntity p_i49858_1_) {
      this.field_212822_a = p_i49858_1_;
    }
  }
}
