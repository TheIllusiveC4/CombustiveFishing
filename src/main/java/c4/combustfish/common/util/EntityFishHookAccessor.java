/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.util;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityFishHookAccessor {

    private static final Method CHECK_COLLISION = ReflectionHelper.findMethod(EntityFishHook.class,"checkCollision", "func_190624_r");

    public static void checkCollision(EntityFishHook hook) throws IllegalAccessException, InvocationTargetException {
        CHECK_COLLISION.invoke(hook);
    }
}
