package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.*;
import net.minecraft.item.crafting.Ingredient;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;

import javax.annotation.Nonnull;

public class ItemSearingSword extends ItemSword {

    public ItemSearingSword() {
        super(new IItemTier() {
            @Override
            public int getMaxUses() {
                return 131;
            }

            @Override
            public float getEfficiency() {
                return 1.0F;
            }

            @Override
            public float getAttackDamage() {
                return 4.0F;
            }

            @Override
            public int getHarvestLevel() {
                return 1;
            }

            @Override
            public int getEnchantability() {
                return 0;
            }

            @Nonnull
            @Override
            public Ingredient getRepairMaterial() {
                return Ingredient.fromItems(CombustiveFishingItems.SWORDFISH_BILL);
            }
        }, 6, 1.4F, new Item.Properties().group(ItemGroup.COMBAT));
        this.setRegistryName(CombustiveFishing.MODID, "searing_sword");
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, @Nonnull EntityLivingBase attacker) {

        if (!target.isImmuneToFire()) {
            target.setFire(10);
        }
        return super.hitEntity(stack, target, attacker);
    }
}
