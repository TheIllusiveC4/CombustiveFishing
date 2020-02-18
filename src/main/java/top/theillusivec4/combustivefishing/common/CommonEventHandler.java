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

package top.theillusivec4.combustivefishing.common;

import com.google.common.collect.Lists;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;

public class CommonEventHandler {

  @SubscribeEvent
  public void onOcelotJoin(EntityJoinWorldEvent evt) {
    Entity entity = evt.getEntity();

    if (!entity.world.isRemote && entity instanceof OcelotEntity) {
      try {
        OcelotEntity ocelot = (OcelotEntity) entity;
        TemptGoal temptGoal = ObfuscationReflectionHelper
            .getPrivateValue(OcelotEntity.class, ocelot, "field_70914_e");
        Ingredient breedingItems = ObfuscationReflectionHelper
            .getPrivateValue(OcelotEntity.class, ocelot, "field_195402_bB");
        ocelot.goalSelector.removeGoal(temptGoal);
        Ingredient newBreedingItems = Ingredient.merge(Lists
            .newArrayList(breedingItems, Ingredient.fromItems(CombustiveFishingItems.BONE_FISH)));
        Class<?> temptClass = OcelotEntity.class.getDeclaredClasses()[0];
        Constructor<?> temptConstructor = temptClass.getDeclaredConstructors()[0];
        temptConstructor.setAccessible(true);
        TemptGoal newTemptGoal = (TemptGoal) temptConstructor
            .newInstance(ocelot, 0.6D, newBreedingItems, true);
        ocelot.goalSelector.addGoal(3, newTemptGoal);
        ObfuscationReflectionHelper
            .setPrivateValue(OcelotEntity.class, ocelot, newTemptGoal, "field_70914_e");
      } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
        CombustiveFishing.LOGGER.error("Error instantiating new tempt goal for ocelot " + entity);
      }
    }
  }
}
