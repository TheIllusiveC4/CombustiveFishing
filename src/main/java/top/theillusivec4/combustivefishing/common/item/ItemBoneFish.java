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

import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Particles;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.IParticleData;
import net.minecraft.util.Hand;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;
import top.theillusivec4.combustivefishing.common.registry.RegistryReference;

public class ItemBoneFish extends Item {

  public ItemBoneFish() {
    super(new Item.Properties().group(ItemGroup.MISC));
    this.setRegistryName(CombustiveFishing.MODID, RegistryReference.BONE_FISH);
    MinecraftForge.EVENT_BUS.register(this);
  }

  @SubscribeEvent
  public void onOcelotJoin(EntityJoinWorldEvent evt) {
    Entity entity = evt.getEntity();

    if (entity instanceof OcelotEntity && !entity.world.isRemote) {
      OcelotEntity ocelot = (OcelotEntity) entity;
      EntityAITempt aiTempt = ObfuscationReflectionHelper
          .getPrivateValue(EntityOcelot.class, ocelot, "field_70914_e");
      Ingredient temptItems = ObfuscationReflectionHelper
          .getPrivateValue(EntityOcelot.class, ocelot, "field_195402_bB");
      ocelot.tasks.removeTask(aiTempt);
      Ingredient newTemptItems = Ingredient.merge(
          Lists.newArrayList(temptItems, Ingredient.fromItems(CombustiveFishingItems.BONE_FISH)));
      EntityAITempt newAITempt = new EntityAITempt(ocelot, 0.6D, newTemptItems, true);
      ocelot.tasks.addTask(3, newAITempt);
      ObfuscationReflectionHelper
          .setPrivateValue(EntityOcelot.class, ocelot, newAITempt, "field_70914_e");
    }
  }

  @Override
  public boolean itemInteractionForEntity(ItemStack stack, PlayerEntity playerIn,
      LivingEntity target, Hand hand) {

    if (target instanceof TameableEntity && !((TameableEntity) target).isTamed()) {

      if (target instanceof OcelotEntity) {
        EntityOcelot ocelot = (EntityOcelot) target;
        EntityAITempt aiTempt = ObfuscationReflectionHelper
            .getPrivateValue(EntityOcelot.class, ocelot, "field_70914_e");

        if ((aiTempt == null || aiTempt.isRunning()) && playerIn.getDistanceSq(ocelot) < 9.0D) {

          if (!playerIn.abilities.isCreativeMode) {
            stack.shrink(1);
          }

          if (!ocelot.world.isRemote) {

            if (random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(ocelot, playerIn)) {
              ocelot.setTamedBy(playerIn);
              ocelot.setTameSkin(1 + ocelot.world.rand.nextInt(3));
              playTameEffect(ocelot, true);
              ocelot.getAISit().setSitting(true);
              ocelot.world.setEntityState(ocelot, (byte) 7);
            } else {
              playTameEffect(ocelot, false);
              ocelot.world.setEntityState(ocelot, (byte) 6);
            }
          }
        }
      } else if (target instanceof EntityWolf) {
        EntityWolf wolf = (EntityWolf) target;

        if (!wolf.isAngry()) {

          if (!playerIn.abilities.isCreativeMode) {
            stack.shrink(1);
          }

          if (!wolf.world.isRemote) {

            if (random.nextInt(3) == 0 && ForgeEventFactory.onAnimalTame(wolf, playerIn)) {
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
      }
      return true;
    }
    return false;
  }

  private static void playTameEffect(EntityTameable tameable, boolean play) {
    Random rand = tameable.getRNG();
    IParticleData iparticledata = Particles.HEART;

    if (!play) {
      iparticledata = Particles.SMOKE;
    }

    for (int i = 0; i < 7; ++i) {
      double d0 = rand.nextGaussian() * 0.02D;
      double d1 = rand.nextGaussian() * 0.02D;
      double d2 = rand.nextGaussian() * 0.02D;
      tameable.world.addParticle(iparticledata,
          tameable.posX + (double) (rand.nextFloat() * tameable.width * 2.0F)
              - (double) tameable.width,
          tameable.posY + 0.5D + (double) (rand.nextFloat() * tameable.height),
          tameable.posZ + (double) (rand.nextFloat() * tameable.width * 2.0F)
              - (double) tameable.width, d0, d1, d2);
    }
  }
}
