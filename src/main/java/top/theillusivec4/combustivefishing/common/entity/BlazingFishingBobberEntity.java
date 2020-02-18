/*
 * Copyright (c) 2017-2020 C4
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

import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingLoot;
import top.theillusivec4.combustivefishing.common.item.BlazingFishingRodItem;

public class BlazingFishingBobberEntity extends FishingBobberEntity implements
    IEntityAdditionalSpawnData {

  private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager
      .createKey(BlazingFishingBobberEntity.class, DataSerializers.VARINT);
  private final int luck;
  private final int lureSpeed;
  private BlazingFishingBobberEntity.State currentState = BlazingFishingBobberEntity.State.FLYING;
  private boolean inGround;
  private int ticksInGround;
  private int ticksInAir;
  private int ticksCatchable;
  private int ticksCaughtDelay;
  private int ticksCatchableDelay;
  private float fishApproachAngle;
  private PlayerEntity angler;

  public BlazingFishingBobberEntity(World worldIn) {
    super(worldIn, Minecraft.getInstance().player, 0, 0, 0);
    this.luck = 0;
    this.lureSpeed = 0;
  }

  public BlazingFishingBobberEntity(PlayerEntity fishingPlayer, World worldIn, int luck,
      int lureSpeed) {
    super(fishingPlayer, worldIn, luck, lureSpeed);
    this.angler = fishingPlayer;
    this.luck = Math.max(0, luck);
    this.lureSpeed = Math.max(0, lureSpeed);
  }

  @Nonnull
  @Override
  public IPacket<?> createSpawnPacket() {
    return NetworkHooks.getEntitySpawningPacket(this);
  }

  @Nonnull
  @Override
  public EntityType<?> getType() {
    return CombustiveFishingEntities.BLAZING_BOBBER;
  }

  @Override
  public void writeSpawnData(PacketBuffer buffer) {
    buffer.writeInt(this.angler.getEntityId());
  }

  @Override
  public void readSpawnData(PacketBuffer additionalData) {
    this.angler = (PlayerEntity) Minecraft.getInstance().world
        .getEntityByID(additionalData.readInt());
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
        f = ifluidstate.getActualHeight(this.world, blockpos);
      }

      if (this.currentState == BlazingFishingBobberEntity.State.FLYING) {

        if (this.caughtEntity != null) {
          this.setMotion(Vec3d.ZERO);
          this.currentState = BlazingFishingBobberEntity.State.HOOKED_IN_ENTITY;
          return;
        }

        if (f > 0.0F) {
          this.setMotion(this.getMotion().mul(0.3D, 0.2D, 0.3D));
          this.currentState = BlazingFishingBobberEntity.State.BOBBING;
          return;
        }

        if (!this.world.isRemote) {
          this.checkCollision();
        }

        if (!this.inGround && !this.onGround && !this.collidedHorizontally) {
          ++this.ticksInAir;
        } else {
          this.ticksInAir = 0;
          this.setMotion(Vec3d.ZERO);
        }
      } else {

        if (this.currentState == BlazingFishingBobberEntity.State.HOOKED_IN_ENTITY) {

          if (this.caughtEntity != null) {

            if (!this.caughtEntity.isAlive()) {
              this.caughtEntity = null;
              this.currentState = BlazingFishingBobberEntity.State.FLYING;
            } else {
              this.posX = this.caughtEntity.posX;
              double d2 = this.caughtEntity.getHeight();
              this.posY = this.caughtEntity.getBoundingBox().minY + d2 * 0.8D;
              this.posZ = this.caughtEntity.posZ;
              this.setPosition(this.posX, this.posY, this.posZ);
            }
          }
          return;
        }

        if (this.currentState == BlazingFishingBobberEntity.State.BOBBING) {
          Vec3d vec3d = this.getMotion();
          double d0 = this.posY + vec3d.y - (double) blockpos.getY() - (double) f;

          if (Math.abs(d0) < 0.01D) {
            d0 += Math.signum(d0) * 0.1D;
          }
          this.setMotion(vec3d.x * 0.9D, vec3d.y - d0 * (double) this.rand.nextFloat() * 0.2D,
              vec3d.z * 0.9D);

          if (!this.world.isRemote && f > 0.0F) {
            this.catchingFish(blockpos);
          }
        }
      }

      if (!ifluidstate.isTagged(FluidTags.LAVA) && !ifluidstate.isTagged(FluidTags.WATER)) {
        this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
      }
      this.move(MoverType.SELF, this.getMotion());
      this.updateRotation();
      double d1 = 0.92D;
      this.setMotion(this.getMotion().scale(d1));
      this.setPosition(this.posX, this.posY, this.posZ);
    }
  }

  private boolean shouldStopFishing() {
    ItemStack itemstack = this.angler.getHeldItemMainhand();
    ItemStack itemstack1 = this.angler.getHeldItemOffhand();
    boolean flag = itemstack.getItem() instanceof BlazingFishingRodItem;
    boolean flag1 = itemstack1.getItem() instanceof BlazingFishingRodItem;

    if (this.angler.isAlive() && (flag || flag1) && !(this.getDistanceSq(this.angler) > 1024.0D)) {
      return false;
    } else {
      this.remove();
      return true;
    }
  }

  private void updateRotation() {
    Vec3d vec3d = this.getMotion();
    float f = MathHelper.sqrt(horizontalMag(vec3d));
    this.rotationYaw = (float) (MathHelper.atan2(vec3d.x, vec3d.z) * (double) (180F
        / (float) Math.PI));

    for (this.rotationPitch = (float) (MathHelper.atan2(vec3d.y, (double) f) * (double) (180F
        / (float) Math.PI)); this.rotationPitch - this.prevRotationPitch < -180.0F;
        this.prevRotationPitch -= 360.0F) {
      ;
    }

    while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
      this.prevRotationPitch += 360.0F;
    }

    while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
      this.prevRotationYaw -= 360.0F;
    }

    while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
      this.prevRotationYaw += 360.0F;
    }
    this.rotationPitch = MathHelper.lerp(0.2F, this.prevRotationPitch, this.rotationPitch);
    this.rotationYaw = MathHelper.lerp(0.2F, this.prevRotationYaw, this.rotationYaw);
  }

  private void checkCollision() {
    RayTraceResult raytraceresult = ProjectileHelper
        .rayTrace(this, this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
            (p_213856_1_) -> !p_213856_1_.isSpectator() && (p_213856_1_.canBeCollidedWith()
                || p_213856_1_ instanceof ItemEntity) && (p_213856_1_ != this.angler
                || this.ticksInAir >= 5), RayTraceContext.BlockMode.COLLIDER, true);

    if (raytraceresult.getType() != RayTraceResult.Type.MISS) {

      if (raytraceresult.getType() == RayTraceResult.Type.ENTITY) {
        this.caughtEntity = ((EntityRayTraceResult) raytraceresult).getEntity();
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
    ServerWorld worldserver = (ServerWorld) this.world;
    int i = 1;
    BlockPos blockpos = blockPos.up();

    if (this.rand.nextFloat() < 0.5F && this.world.isRainingAt(blockpos)) {
      i--;
    }

    if (this.rand.nextFloat() < 0.25F && !this.world.isSkyLightMax(blockpos)) {
      i++;
    }

    if (this.ticksCatchable > 0) {
      this.ticksCatchable--;

      if (this.ticksCatchable <= 0) {
        this.ticksCaughtDelay = 0;
        this.ticksCatchableDelay = 0;
      } else {
        this.setMotion(this.getMotion()
            .add(0.0D, -0.2D * (double) this.rand.nextFloat() * (double) this.rand.nextFloat(),
                0.0D));
      }
    } else if (this.ticksCatchableDelay > 0) {
      this.ticksCatchableDelay -= i;

      if (this.ticksCatchableDelay > 0) {
        this.fishApproachAngle = (float) ((double) this.fishApproachAngle
            + this.rand.nextGaussian() * 4.0D);
        float f = this.fishApproachAngle * ((float) Math.PI / 180F);
        float f1 = MathHelper.sin(f);
        float f2 = MathHelper.cos(f);
        double d0 = this.posX + (double) (f1 * (float) this.ticksCatchableDelay * 0.1F);
        double d1 = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        double d2 = this.posZ + (double) (f2 * (float) this.ticksCatchableDelay * 0.1F);
        BlockState state = worldserver.getBlockState(new BlockPos(d0, d1 - 1.0D, d2));

        if (state.getMaterial() == Material.WATER) {

          if (this.rand.nextFloat() < 0.15F) {
            worldserver
                .spawnParticle(ParticleTypes.BUBBLE, d0, d1 - (double) 0.1F, d2, 1, f1, 0.1D, f2,
                    0.0D);
          }

          float f3 = f1 * 0.04F;
          float f4 = f2 * 0.04F;
          worldserver.spawnParticle(ParticleTypes.FISHING, d0, d1, d2, 0, f4, 0.01D, -f3, 1.0D);
          worldserver.spawnParticle(ParticleTypes.FISHING, d0, d1, d2, 0, -f4, 0.01D, f3, 1.0D);
        } else if (state.getMaterial() == Material.LAVA) {

          if (this.rand.nextFloat() < 0.15F) {
            worldserver
                .spawnParticle(ParticleTypes.FLAME, d0, d1 - (double) 0.1F, d2, 1, f1, 0.1D, f2,
                    0.0D);
          }

          float f3 = f1 * 0.04F;
          float f4 = f2 * 0.04F;
          worldserver.spawnParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, f4, 0.01D, -f3, 1.0D);
          worldserver.spawnParticle(ParticleTypes.SMOKE, d0, d1, d2, 0, -f4, 0.01D, f3, 1.0D);
        }
      } else {
        double d3 = this.getBoundingBox().minY + 0.5D;
        double d1 = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        BlockState state = worldserver.getBlockState(new BlockPos(this.posX, d1 - 1.0D, this.posZ));
        Vec3d vec3d = this.getMotion();

        if (state.getMaterial() == Material.WATER) {
          this.setMotion(vec3d.x, -0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F), vec3d.z);
          this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
              1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
          worldserver.spawnParticle(ParticleTypes.BUBBLE, this.posX, d3, this.posZ,
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
          worldserver.spawnParticle(ParticleTypes.FISHING, this.posX, d3, this.posZ,
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
        } else if (state.getMaterial() == Material.LAVA) {
          this.setMotion(vec3d.x, -0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F), vec3d.z);
          this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
              0.4F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
          worldserver.spawnParticle(ParticleTypes.FLAME, this.posX, d3, this.posZ,
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
          worldserver.spawnParticle(ParticleTypes.SMOKE, this.posX, d3, this.posZ,
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
        }
        this.ticksCatchable = MathHelper.nextInt(this.rand, 20, 40);
      }
    } else if (this.ticksCaughtDelay > 0) {
      this.ticksCaughtDelay -= i;
      float f5 = 0.15F;
      if (this.ticksCaughtDelay < 20) {
        f5 = (float) ((double) f5 + (double) (20 - this.ticksCaughtDelay) * 0.05D);
      } else if (this.ticksCaughtDelay < 40) {
        f5 = (float) ((double) f5 + (double) (40 - this.ticksCaughtDelay) * 0.02D);
      } else if (this.ticksCaughtDelay < 60) {
        f5 = (float) ((double) f5 + (double) (60 - this.ticksCaughtDelay) * 0.01D);
      }

      if (this.rand.nextFloat() < f5) {
        float f6 = MathHelper.nextFloat(this.rand, 0.0F, 360.0F) * ((float) Math.PI / 180F);
        float f7 = MathHelper.nextFloat(this.rand, 25.0F, 60.0F);
        double d4 = this.posX + (double) (MathHelper.sin(f6) * f7 * 0.1F);
        double d5 = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        double d6 = this.posZ + (double) (MathHelper.cos(f6) * f7 * 0.1F);
        BlockState state = worldserver
            .getBlockState(new BlockPos((int) d4, (int) d5 - 1, (int) d6));

        if (state.getMaterial() == Material.WATER) {
          worldserver
              .spawnParticle(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.rand.nextInt(2), 0.1F, 0.0D,
                  0.1F, 0.0D);
        } else if (state.getMaterial() == Material.LAVA) {
          worldserver
              .spawnParticle(ParticleTypes.LARGE_SMOKE, d4, d5, d6, 2 + this.rand.nextInt(2), 0.1F,
                  0.0D, 0.1F, 0.0D);
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
        CriteriaTriggers.FISHING_ROD_HOOKED
            .trigger((ServerPlayerEntity) this.angler, itemStack, this, Collections.emptyList());
        this.world.setEntityState(this, (byte) 31);
        i = this.caughtEntity instanceof ItemEntity ? 3 : 5;
      } else if (this.ticksCatchable > 0) {
        BlockPos pos = new BlockPos(this);
        LootContext.Builder lootcontext$builder = (new LootContext.Builder(
            (ServerWorld) this.world)).withParameter(LootParameters.POSITION, new BlockPos(this))
            .withParameter(LootParameters.TOOL, itemStack).withRandom(this.rand)
            .withLuck((float) this.luck + this.angler.getLuck());
        ;
        lootcontext$builder.withParameter(LootParameters.KILLER_ENTITY, this.angler)
            .withParameter(LootParameters.THIS_ENTITY, this);
        double d = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        BlockState state = this.world.getBlockState(new BlockPos(this.posX, d - 1.0D, this.posZ));
        ResourceLocation loottable;

        if (state.getMaterial() == Material.LAVA) {

          if (this.world.getDimension().isNether()) {
            loottable = CombustiveFishingLoot.NETHER_FISHING;
          } else {
            loottable = CombustiveFishingLoot.LAVA_FISHING;
          }
        } else {
          loottable = LootTables.GAMEPLAY_FISHING;
        }
        MinecraftServer server = this.world.getServer();

        if (server != null) {
          LootTable loot = this.world.getServer().getLootTableManager()
              .getLootTableFromLocation(loottable);
          List<ItemStack> list = loot
              .generate(lootcontext$builder.build(LootParameterSets.FISHING));
          event = new net.minecraftforge.event.entity.player.ItemFishedEvent(list,
              this.inGround ? 2 : 1, this);
          net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

          if (event.isCanceled()) {
            this.remove();
            return event.getRodDamage();
          }
          CriteriaTriggers.FISHING_ROD_HOOKED
              .trigger((ServerPlayerEntity) this.angler, itemStack, this, list);

          for (ItemStack itemstack : list) {
            ItemEntity itementity = new ItemEntity(this.world, this.posX, this.posY + 2.0D,
                this.posZ, itemstack);
            double d0 = this.angler.posX - this.posX;
            double d1 = this.angler.posY - this.posY;
            double d2 = this.angler.posZ - this.posZ;
            double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            double d4 = 0.1D;
            itementity.setMotion(d0 * d4,
                d1 * d4 + Math.sqrt(d3) * 0.08D, d2 * d4);
            this.world.addEntity(itementity);
            this.angler.world.addEntity(new ExperienceOrbEntity(this.angler.world, this.angler.posX,
                this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));

            if (itemstack.getItem().isIn(ItemTags.FISHES)) {
              this.angler.addStat(Stats.FISH_CAUGHT, 1);
            }
          }
          i = 1;
        }
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

  enum State {
    FLYING, HOOKED_IN_ENTITY, BOBBING;
  }
}
