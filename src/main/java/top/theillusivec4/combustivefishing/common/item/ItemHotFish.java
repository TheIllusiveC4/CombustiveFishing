package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingItems;

import javax.annotation.Nullable;

public class ItemHotFish extends Item {

    private final FishType fishType;

    public ItemHotFish(FishType fishType, Item.Properties properties) {
        super(properties);
        this.fishType = fishType;
    }

    @Override
    public boolean onEntityItemUpdate(ItemStack stack, EntityItem entity) {
        World world = entity.world;
        double posX = entity.posX;
        double posY = entity.posY;
        double posZ = entity.posZ;
        BlockPos blockpos = entity.getPosition();
        IBlockState state = world.getBlockState(blockpos);

        if (entity.isInWater()) {

            if (!world.isRemote) {

                world.spawnEntity(new EntityItem(world, posX, posY, posZ, new ItemStack(fishType.getCooledItem(), entity.getItem().getCount())));
                entity.remove();
            }
            world.playSound(null, posX, posY, posZ, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.NEUTRAL, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
            return true;
        } else if (state.getBlock() == Blocks.CAULDRON) {
            int level = state.get(BlockCauldron.LEVEL);

            if (level > 0) {
                world.spawnEntity(new EntityItem(world, posX, posY, posZ, new ItemStack(fishType.getCooledItem(), entity.getItem().getCount())));
                entity.remove();
            }
            world.setBlockState(blockpos, state.with(BlockCauldron.LEVEL, MathHelper.clamp(level, 0, 3)), 2);
            world.updateComparatorOutputLevel(blockpos, state.getBlock());
            return true;
        }
        return false;
    }

    public enum FishType {
        COMBUSTIVE_COD,
        SEARING_SWORDFISH;

        @Nullable
        public Item getCooledItem() {
            switch (this) {
                case COMBUSTIVE_COD: return CombustiveFishingItems.COOLED_COD;
                case SEARING_SWORDFISH: return CombustiveFishingItems.COOLED_BILL;
            }
            return null;
        }
    }
}
