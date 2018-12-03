/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.entities.EntityThrownCombustiveCod;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCombustiveCod extends ItemSnowball {

    public ItemCombustiveCod() {
        super();
        this.setRegistryName("combustive_cod");
        this.setTranslationKey(CombustiveFishing.MODID + ".combustive_cod");
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public boolean onEntityItemUpdate(net.minecraft.entity.item.EntityItem entityItem)
    {
        if (entityItem.isInWater()) {

            World world = entityItem.world;
            double posX = entityItem.posX;
            double posY = entityItem.posY;
            double posZ = entityItem.posZ;

            if (!entityItem.world.isRemote) {
                int amount = entityItem.getItem().getCount();
                entityItem.setDead();
                world.spawnEntity(new EntityItem(entityItem.world, posX, posY, posZ, new ItemStack(CombustFishItems.cooledCod, amount, 0)));
            }

            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
        } else if (entityItem.getEntityWorld().getBlockState(entityItem.getPosition()).getBlock() == Blocks.CAULDRON) {
            IBlockState state = entityItem.getEntityWorld().getBlockState(entityItem.getPosition());
            int level = state.getValue(BlockCauldron.LEVEL);
            if (level > 0) {
                World world = entityItem.world;
                double posX = entityItem.posX;
                double posY = entityItem.posY;
                double posZ = entityItem.posZ;

                if (!entityItem.world.isRemote) {
                    int amount = entityItem.getItem().getCount();
                    entityItem.setDead();
                    world.spawnEntity(new EntityItem(entityItem.world, posX, posY, posZ, new ItemStack(CombustFishItems.cooledCod, amount, 0)));
                }

                Blocks.CAULDRON.setWaterLevel(entityItem.getEntityWorld(), entityItem.getPosition(), state, level - 1);
                world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            }
        }

        return false;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.capabilities.isCreativeMode)
        {
            itemstack.shrink(1);
        }

        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote)
        {
            EntityThrownCombustiveCod conflagrantCod = new EntityThrownCombustiveCod(worldIn, playerIn);
            conflagrantCod.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(conflagrantCod);
        }

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}
