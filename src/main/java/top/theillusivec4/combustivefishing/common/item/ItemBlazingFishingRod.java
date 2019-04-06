package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.EntityBlazingHook;

import javax.annotation.Nonnull;

public class ItemBlazingFishingRod extends ItemFishingRod {

    public ItemBlazingFishingRod() {
        super(new Item.Properties().defaultMaxDamage(128).group(ItemGroup.TOOLS));
        this.setRegistryName(CombustiveFishing.MODID, "blazing_fishing_rod");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack stack = playerIn.getHeldItem(handIn);

        if (playerIn.fishEntity != null) {
            int i = playerIn.fishEntity.handleHookRetraction(stack);
            stack.damageItem(i, playerIn);
            playerIn.swingArm(handIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        } else {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote) {
                EntityBlazingHook blazingHook = new EntityBlazingHook(worldIn, playerIn);
                int j = EnchantmentHelper.getFishingSpeedBonus(stack);

                if (j > 0) {
                    blazingHook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(stack);
                if (k > 0) {
                    blazingHook.setLuck(k);
                }
                worldIn.spawnEntity(blazingHook);
            }
            playerIn.swingArm(handIn);
            playerIn.addStat(StatList.ITEM_USED.get(this));
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public int getItemEnchantability() {
        return 22;
    }
}
