/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.util;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

public class EntityAccessor {

    private static final Method CHECK_COLLISION = ReflectionHelper.findMethod(EntityFishHook.class,"checkCollision", "func_190624_r");
    private static final Field AI_TEMPT = ReflectionHelper.findField(EntityOcelot.class, "aiTempt", "field_70914_e", "bD");
    private static final Method PLAY_TAME_EFFECT = ReflectionHelper.findMethod(EntityTameable.class, "playTameEffect", "func_70908_e", Boolean.TYPE);
    private static final Field TEMPT_ITEMS = ReflectionHelper.findField(EntityAITempt.class, "temptItem", "field_151484_k", "k");

    public static void checkCollision(EntityFishHook hook) throws IllegalAccessException, InvocationTargetException {
        CHECK_COLLISION.invoke(hook);
    }

    public static EntityAITempt getAITempt(EntityOcelot ocelot) throws IllegalAccessException {
        return (EntityAITempt) AI_TEMPT.get(ocelot);
    }

    public static void setAITempt(EntityOcelot ocelot, EntityAITempt aiTempt) throws IllegalAccessException {
        AI_TEMPT.set(ocelot, aiTempt);
    }

    public static Set<Item> getTemptItems(EntityAITempt aiTempt) throws IllegalAccessException {
        return (Set<Item>) TEMPT_ITEMS.get(aiTempt);
    }

    public static void playTameEffect(EntityTameable entityTameable, boolean bool) throws IllegalAccessException, InvocationTargetException {
        PLAY_TAME_EFFECT.invoke(entityTameable, bool);
    }
}
