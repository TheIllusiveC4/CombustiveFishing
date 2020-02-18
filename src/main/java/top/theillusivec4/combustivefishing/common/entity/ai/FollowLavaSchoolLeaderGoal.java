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

package top.theillusivec4.combustivefishing.common.entity.ai;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.ai.goal.Goal;
import top.theillusivec4.combustivefishing.common.entity.AbstractLavaGroupFishEntity;

public class FollowLavaSchoolLeaderGoal extends Goal {

  private final AbstractLavaGroupFishEntity taskOwner;
  private int navigateTimer;
  private int field_212826_c;

  public FollowLavaSchoolLeaderGoal(AbstractLavaGroupFishEntity p_i49857_1_) {
    this.taskOwner = p_i49857_1_;
    this.field_212826_c = this.func_212825_a(p_i49857_1_);
  }

  protected int func_212825_a(AbstractLavaGroupFishEntity p_212825_1_) {
    return 200 + p_212825_1_.getRNG().nextInt(200) % 20;
  }

  @Override
  public boolean shouldExecute() {

    if (this.taskOwner.isGroupLeader()) {
      return false;
    } else if (this.taskOwner.hasGroupLeader()) {
      return true;
    } else if (this.field_212826_c > 0) {
      --this.field_212826_c;
      return false;
    } else {
      this.field_212826_c = this.func_212825_a(this.taskOwner);
      Predicate<AbstractLavaGroupFishEntity> predicate = (p_212824_0_) -> p_212824_0_.canGroupGrow()
          || !p_212824_0_.hasGroupLeader();
      List<AbstractLavaGroupFishEntity> list = this.taskOwner.world
          .getEntitiesWithinAABB(this.taskOwner.getClass(),
              this.taskOwner.getBoundingBox().grow(8.0D, 8.0D, 8.0D), predicate);
      AbstractLavaGroupFishEntity abstractgroupfish = list.stream()
          .filter(AbstractLavaGroupFishEntity::canGroupGrow).findAny().orElse(this.taskOwner);
      abstractgroupfish
          .func_212810_a(list.stream().filter((p_212823_0_) -> !p_212823_0_.hasGroupLeader()));
      return this.taskOwner.hasGroupLeader();
    }
  }

  @Override
  public boolean shouldContinueExecuting() {
    return this.taskOwner.hasGroupLeader() && this.taskOwner.inRangeOfGroupLeader();
  }

  @Override
  public void startExecuting() {
    this.navigateTimer = 0;
  }

  @Override
  public void resetTask() {
    this.taskOwner.leaveGroup();
  }

  @Override
  public void tick() {
    if (--this.navigateTimer <= 0) {
      this.navigateTimer = 10;
      this.taskOwner.moveToGroupLeader();
    }
  }
}