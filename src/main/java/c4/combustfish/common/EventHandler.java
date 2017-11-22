/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.util.EntityAccessor;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.logging.log4j.Level;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EventHandler {

    private static Random rand = new Random();

    @SubscribeEvent
    public void onCombustionCodPickUp(EntityItemPickupEvent e) {

        if (!e.getEntityPlayer().world.isRemote) {

            EntityItem entityItem = e.getItem();

            if (entityItem.getItem().getItem() == CombustFishItems.combustiveCod && entityItem.ticksExisted < 40) {
                e.getEntityPlayer().setFire(1);
            }
        }
    }

    @SubscribeEvent
    public void onOcelotJoin(EntityJoinWorldEvent evt) {

        Entity entity = evt.getEntity();

        if (entity instanceof EntityOcelot && !entity.world.isRemote) {
            EntityOcelot ocelot = (EntityOcelot) entity;
            EntityAITempt aiTempt = null;
            Set<Item> temptItems = new HashSet<>();
            try {
                aiTempt = EntityAccessor.getAITempt(ocelot);
                temptItems = EntityAccessor.getTemptItems(aiTempt);
            } catch (Exception e){
                CombustiveFishing.logger.log(Level.ERROR, "Failed to access field aiTempt or access field temptItem");
            }
            ocelot.tasks.removeTask(aiTempt);
            temptItems.add(CombustFishItems.skeletonFish);
            EntityAITempt newAITempt = new EntityAITempt(ocelot, 0.6D, true, temptItems);
            ocelot.tasks.addTask(3, newAITempt);
            try {
                EntityAccessor.setAITempt(ocelot, newAITempt);
            } catch (Exception e) {
                CombustiveFishing.logger.log(Level.ERROR, "Failed to set field aiTempt");
            }
        }
    }

    @SubscribeEvent
    public void pigmanLootLoad(LootTableLoadEvent e) {

        if (e.getName().equals(LootTableList.ENTITIES_ZOMBIE_PIGMAN)) {

            LootTable lootTable = e.getTable();
            LootTable injectTable = e.getLootTableManager().getLootTableFromLocation(new ResourceLocation(CombustiveFishing.MODID, "inject/zombie_pigman"));

            lootTable.addPool(injectTable.getPool("golden_rod"));
            lootTable.addPool(injectTable.getPool("nether_fish"));
        }
    }
}
