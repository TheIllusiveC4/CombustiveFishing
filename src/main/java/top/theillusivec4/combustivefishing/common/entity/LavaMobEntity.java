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

import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.items.ItemHandlerHelper;

public abstract class LavaMobEntity extends WaterMobEntity {

  protected LavaMobEntity(EntityType<? extends WaterMobEntity> type, World world) {
    super(type, world);
    this.setPathPriority(PathNodeType.WATER, -1.0F);
    this.setPathPriority(PathNodeType.LAVA, 8.0F);
    this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
    this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
  }

  @Override
  protected int getExperiencePoints(PlayerEntity player) {
    return 1 + this.world.rand.nextInt(6);
  }

  @Override
  protected void dropLoot(@Nonnull DamageSource source, boolean wasRecentlyHit) {

    if (this.isInLava()) {
      ResourceLocation resourcelocation = this.getLootTable();
      MinecraftServer server = this.world.getServer();

      if (server != null) {
        LootTable loottable = server.getLootTableManager()
            .getLootTableFromLocation(resourcelocation);
        LootContext.Builder lootcontext = this.getLootContextBuilder(wasRecentlyHit, source);
        loottable.generate(lootcontext.build(LootParameterSets.ENTITY)).forEach(itemstack -> {
          if (wasRecentlyHit) {
            ItemHandlerHelper.giveItemToPlayer(this.attackingPlayer, itemstack);
          } else {
            this.entityDropItem(itemstack);
          }
        });
      }
    } else {
      super.dropLoot(source, wasRecentlyHit);
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
  public ItemEntity entityDropItem(ItemStack stack, float offsetY) {

    if (stack.isEmpty()) {
      return null;
    } else {
      ItemEntity itemEntity = new ItemEntity(this.world, this.getPosX(),
          this.getPosY() + (double) offsetY, this.getPosZ(), stack);
      itemEntity.setDefaultPickupDelay();
      itemEntity.setInvulnerable(true);
      Collection<ItemEntity> captureDrops = captureDrops();

      if (captureDrops != null) {
        captureDrops.add(itemEntity);
      } else {
        this.world.addEntity(itemEntity);
      }
      return itemEntity;
    }
  }
}
