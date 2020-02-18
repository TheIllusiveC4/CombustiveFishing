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

package top.theillusivec4.combustivefishing.common.item;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Random;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.registry.RegistryReference;

public class BoneFishItem extends Item {

  private static final Method SET_TRUSTING = ObfuscationReflectionHelper
      .findMethod(OcelotEntity.class, "func_213528_r", boolean.class);

  public BoneFishItem() {
    super(new Item.Properties().group(ItemGroup.MISC));
    this.setRegistryName(CombustiveFishing.MODID, RegistryReference.BONE_FISH);
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn,
      LivingEntity target, Hand hand) {

    if (target instanceof OcelotEntity) {
      OcelotEntity ocelot = (OcelotEntity) target;
      TemptGoal temptGoal = ObfuscationReflectionHelper
          .getPrivateValue(OcelotEntity.class, ocelot, "field_70914_e");

      if ((temptGoal == null || temptGoal.isRunning()) && playerIn.getDistanceSq(ocelot) < 9.0D) {

        if (!playerIn.abilities.isCreativeMode) {
          stack.shrink(1);
        }

        if (!ocelot.world.isRemote) {

          if (random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(ocelot, playerIn)) {
            try {
              SET_TRUSTING.invoke(ocelot, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
              CombustiveFishing.LOGGER.error("Error invoking setTrusting for " + ocelot);
              CombustiveFishing.LOGGER.error(e);
            }
            playTameEffect(ocelot, true);
            ocelot.world.setEntityState(ocelot, (byte) 41);
          } else {
            playTameEffect(ocelot, false);
            ocelot.world.setEntityState(ocelot, (byte) 40);
          }
        }
      }
      return true;
    } else if (target instanceof WolfEntity && !((WolfEntity) target).isTamed()) {
      WolfEntity wolf = (WolfEntity) target;

      if (!wolf.isAngry()) {

        if (!playerIn.abilities.isCreativeMode) {
          stack.shrink(1);
        }

        if (!wolf.world.isRemote) {

          if (random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(wolf, playerIn)) {
            wolf.setTamedBy(playerIn);
            wolf.getNavigator().clearPath();
            wolf.setAttackTarget(null);
            wolf.setHealth(20.0F);
            playTameEffect(wolf, true);
            wolf.getAISit().setSitting(true);
            wolf.world.setEntityState(wolf, (byte) 7);
          } else {
            playTameEffect(wolf, false);
            wolf.world.setEntityState(wolf, (byte) 6);
          }
        }
      }
      return true;
    }
    return false;
  }

  private static void playTameEffect(AnimalEntity entity, boolean play) {
    Random rand = entity.getRNG();
    IParticleData iparticledata = ParticleTypes.HEART;

    if (!play) {
      iparticledata = ParticleTypes.SMOKE;
    }

    for (int i = 0; i < 7; ++i) {
      double d0 = rand.nextGaussian() * 0.02D;
      double d1 = rand.nextGaussian() * 0.02D;
      double d2 = rand.nextGaussian() * 0.02D;
      entity.world.addParticle(iparticledata,
          entity.posX + (double) (rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity
              .getWidth(), entity.posY + 0.5D + (double) (rand.nextFloat() * entity.getHeight()),
          entity.posZ + (double) (rand.nextFloat() * entity.getWidth() * 2.0F) - (double) entity
              .getWidth(), d0, d1, d2);
    }
  }
}
