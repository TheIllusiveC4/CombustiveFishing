package c4.combustfish.common.items;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.util.EntityAccessor;
import c4.combustfish.common.util.init.CombustFishItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;

import java.util.Random;

public class ItemBoneFish extends Item {

    private static Random rand = new Random();

    public ItemBoneFish() {
        this.setRegistryName("bone_fish");
        this.setUnlocalizedName(CombustiveFishing.MODID + ".bone_fish");
        this.setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {

        if (target instanceof EntityTameable && !((EntityTameable) target).isTamed()) {

            if (target instanceof EntityOcelot) {

                EntityOcelot ocelot = (EntityOcelot) target;
                EntityAITempt aiTempt = null;
                try {
                    aiTempt = EntityAccessor.getAITempt(ocelot);
                } catch (Exception e) {
                    CombustiveFishing.logger.log(Level.ERROR, "Failed to access field aiTempt");
                }

                if ((aiTempt == null || aiTempt.isRunning()) && player.getDistanceSqToEntity(ocelot) < 9.0D) {

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    if (!ocelot.world.isRemote) {
                        if (rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(ocelot, player)) {
                            ocelot.setTamedBy(player);
                            ocelot.setTameSkin(1 + ocelot.world.rand.nextInt(3));
                            try {
                                EntityAccessor.playTameEffect(ocelot, true);
                            } catch (Exception e) {
                                CombustiveFishing.logger.log(Level.ERROR, "Failed to invoke playTameEffect");
                            }
                            ocelot.getAISit().setSitting(true);
                            ocelot.world.setEntityState(ocelot, (byte) 7);
                        } else {
                            try {
                                EntityAccessor.playTameEffect(ocelot, false);
                            } catch (Exception e) {
                                CombustiveFishing.logger.log(Level.ERROR, "Failed to invoke playTameEffect");
                            }
                            ocelot.world.setEntityState(ocelot, (byte) 6);
                        }
                    }
                }

                return true;

            } else if (target instanceof EntityWolf) {

                EntityWolf wolf = (EntityWolf) target;

                if (!wolf.isAngry()) {
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    if (!wolf.world.isRemote) {
                        if (rand.nextInt(3) == 0 && !net.minecraftforge.event.ForgeEventFactory.onAnimalTame(wolf, player)) {
                            wolf.setTamedBy(player);
                            wolf.getNavigator().clearPathEntity();
                            wolf.setAttackTarget(null);
                            wolf.setHealth(20.0F);
                            try {
                                EntityAccessor.playTameEffect(wolf, true);
                            } catch (Exception e) {
                                CombustiveFishing.logger.log(Level.ERROR, "Failed to invoke playTameEffect");
                            }
                            wolf.getAISit().setSitting(true);
                            wolf.world.setEntityState(wolf, (byte) 7);
                        } else {
                            try {
                                EntityAccessor.playTameEffect(wolf, false);
                            } catch (Exception e) {
                                CombustiveFishing.logger.log(Level.ERROR, "Failed to invoke playTameEffect");
                            }
                            wolf.world.setEntityState(wolf, (byte) 6);
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
