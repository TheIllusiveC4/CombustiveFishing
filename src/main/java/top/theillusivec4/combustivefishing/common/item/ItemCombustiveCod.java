package top.theillusivec4.combustivefishing.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.entity.EntityThrownCombustiveCod;

import javax.annotation.Nonnull;

public class ItemCombustiveCod extends ItemHotFish {

    public ItemCombustiveCod() {
        super(FishType.COMBUSTIVE_COD, new Item.Properties().maxStackSize(16).group(ItemGroup.MISC));
        this.setRegistryName(CombustiveFishing.MODID, "combustive_cod");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        if (!playerIn.abilities.isCreativeMode) {
            itemstack.shrink(1);
        }
        worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_BLAZE_SHOOT, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));

        if (!worldIn.isRemote) {
            EntityThrownCombustiveCod throwncod = new EntityThrownCombustiveCod(worldIn, playerIn);
            throwncod.shoot(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
            worldIn.spawnEntity(throwncod);
        }
        playerIn.addStat(StatList.ITEM_USED.get(this));
        return new ActionResult<>(EnumActionResult.SUCCESS, itemstack);
    }
}
