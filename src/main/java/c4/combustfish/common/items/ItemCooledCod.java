package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCooledCod extends ItemFood {

    public ItemCooledCod() {
        super(7, 0.8F, false);
        this.setRegistryName("cooled_cod");
        this.setTranslationKey(CombustiveFishing.MODID + ".cooled_cod");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
