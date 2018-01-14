package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentFireAspect;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemSearingSwordfish extends ItemSword {

    public ItemSearingSwordfish() {
        super(CombustFishItems.fishMaterial);
        this.setRegistryName("searing_swordfish");
        this.setUnlocalizedName(CombustiveFishing.MODID + ".searing_swordfish");
        this.setNoRepair();
    }

    @Override
    public boolean isEnchantable(ItemStack stack) { return false; }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book)
    {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(1, attacker);
        target.setFire(4);
        return true;
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
                int damage = entityItem.getItem().getItemDamage();
                entityItem.setDead();
                ItemStack stack = new ItemStack(CombustFishItems.temperedSwordfish, 1, damage);
                world.spawnEntity(new EntityItem(entityItem.world, posX, posY, posZ, stack));
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
                    int damage = entityItem.getItem().getItemDamage();
                    entityItem.setDead();
                    ItemStack stack = new ItemStack(CombustFishItems.temperedSwordfish, 1, damage);
                    world.spawnEntity(new EntityItem(entityItem.world, posX, posY, posZ, stack));
                }

                Blocks.CAULDRON.setWaterLevel(entityItem.getEntityWorld(), entityItem.getPosition(), state, level - 1);
                world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            }
        }

        return false;
    }
}
