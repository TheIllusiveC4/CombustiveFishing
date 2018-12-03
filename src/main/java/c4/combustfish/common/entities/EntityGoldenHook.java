/*
 * Copyright (c) 2017. C4, MIT License.
 */

package c4.combustfish.common.entities;

import c4.combustfish.CombustiveFishing;
import c4.combustfish.common.util.EntityHelper;
import c4.combustfish.common.util.init.CombustFishItems;
import c4.combustfish.common.util.EntityAccessor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import org.apache.logging.log4j.Level;

import java.util.List;

public class EntityGoldenHook extends EntityFishHook {

    private EntityGoldenHook.State currentState = EntityGoldenHook.State.FLYING;
    private boolean inGround;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    private int lureSpeed;
    private int luck;

    public EntityGoldenHook(World worldIn) {
        super(worldIn, worldIn.isRemote ? EntityHelper.initClient() : null);
        this.isImmuneToFire = true;
    }

    public EntityGoldenHook(World worldIn, EntityPlayer fishingPlayer) {
        super(worldIn, fishingPlayer);
        this.isImmuneToFire = true;
    }

    @Override
    public void onUpdate() {

        if (!this.world.isRemote)
        {
            this.setFlag(6, this.isGlowing());
        }

        this.onEntityUpdate();

        if (this.getAngler() == null)
        {
            this.setDead();
        }
        else if (this.world.isRemote || !this.shouldStopFishing())
        {
            if (this.inGround)
            {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200)
                {
                    this.setDead();
                    return;
                }
            }

            float f = 0.0F;
            BlockPos blockpos = new BlockPos(this);
            IBlockState iblockstate = this.world.getBlockState(blockpos);

            if (iblockstate.getMaterial() == Material.WATER || iblockstate.getMaterial() == Material.LAVA)
            {
                f = BlockLiquid.getBlockLiquidHeight(iblockstate, this.world, blockpos);
            }

            if (this.currentState == EntityGoldenHook.State.FLYING)
            {
                if (this.caughtEntity != null)
                {
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                    this.currentState = EntityGoldenHook.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F)
                {
                    this.motionX *= 0.3D;
                    this.motionY *= 0.2D;
                    this.motionZ *= 0.3D;
                    this.currentState = EntityGoldenHook.State.BOBBING;
                    return;
                }

                if (!this.world.isRemote)
                {
                    try {
                        EntityAccessor.checkCollision(this);
                    } catch (Exception e) {
                        CombustiveFishing.logger.log(Level.ERROR, "Failed to invoke method checkCollision");
                    }
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally)
                {
                    ++this.ticksInAir;
                }
                else
                {
                    this.ticksInAir = 0;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
            }
            else
            {
                if (this.currentState == EntityGoldenHook.State.HOOKED_IN_ENTITY)
                {
                    if (this.caughtEntity != null)
                    {
                        if (this.caughtEntity.isDead)
                        {
                            this.caughtEntity = null;
                            this.currentState = EntityGoldenHook.State.FLYING;
                        }
                        else
                        {
                            this.posX = this.caughtEntity.posX;
                            double d2 = (double)this.caughtEntity.height;
                            this.posY = this.caughtEntity.getEntityBoundingBox().minY + d2 * 0.8D;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }

                    return;
                }

                if (this.currentState == EntityGoldenHook.State.BOBBING)
                {
                    this.motionX *= 0.9D;
                    this.motionZ *= 0.9D;
                    double d0 = this.posY + this.motionY - (double)blockpos.getY() - (double)f;

                    if (Math.abs(d0) < 0.01D)
                    {
                        d0 += Math.signum(d0) * 0.1D;
                    }

                    this.motionY -= d0 * (double)this.rand.nextFloat() * 0.2D;

                    if (!this.world.isRemote && f > 0.0F)
                    {
                        this.catchingFish(blockpos);
                    }
                }
            }

            if (iblockstate.getMaterial() != Material.WATER && iblockstate.getMaterial() != Material.LAVA)
            {
                this.motionY -= 0.03D;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.updateRotation();
            double d1 = 0.92D;
            this.motionX *= d1;
            this.motionY *= d1;
            this.motionZ *= d1;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    private void updateRotation()
    {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

        for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (180D / Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F)
        {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    }

    private void catchingFish(BlockPos p_190621_1_)
    {
        WorldServer worldserver = (WorldServer)this.world;
        int i = 1;
        BlockPos blockpos = p_190621_1_.up();

        if (this.rand.nextFloat() < 0.25F && this.world.isRainingAt(blockpos))
        {
            ++i;
        }

        if (this.rand.nextFloat() < 0.5F && !this.world.canSeeSky(blockpos) && this.world.provider.getDimension() == 0)
        {
            --i;
        }

        if (this.ticksCatchable > 0)
        {
            --this.ticksCatchable;

            if (this.ticksCatchable <= 0)
            {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            }
            else
            {
                this.motionY -= 0.2D * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        }
        else if (this.ticksCatchableDelay > 0)
        {
            this.ticksCatchableDelay -= i;

            if (this.ticksCatchableDelay > 0)
            {
                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                float f = this.fishApproachAngle * 0.017453292F;
                float f1 = MathHelper.sin(f);
                float f2 = MathHelper.cos(f);
                double d0 = this.posX + (double)(f1 * (float)this.ticksCatchableDelay * 0.1F);
                double d1 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                double d2 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);
                Block block = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2)).getBlock();

                if (block == Blocks.WATER || block == Blocks.FLOWING_WATER)
                {
                    if (this.rand.nextFloat() < 0.15F)
                    {
                        worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d0, d1 - 0.10000000149011612D, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                } else if (block == Blocks.LAVA || block == Blocks.LAVA) {
                    if (this.rand.nextFloat() < 0.15F) {
                        worldserver.spawnParticle(EnumParticleTypes.FLAME, d0, d1 - 0.10000000149011612D, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }
                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                }
            }
            else
            {
                double d3 = this.getEntityBoundingBox().minY + 0.5D;
                double d1 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                Block block0 = worldserver.getBlockState(new BlockPos(this.posX, d1 - 1.0D, this.posZ)).getBlock();

                if (block0 == Blocks.WATER || block0 == Blocks.FLOWING_WATER) {
                    this.motionY = (double)(-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                    this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                    worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                } else if (block0 == Blocks.LAVA || block0 == Blocks.FLOWING_LAVA) {
                    this.motionY = (double)(-0.1F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                    this.playSound(SoundEvents.ENTITY_BOBBER_SPLASH, 0.25F, 0.4F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    worldserver.spawnParticle(EnumParticleTypes.FLAME, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                    worldserver.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, 0.20000000298023224D);
                }
                this.ticksCatchable = MathHelper.getInt(this.rand, 20, 40);
            }
        }
        else if (this.ticksCaughtDelay > 0)
        {
            this.ticksCaughtDelay -= i;
            float f5 = 0.15F;

            if (this.ticksCaughtDelay < 20)
            {
                f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
            }
            else if (this.ticksCaughtDelay < 40)
            {
                f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
            }
            else if (this.ticksCaughtDelay < 60)
            {
                f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
            }

            if (this.rand.nextFloat() < f5)
            {
                float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * 0.017453292F;
                float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                double d4 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1F);
                double d5 = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                double d6 = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1F);
                Block block1 = worldserver.getBlockState(new BlockPos((int)d4, (int)d5 - 1, (int)d6)).getBlock();

                if (block1 == Blocks.WATER || block1 == Blocks.FLOWING_WATER)
                {
                    worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                } else if (block1 == Blocks.LAVA || block1 == Blocks.FLOWING_LAVA) {
                    worldserver.spawnParticle(EnumParticleTypes.SMOKE_LARGE, d4, d5, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D);
                }
            }

            if (this.ticksCaughtDelay <= 0)
            {
                this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                this.ticksCatchableDelay = MathHelper.getInt(this.rand, 20, 80);
            }
        }
        else
        {
            this.ticksCaughtDelay = MathHelper.getInt(this.rand, 100, 600);
            this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
        }
    }

    @Override
    public int handleHookRetraction()
    {
        if (!this.world.isRemote && this.getAngler() != null)
        {
            int i = 0;

            net.minecraftforge.event.entity.player.ItemFishedEvent event = null;
            if (this.caughtEntity != null)
            {
                this.bringInHookedEntity();
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            }
            else if (this.ticksCatchable > 0)
            {
                LootContext.Builder lootcontext$builder = new LootContext.Builder((WorldServer)this.world);
                lootcontext$builder.withLuck((float)this.luck + this.getAngler().getLuck()).withPlayer(this.getAngler())
                        .withLootedEntity(this);
                List<ItemStack> result;
                double d = (double)((float)MathHelper.floor(this.getEntityBoundingBox().minY) + 1.0F);
                Block block = this.world.getBlockState(new BlockPos(this.posX, d - 1.0D, this.posZ)).getBlock();
                ResourceLocation loottable;
                if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                    if (this.world.provider.getDimension() == -1) {
                        loottable = new ResourceLocation(CombustiveFishing.MODID, "nether_fishing");
                    } else {
                        loottable = new ResourceLocation(CombustiveFishing.MODID, "lava_fishing");
                    }
                } else {
                    loottable = LootTableList.GAMEPLAY_FISHING;
                }
                result = this.world.getLootTableManager().getLootTableFromLocation(loottable).generateLootForPools(this.rand, lootcontext$builder.build());
                event = new net.minecraftforge.event.entity.player.ItemFishedEvent(result, this.inGround ? 2 : 1, this);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
                if (event.isCanceled())
                {
                    this.setDead();
                    return event.getRodDamage();
                }

                for (ItemStack itemstack : result)
                {
                    Item item = itemstack.getItem();
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY, this.posZ, itemstack);
                    double d0 = this.getAngler().posX - this.posX;
                    double d1 = this.getAngler().posY - this.posY;
                    double d2 = this.getAngler().posZ - this.posZ;
                    double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;
                    if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
                        d4 = 0.2D;
                        entityitem.setPosition(entityitem.posX, entityitem.posY + 1.0D, entityitem.posZ);
                        entityitem.setEntityInvulnerable(true);
                        entityitem.lifespan = 200;
                    }
                    entityitem.motionX = d0 * d4;
                    entityitem.motionY = d1 * d4 + (double)MathHelper.sqrt(d3) * 0.08D;
                    entityitem.motionZ = d2 * d4;

                    this.world.spawnEntity(entityitem);
                    this.getAngler().world.spawnEntity(new EntityXPOrb(this.getAngler().world, this.getAngler().posX, this.getAngler().posY + 0.5D, this.getAngler().posZ + 0.5D, this.rand.nextInt(6) + 1));

                    if (item == Items.FISH || item == Items.COOKED_FISH || item == CombustFishItems.combustiveCod)
                    {
                        this.getAngler().addStat(StatList.FISH_CAUGHT, 1);
                    }
                }

                i = 1;
            }

            if (this.inGround)
            {
                i = 2;
            }

            this.setDead();
            return event == null ? i : event.getRodDamage();
        }
        else
        {
            return 0;
        }
    }

    private boolean shouldStopFishing()
    {
        ItemStack itemstack = this.getAngler().getHeldItemMainhand();
        ItemStack itemstack1 = this.getAngler().getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof net.minecraft.item.ItemFishingRod;
        boolean flag1 = itemstack1.getItem() instanceof net.minecraft.item.ItemFishingRod;

        if (!this.getAngler().isDead && this.getAngler().isEntityAlive() && (flag || flag1) && this.getDistanceSq(this.getAngler()) <= 1024.0D)
        {
            return false;
        }
        else
        {
            this.setDead();
            return true;
        }
    }

    enum State
    {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;
    }
}
