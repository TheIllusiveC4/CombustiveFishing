package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGroup;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class ItemCooledBill extends ItemFood {

    public ItemCooledBill() {
        super(12, 1.6F, false, new Item.Properties().group(ItemGroup.FOOD));
        this.setRegistryName(CombustiveFishing.MODID, "cooled_bill");
    }
}
