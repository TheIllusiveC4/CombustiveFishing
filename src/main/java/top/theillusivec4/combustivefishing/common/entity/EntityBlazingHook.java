/*
 * Copyright (C) 2017-2019  C4
 *
 * This file is part of Combustive Fishing, a mod made for Minecraft.
 *
 * Combustive Fishing is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Combustive Fishing is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Combustive Fishing.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Particles;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.stats.StatList;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;
import top.theillusivec4.combustivefishing.common.item.ItemBlazingFishingRod;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class EntityBlazingHook extends EntityFishHook implements IEntityAdditionalSpawnData {

    private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.createKey(EntityBlazingHook.class, DataSerializers.VARINT);
    private EntityBlazingHook.State currentState = EntityBlazingHook.State.FLYING;
    private boolean inGround;
    private int ticksInGround;
    private int ticksInAir;
    private int ticksCatchable;
    private int ticksCaughtDelay;
    private int ticksCatchableDelay;
    private float fishApproachAngle;
    private int lureSpeed;
    private int luck;
    private EntityPlayer angler;

    public EntityBlazingHook(World worldIn) {
        super(worldIn, Minecraft.getInstance().player, 0, 0, 0);
        this.isImmuneToFire = true;
    }

    public EntityBlazingHook(World worldIn, EntityPlayer fishingPlayer) {
        super(worldIn, fishingPlayer);
        this.angler = fishingPlayer;
        this.isImmuneToFire = true;
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(this.angler.getEntityId());
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.angler = (EntityPlayer)Minecraft.getInstance().world.getEntityByID(additionalData.readInt());
    }

    @Nonnull
    @Override
    public EntityType<?> getType() {
        return CombustiveFishingEntities.BLAZING_BOBBER;
    }

    @Override
    protected void registerData() {
        this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
    }

    @Override
    public void notifyDataManagerChange(@Nonnull DataParameter<?> key) {

        if (DATA_HOOKED_ENTITY.equals(key)) {
            int i = this.getDataManager().get(DATA_HOOKED_ENTITY);
            this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
        }
        super.notifyDataManagerChange(key);
    }

    @Override
    public void tick() {

        if (!this.world.isRemote) {
            this.setFlag(6, this.isGlowing());
        }
        this.baseTick();

        if (this.angler == null) {
            this.remove();
        } else if (this.world.isRemote || !this.shouldStopFishing()) {

            if (this.inGround) {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200) {
                    this.remove();
                    return;
                }
            }
            float f = 0.0F;
            BlockPos blockpos = new BlockPos(this);
            IFluidState ifluidstate = this.world.getFluidState(blockpos);
            boolean isLava = ifluidstate.isTagged(FluidTags.LAVA);

            if (isLava || ifluidstate.isTagged(FluidTags.WATER)) {
                f = ifluidstate.getHeight();
            }

            if (this.currentState == EntityBlazingHook.State.FLYING) {

                if (this.caughtEntity != null) {
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                    this.currentState = EntityBlazingHook.State.HOOKED_IN_ENTITY;
                    return;
                }

                if (f > 0.0F) {
                    this.motionX *= 0.3D;
                    this.motionY *= 0.2D;
                    this.motionZ *= 0.3D;
                    this.currentState = EntityBlazingHook.State.BOBBING;
                    return;
                }

                if (!this.world.isRemote) {
                    this.checkCollision();
                }

                if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
                    ++this.ticksInAir;
                } else {
                    this.ticksInAir = 0;
                    this.motionX = 0.0D;
                    this.motionY = 0.0D;
                    this.motionZ = 0.0D;
                }
            } else {

                if (this.currentState == EntityBlazingHook.State.HOOKED_IN_ENTITY) {

                    if (this.caughtEntity != null) {

                        if (!this.caughtEntity.isAlive()) {
                            this.caughtEntity = null;
                            this.currentState = EntityBlazingHook.State.FLYING;
                        } else {
                            this.posX = this.caughtEntity.posX;
                            double d2 = (double)this.caughtEntity.height;
                            this.posY = this.caughtEntity.getBoundingBox().minY + d2 * 0.8D;
                            this.posZ = this.caughtEntity.posZ;
                            this.setPosition(this.posX, this.posY, this.posZ);
                        }
                    }
                    return;
                }

                if (this.currentState == EntityBlazingHook.State.BOBBING) {
                    this.motionX *= 0.9D;
                    this.motionZ *= 0.9D;
                    double d0 = this.posY + this.motionY - (double)blockpos.getY() - (double)f;

                    if (Math.abs(d0) < 0.01D) {
                        d0 += Math.signum(d0) * 0.1D;
                    }
                    this.motionY -= d0 * (double)this.rand.nextFloat() * 0.2D;

                    if (!this.world.isRemote && f > 0.0F) {
                        this.catchingFish(blockpos);
                    }
                }
            }

            if (!ifluidstate.isTagged(FluidTags.LAVA) && !ifluidstate.isTagged(FluidTags.WATER)) {
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

    @Override
    public void setLureSpeed(int p_191516_1_) {
        super.setLureSpeed(p_191516_1_);
        this.lureSpeed = p_191516_1_;
    }

    @Override
    public void setLuck(int p_191517_1_) {
        super.setLuck(p_191517_1_);
        this.luck = p_191517_1_;
    }

    private boolean shouldStopFishing() {
        ItemStack itemstack = this.angler.getHeldItemMainhand();
        ItemStack itemstack1 = this.angler.getHeldItemOffhand();
        boolean flag = itemstack.getItem() instanceof ItemBlazingFishingRod;
        boolean flag1 = itemstack1.getItem() instanceof ItemBlazingFishingRod;

        if (this.angler.isAlive() && (flag || flag1) && !(this.getDistanceSq(this.angler) > 1024.0D)) {
            return false;
        } else {
            this.remove();
            return true;
        }
    }

    private void updateRotation() {
        float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * (double)(180F / (float)Math.PI));

        for(this.rotationPitch = (float)(MathHelper.atan2(this.motionY, (double)f) * (double)(180F / (float)Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while(this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while(this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
    }

    private void checkCollision() {
        Vec3d vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(vec3d, vec3d1, RayTraceFluidMode.NEVER, true, false);
        vec3d = new Vec3d(this.posX, this.posY, this.posZ);
        vec3d1 = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        if (raytraceresult != null) {
            vec3d1 = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }

        Entity entity = null;
        List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getBoundingBox().expand(this.motionX, this.motionY, this.motionZ).grow(1.0D));
        double d0 = 0.0D;

        for(Entity entity1 : list) {

            if (this.canBeHooked(entity1) && (entity1 != this.angler || this.ticksInAir >= 5)) {
                AxisAlignedBB axisalignedbb = entity1.getBoundingBox().grow((double)0.3F);
                RayTraceResult raytraceresult1 = axisalignedbb.calculateIntercept(vec3d, vec3d1);

                if (raytraceresult1 != null) {
                    double d1 = vec3d.squareDistanceTo(raytraceresult1.hitVec);
                    if (d1 < d0 || d0 == 0.0D) {
                        entity = entity1;
                        d0 = d1;
                    }
                }
            }
        }

        if (entity != null) {
            raytraceresult = new RayTraceResult(entity);
        }

        if (raytraceresult != null && raytraceresult.type != RayTraceResult.Type.MISS) {

            if (raytraceresult.type == RayTraceResult.Type.ENTITY) {
                this.caughtEntity = raytraceresult.entity;
                this.setHookedEntity();
            } else {
                this.inGround = true;
            }
        }
    }

    private void setHookedEntity() {
        this.getDataManager().set(DATA_HOOKED_ENTITY, this.caughtEntity.getEntityId() + 1);
    }

    private void catchingFish(BlockPos blockPos) {
        WorldServer worldserver = (WorldServer)this.world;
        int i = 1;
        BlockPos blockpos = blockPos.up();

        if (this.rand.nextFloat() < 0.5F && this.world.isRainingAt(blockpos)) {
            i--;
        }

        if (this.rand.nextFloat() < 0.25F && !this.world.canSeeSky(blockpos)) {
            i++;
        }

        if (this.ticksCatchable > 0) {
            this.ticksCatchable--;

            if (this.ticksCatchable <= 0) {
                this.ticksCaughtDelay = 0;
                this.ticksCatchableDelay = 0;
            } else {
                this.motionY -= 0.2D * (double)this.rand.nextFloat() * (double)this.rand.nextFloat();
            }
        } else if (this.ticksCatchableDelay > 0) {
            this.ticksCatchableDelay -= i;

            if (this.ticksCatchableDelay > 0) {
                this.fishApproachAngle = (float)((double)this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
                float f = this.fishApproachAngle * ((float)Math.PI / 180F);
                float f1 = MathHelper.sin(f);
                float f2 = MathHelper.cos(f);
                double d0 = this.posX + (double)(f1 * (float)this.ticksCatchableDelay * 0.1F);
                double d1 = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                double d2 = this.posZ + (double)(f2 * (float)this.ticksCatchableDelay * 0.1F);
                IBlockState state = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2));

                if (state.getMaterial() == Material.WATER) {

                    if (this.rand.nextFloat() < 0.15F) {
                        worldserver.spawnParticle(Particles.BUBBLE, d0, d1 - (double)0.1F, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.spawnParticle(Particles.FISHING, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.spawnParticle(Particles.FISHING, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                } else if (state.getMaterial() == Material.LAVA) {

                    if (this.rand.nextFloat() < 0.15F) {
                        worldserver.spawnParticle(Particles.FLAME, d0, d1 - (double)0.1F, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
                    }

                    float f3 = f1 * 0.04F;
                    float f4 = f2 * 0.04F;
                    worldserver.spawnParticle(Particles.SMOKE, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
                    worldserver.spawnParticle(Particles.SMOKE, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
                }
            } else {
                double d3 = this.getBoundingBox().minY + 0.5D;
                double d1 = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                IBlockState state = worldserver.getBlockState(new BlockPos(this.posX, d1 - 1.0D, this.posZ));

                if (state.getMaterial() == Material.WATER) {
                    this.motionY = (double) (-0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                    this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
                    worldserver.spawnParticle(Particles.BUBBLE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, (double) 0.2F);
                    worldserver.spawnParticle(Particles.FISHING, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, (double) 0.2F);
                } else if (state.getMaterial() == Material.LAVA) {
                    this.motionY = (double) (-0.1F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F));
                    this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F, 0.4F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
                    worldserver.spawnParticle(Particles.FLAME, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, (double) 0.2F);
                    worldserver.spawnParticle(Particles.SMOKE, this.posX, d3, this.posZ, (int) (1.0F + this.width * 20.0F), (double) this.width, 0.0D, (double) this.width, (double) 0.2F);
                }
                this.ticksCatchable = MathHelper.nextInt(this.rand, 20, 40);
            }
        } else if (this.ticksCaughtDelay > 0) {
            this.ticksCaughtDelay -= i;
            float f5 = 0.15F;
            if (this.ticksCaughtDelay < 20) {
                f5 = (float)((double)f5 + (double)(20 - this.ticksCaughtDelay) * 0.05D);
            } else if (this.ticksCaughtDelay < 40) {
                f5 = (float)((double)f5 + (double)(40 - this.ticksCaughtDelay) * 0.02D);
            } else if (this.ticksCaughtDelay < 60) {
                f5 = (float)((double)f5 + (double)(60 - this.ticksCaughtDelay) * 0.01D);
            }

            if (this.rand.nextFloat() < f5) {
                float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * ((float)Math.PI / 180F);
                float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
                double d4 = this.posX + (double)(MathHelper.sin(f6) * f7 * 0.1F);
                double d5 = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                double d6 = this.posZ + (double)(MathHelper.cos(f6) * f7 * 0.1F);
                IBlockState state = worldserver.getBlockState(new BlockPos((int)d4, (int)d5 - 1, (int)d6));

                if (state.getMaterial() == Material.WATER) {
                    worldserver.spawnParticle(Particles.SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), (double)0.1F, 0.0D, (double)0.1F, 0.0D);
                } else if (state.getMaterial() == Material.LAVA) {
                    worldserver.spawnParticle(Particles.LARGE_SMOKE, d4, d5, d6, 2 + this.rand.nextInt(2), (double)0.1F, 0.0D, (double)0.1F, 0.0D);
                }
            }

            if (this.ticksCaughtDelay <= 0) {
                this.fishApproachAngle = MathHelper.nextFloat(this.rand, 0.0F, 360.0F);
                this.ticksCatchableDelay = MathHelper.nextInt(this.rand, 20, 80);
            }
        } else {
            this.ticksCaughtDelay = MathHelper.nextInt(this.rand, 100, 600);
            this.ticksCaughtDelay -= this.lureSpeed * 20 * 5;
        }
    }

    @Override
    public int handleHookRetraction(@Nonnull ItemStack itemStack) {

        if (!this.world.isRemote && this.angler != null) {
            int i = 0;
            net.minecraftforge.event.entity.player.ItemFishedEvent event = null;

            if (this.caughtEntity != null) {
                this.bringInHookedEntity();
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((EntityPlayerMP)this.angler, itemStack, this, Collections.emptyList());
                this.world.setEntityState(this, (byte)31);
                i = this.caughtEntity instanceof EntityItem ? 3 : 5;
            } else if (this.ticksCatchable > 0) {
                BlockPos pos = new BlockPos(this);
                LootContext.Builder lootcontext$builder = (new LootContext.Builder((WorldServer)this.world)).withPosition(new BlockPos(this));
                lootcontext$builder.withLuck((float)this.luck + this.angler.getLuck());
                lootcontext$builder.withPlayer(this.angler).withLootedEntity(this);
                double d = (double)((float)MathHelper.floor(this.getBoundingBox().minY) + 1.0F);
                IBlockState state = this.world.getBlockState(new BlockPos(this.posX, d - 1.0D, this.posZ));
                ResourceLocation loottable;

                if (state.getMaterial() == Material.LAVA) {

                    if (this.world.getDimension().isNether()) {
                        loottable = CombustiveFishingLoot.NETHER_FISHING;
                    } else {
                        loottable = CombustiveFishingLoot.LAVA_FISHING;
                    }
                } else {
                    loottable = LootTableList.GAMEPLAY_FISHING;
                }
                List<ItemStack> list = this.world.getServer().getLootTableManager().getLootTableFromLocation(loottable).generateLootForPools(this.rand, lootcontext$builder.build());
                event = new net.minecraftforge.event.entity.player.ItemFishedEvent(list, this.inGround ? 2 : 1, this);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

                if (event.isCanceled()) {
                    this.remove();
                    return event.getRodDamage();
                }
                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((EntityPlayerMP)this.angler, itemStack, this, list);

                for(ItemStack itemstack : list) {
                    EntityItem entityitem = new EntityItem(this.world, this.posX, this.posY + 1.0D, this.posZ, itemstack);
                    double d0 = this.angler.posX - this.posX;
                    double d1 = this.angler.posY - this.posY;
                    double d2 = this.angler.posZ - this.posZ;
                    double d3 = (double)MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                    double d4 = 0.1D;

                    if (isInLava()) {
                        d4 *= 2;
                        ObfuscationReflectionHelper.setPrivateValue(Entity.class, entityitem, true, "field_70178_ae");
                    }
                    entityitem.motionX = d0 * d4;
                    entityitem.motionY = d1 * d4 + (double)MathHelper.sqrt(d3) * 0.08D;
                    entityitem.motionZ = d2 * d4;
                    this.world.spawnEntity(entityitem);
                    this.angler.world.spawnEntity(new EntityXPOrb(this.angler.world, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));

                    if (itemstack.getItem().isIn(ItemTags.FISHES)) {
                        this.angler.addStat(StatList.FISH_CAUGHT, 1);
                    }
                }
                i = 1;
            }

            if (this.inGround) {
                i = 2;
            }
            this.remove();
            return event == null ? i : event.getRodDamage();
        } else {
            return 0;
        }
    }

    enum State
    {
        FLYING,
        HOOKED_IN_ENTITY,
        BOBBING;
    }
}
