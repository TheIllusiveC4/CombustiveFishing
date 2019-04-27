package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import top.theillusivec4.combustivefishing.CombustiveFishing;

public class ItemSwordfishBill extends ItemHotFish {

    public ItemSwordfishBill() {
        super(FishType.SEARING_SWORDFISH, new Item.Properties().group(ItemGroup.MISC));
        this.setRegistryName(CombustiveFishing.MODID, "swordfish_bill");
    }
}
