package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMagmaString extends Item {

    public ItemMagmaString() {
        super();
        this.setRegistryName("magma_string");
        this.setTranslationKey(CombustiveFishing.MODID + ".magma_string");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
