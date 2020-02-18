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
