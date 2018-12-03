/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.entities.EntityGoldenHook;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemGoldenRod extends ItemFishingRod {

    public ItemGoldenRod() {
        super();
        this.setMaxDamage(128);
        this.setRegistryName("golden_rod");
        this.setTranslationKey(CombustiveFishing.MODID + ".golden_rod");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (playerIn.fishEntity != null)
        {
            int i = playerIn.fishEntity.handleHookRetraction();
            itemstack.damageItem(i, playerIn);
            playerIn.swingArm(handIn);
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
        }
        else
        {
            worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            if (!worldIn.isRemote)
            {
                EntityGoldenHook entitygoldhook = new EntityGoldenHook(worldIn, playerIn);
                int j = EnchantmentHelper.getFishingSpeedBonus(itemstack);

                if (j > 0)
                {
                    entitygoldhook.setLureSpeed(j);
                }

                int k = EnchantmentHelper.getFishingLuckBonus(itemstack);

                if (k > 0)
                {
                    entitygoldhook.setLuck(k);
                }

                worldIn.spawnEntity(entitygoldhook);
            }

            playerIn.swingArm(handIn);
            playerIn.addStat(StatList.getObjectUseStats(this));
        }

        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }

    public int getItemEnchantability()
    {
        return 22;
    }
}
