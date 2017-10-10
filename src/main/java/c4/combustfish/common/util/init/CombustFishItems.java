/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.util.init;

import c4.combustfish.common.items.ItemCombustiveCod;
import c4.combustfish.common.items.ItemCooledCod;
import c4.combustfish.common.items.ItemGoldenRod;
import c4.combustfish.common.items.ItemMagmaString;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CombustFishItems {

    @GameRegistry.ObjectHolder("combustfish:combustive_cod")
    public static ItemCombustiveCod combustiveCod;

    @GameRegistry.ObjectHolder("combustfish:golden_rod")
    public static ItemGoldenRod goldenRod;

    @GameRegistry.ObjectHolder("combustfish:cooled_cod")
    public static ItemCooledCod cooledCod;

    @GameRegistry.ObjectHolder("combustfish:magma_string")
    public static ItemMagmaString magmaString;

    @SideOnly(Side.CLIENT)
    public static void initModels() {

        combustiveCod.initModel();
        cooledCod.initModel();
        goldenRod.initModel();
        magmaString.initModel();
    }
}
