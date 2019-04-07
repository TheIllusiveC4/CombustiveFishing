package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGroup;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class ItemCooledCod extends ItemFood {

    public ItemCooledCod() {
        super(7, 0.8F, false, new Item.Properties().group(ItemGroup.FOOD));
        this.setRegistryName(CombustiveFishing.MODID, "cooled_cod");
    }
}
