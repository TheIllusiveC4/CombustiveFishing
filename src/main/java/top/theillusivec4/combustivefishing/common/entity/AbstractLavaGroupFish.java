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

import net.minecraft.entity.EntityType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.entity.ai.EntityAIFollowGroupLeaderInLava;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbstractLavaGroupFish extends AbstractLavaFish {

    private AbstractLavaGroupFish field_212813_a;
    private int field_212814_b = 1;

    public AbstractLavaGroupFish(EntityType<?> p_i49856_1_, World p_i49856_2_) {
        super(p_i49856_1_, p_i49856_2_);
    }

    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(5, new EntityAIFollowGroupLeaderInLava(this));
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
        return !this.func_212802_dB();
    }

    public boolean func_212802_dB() {
        return this.field_212813_a != null && this.field_212813_a.isAlive();
    }

    public AbstractLavaGroupFish func_212803_a(AbstractLavaGroupFish p_212803_1_) {
        this.field_212813_a = p_212803_1_;
        p_212803_1_.func_212807_dH();
        return p_212803_1_;
    }

    public void func_212808_dC() {
        this.field_212813_a.func_212806_dI();
        this.field_212813_a = null;
    }

    private void func_212807_dH() {
        ++this.field_212814_b;
    }

    private void func_212806_dI() {
        --this.field_212814_b;
    }

    public boolean func_212811_dD() {
        return this.func_212812_dE() && this.field_212814_b < this.getMaxGroupSize();
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.func_212812_dE() && this.world.rand.nextInt(200) == 1) {
            List<AbstractLavaFish> list = this.world.getEntitiesWithinAABB(this.getClass(), this.getBoundingBox().grow(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.field_212814_b = 1;
            }
        }

    }

    public boolean func_212812_dE() {
        return this.field_212814_b > 1;
    }

    public boolean func_212809_dF() {
        return this.getDistanceSq(this.field_212813_a) <= 121.0D;
    }

    public void func_212805_dG() {
        if (this.func_212802_dB()) {
            this.getNavigator().tryMoveToEntityLiving(this.field_212813_a, 1.0D);
        }

    }

    public void func_212810_a(Stream<AbstractLavaGroupFish> p_212810_1_) {
        p_212810_1_.limit((long)(this.getMaxGroupSize() - this.field_212814_b)).filter((p_212801_1_) -> p_212801_1_ != this).forEach((p_212804_1_) -> p_212804_1_.func_212803_a(this));
    }

    @Nullable
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData entityLivingData, @Nullable NBTTagCompound itemNbt) {
        super.onInitialSpawn(difficulty, entityLivingData, itemNbt);
        if (entityLivingData == null) {
            entityLivingData = new AbstractLavaGroupFish.GroupData(this);
        } else {
            this.func_212803_a(((AbstractLavaGroupFish.GroupData)entityLivingData).field_212822_a);
        }

        return entityLivingData;
    }

    public static class GroupData implements IEntityLivingData {
        public final AbstractLavaGroupFish field_212822_a;

        public GroupData(AbstractLavaGroupFish p_i49858_1_) {
            this.field_212822_a = p_i49858_1_;
        }
    }
}
