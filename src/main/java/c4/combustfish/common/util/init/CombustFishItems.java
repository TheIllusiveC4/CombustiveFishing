/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.util.init;

import c4.combustfish.common.items.*;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

public class CombustFishItems {

    public static ArrayList<Item> initItems;

    public static ItemCombustiveCod combustiveCod;
    public static ItemGoldenRod goldenRod;
    public static ItemCooledCod cooledCod;
    public static ItemMagmaString magmaString;
    public static ItemSearingSwordfish searingSwordfish;
    public static ItemTemperedSwordfish temperedSwordfish;
    public static ItemBoneFish skeletonFish;

    public static Item.ToolMaterial fishMaterial = EnumHelper.addToolMaterial("fish", 1, 131, 4.0F, 1.0F, 0);

    public static void init() {

        initItems = new ArrayList<>();

        combustiveCod = new ItemCombustiveCod();
        goldenRod = new ItemGoldenRod();
        cooledCod = new ItemCooledCod();
        magmaString = new ItemMagmaString();
        searingSwordfish = new ItemSearingSwordfish();
        temperedSwordfish = new ItemTemperedSwordfish();
        skeletonFish = new ItemBoneFish();

        initItems.add(combustiveCod);
        initItems.add(goldenRod);
        initItems.add(cooledCod);
        initItems.add(magmaString);
        initItems.add(searingSwordfish);
        initItems.add(temperedSwordfish);
        initItems.add(skeletonFish);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {

        combustiveCod.initModel();
        cooledCod.initModel();
        goldenRod.initModel();
        magmaString.initModel();
        searingSwordfish.initModel();
        temperedSwordfish.initModel();
        skeletonFish.initModel();
    }
}
