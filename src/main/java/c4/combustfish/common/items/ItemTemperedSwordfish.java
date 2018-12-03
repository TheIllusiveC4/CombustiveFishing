package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTemperedSwordfish extends ItemFood {

    public ItemTemperedSwordfish() {

        super(12, 1.6F, false);
        this.setMaxDamage(131);
        this.setRegistryName("tempered_swordfish");
        this.setTranslationKey(CombustiveFishing.MODID + ".tempered_swordfish");
    }

    @Override
    public boolean isDamageable() {
        return false;
    }

    @Override
    public int getHealAmount(ItemStack stack)
    {
        float maxDamage = getMaxDamage(stack);
        float damage = getDamage(stack);
        return Math.max((int)((maxDamage - damage) / maxDamage * super.getHealAmount(stack)), 1);
    }

    @Override
    public float getSaturationModifier(ItemStack stack)
    {
        float maxDamage = getMaxDamage(stack);
        float damage = getDamage(stack);
        return (maxDamage - damage) / maxDamage * super.getSaturationModifier(stack);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
