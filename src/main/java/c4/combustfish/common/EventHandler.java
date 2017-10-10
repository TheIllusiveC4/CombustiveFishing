/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHandler {

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
    public void pigmanLootLoad(LootTableLoadEvent e) {

        if (e.getName().equals(LootTableList.ENTITIES_ZOMBIE_PIGMAN)) {

            LootTable lootTable = e.getTable();
            LootTable injectTable = e.getLootTableManager().getLootTableFromLocation(new ResourceLocation(CombustiveFishing.MODID, "inject/zombie_pigman"));

            lootTable.addPool(injectTable.getPool("golden_rod"));
            lootTable.addPool(injectTable.getPool("combustive_cod"));
        }
    }
}
