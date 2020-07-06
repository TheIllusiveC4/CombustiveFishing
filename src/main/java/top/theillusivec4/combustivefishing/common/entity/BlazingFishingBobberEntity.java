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

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.annotation.Nonnull;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.network.IPacket;
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
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.combustivefishing.CombustiveFishing;
import top.theillusivec4.combustivefishing.common.item.BlazingFishingRodItem;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingEntities;
import top.theillusivec4.combustivefishing.common.registry.CombustiveFishingLoot;

public class BlazingFishingBobberEntity extends FishingBobberEntity {

  private static final Field HAS_LEFT_OWNER = ObfuscationReflectionHelper
      .findField(ProjectileEntity.class, "field_234611_d_");
  private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager
      .createKey(BlazingFishingBobberEntity.class, DataSerializers.VARINT);
  private static final DataParameter<Boolean> DATA_CATCHING = EntityDataManager
      .createKey(BlazingFishingBobberEntity.class, DataSerializers.BOOLEAN);

  private final Random random = new Random();
  private final int luck;
  private final int lureSpeed;
  private BlazingFishingBobberEntity.State currentState = BlazingFishingBobberEntity.State.FLYING;
  private int ticksInGround;
  private int ticksCatchable;
  private int ticksCaughtDelay;
  private int ticksCatchableDelay;
  private float fishApproachAngle;
  private Entity caughtEntity;
  private boolean catching;
  private boolean canCatch = true;
  private int catchTimer;

  @SuppressWarnings("ConstantConditions")
  @OnlyIn(Dist.CLIENT)
  public BlazingFishingBobberEntity(World worldIn) {
    super(worldIn, Minecraft.getInstance().player, 0, 0, 0);
    this.luck = 0;
    this.lureSpeed = 0;
  }

  public BlazingFishingBobberEntity(PlayerEntity fishingPlayer, World worldIn, int luck,
      int lureSpeed) {
    super(fishingPlayer, worldIn, luck, lureSpeed);
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
  protected void registerData() {
    this.getDataManager().register(DATA_HOOKED_ENTITY, 0);
    this.getDataManager().register(DATA_CATCHING, false);
  }

  @Override
  public void notifyDataManagerChange(@Nonnull DataParameter<?> key) {

    if (DATA_HOOKED_ENTITY.equals(key)) {
      int i = this.getDataManager().get(DATA_HOOKED_ENTITY);
      this.caughtEntity = i > 0 ? this.world.getEntityByID(i - 1) : null;
    }

    if (DATA_CATCHING.equals(key)) {
      this.catching = this.getDataManager().get(DATA_CATCHING);

      if (this.catching) {
        this.setMotion(this.getMotion().x, (-0.4F * MathHelper.nextFloat(this.random, 0.6F, 1.0F)),
            this.getMotion().z);
      }
    }
    super.notifyDataManagerChange(key);
  }

  private boolean func_234615_h_() {
    Entity entity = this.func_234616_v_();

    if (entity != null) {

      for (Entity entity1 : this.world.getEntitiesInAABBexcluding(this,
          this.getBoundingBox().expand(this.getMotion()).grow(1.0D),
          (p_234613_0_) -> !p_234613_0_.isSpectator() && p_234613_0_.canBeCollidedWith())) {
        if (entity1.getLowestRidingEntity() == entity.getLowestRidingEntity()) {
          return false;
        }
      }
    }
    return true;
  }

  @Override
  public void tick() {
    this.random.setSeed(this.getUniqueID().getLeastSignificantBits() ^ this.world.getGameTime());

    try {
      boolean hasLeftOwner = HAS_LEFT_OWNER.getBoolean(this);

      if (!hasLeftOwner) {
        HAS_LEFT_OWNER.setBoolean(this, this.func_234615_h_());
      }
    } catch (IllegalAccessException e) {
      CombustiveFishing.LOGGER.error(e.getStackTrace());
    }

    if (!this.world.isRemote) {
      this.setFlag(6, this.isGlowing());
    }
    this.baseTick();
    PlayerEntity playerentity = this.func_234606_i_();

    if (playerentity == null) {
      this.remove();
    } else if (this.world.isRemote || !this.shouldStopFishing(playerentity)) {

      if (this.onGround) {
        ++this.ticksInGround;

        if (this.ticksInGround >= 1200) {
          this.remove();
          return;
        }
      } else {
        this.ticksInGround = 0;
      }
      float f = 0.0F;
      BlockPos blockpos = this.func_233580_cy_();
      FluidState fluidstate = this.world.getFluidState(blockpos);

      if (fluidstate.isTagged(FluidTags.LAVA) || fluidstate.isTagged(FluidTags.WATER)) {
        f = fluidstate.getActualHeight(this.world, blockpos);
      }
      boolean flag = f > 0.0F;

      if (this.currentState == State.FLYING) {

        if (this.caughtEntity != null) {
          this.setMotion(Vector3d.ZERO);
          this.currentState = State.HOOKED_IN_ENTITY;
          return;
        }

        if (flag) {
          this.setMotion(this.getMotion().mul(0.3D, 0.2D, 0.3D));
          this.currentState = State.BOBBING;
          return;
        }
        this.checkCollision();
      } else {

        if (this.currentState == State.HOOKED_IN_ENTITY) {

          if (this.caughtEntity != null) {

            if (!this.caughtEntity.isAlive()) {
              this.caughtEntity = null;
              this.currentState = State.FLYING;
            } else {
              this.setPosition(this.caughtEntity.getPosX(), this.caughtEntity.getPosYHeight(0.8D),
                  this.caughtEntity.getPosZ());
            }
          }

          return;
        }

        if (this.currentState == State.BOBBING) {
          Vector3d vector3d = this.getMotion();
          double d0 = this.getPosY() + vector3d.y - (double) blockpos.getY() - (double) f;

          if (Math.abs(d0) < 0.01D) {
            d0 += Math.signum(d0) * 0.1D;
          }
          this.setMotion(vector3d.x * 0.9D, vector3d.y - d0 * (double) this.rand.nextFloat() * 0.2D,
              vector3d.z * 0.9D);
          if (this.ticksCatchable <= 0 && this.ticksCatchableDelay <= 0) {
            this.canCatch = true;
          } else {
            this.canCatch = this.canCatch && this.catchTimer < 10 && this.canFish(blockpos);
          }

          if (flag) {
            this.catchTimer = Math.max(0, this.catchTimer - 1);

            if (this.catching) {
              this.setMotion(this.getMotion().add(0.0D,
                  -0.1D * (double) this.random.nextFloat() * (double) this.random.nextFloat(),
                  0.0D));
            }

            if (!this.world.isRemote) {
              this.catchingFish(blockpos);
            }
          } else {
            this.catchTimer = Math.min(10, this.catchTimer + 1);
          }
        }
      }

      if (!fluidstate.isTagged(FluidTags.WATER) && !fluidstate.isTagged(FluidTags.LAVA)) {
        this.setMotion(this.getMotion().add(0.0D, -0.03D, 0.0D));
      }
      this.move(MoverType.SELF, this.getMotion());
      this.func_234617_x_();

      if (this.currentState == State.FLYING && (this.onGround || this.collidedHorizontally)) {
        this.setMotion(Vector3d.ZERO);
      }
      double d1 = 0.92D;
      this.setMotion(this.getMotion().scale(d1));
      this.recenterBoundingBox();
    }
  }

  private boolean canFish(BlockPos p_234603_1_) {
    LavaType lavaType = LavaType.INVALID;

    for (int i = -1; i <= 2; ++i) {
      LavaType type1 = this.canFishAt(p_234603_1_.add(-2, i, -2), p_234603_1_.add(2, i, 2));

      switch (type1) {
        case INVALID:
          return false;
        case ABOVE_LAVA:
          if (lavaType == LavaType.INVALID) {
            return false;
          }
          break;
        case INSIDE_LAVA:
          if (lavaType == LavaType.ABOVE_LAVA) {
            return false;
          }
      }
      lavaType = type1;
    }
    return true;
  }

  private LavaType canFishAt(BlockPos pos1, BlockPos pos2) {
    return BlockPos.getAllInBox(pos1, pos2).map(this::getLavaType).reduce(
        (p_234601_0_, p_234601_1_) -> p_234601_0_ == p_234601_1_ ? p_234601_0_ : LavaType.INVALID)
        .orElse(LavaType.INVALID);
  }

  private LavaType getLavaType(BlockPos pos) {
    BlockState blockstate = this.world.getBlockState(pos);

    if (!blockstate.isAir(this.world, pos) && !blockstate.isIn(Blocks.LILY_PAD)) {
      FluidState fluidstate = blockstate.getFluidState();
      return fluidstate.isTagged(FluidTags.LAVA) && fluidstate.isSource() && blockstate
          .getCollisionShape(this.world, pos).isEmpty() ? LavaType.INSIDE_LAVA : LavaType.INVALID;
    } else {
      return LavaType.ABOVE_LAVA;
    }
  }

  private boolean shouldStopFishing(PlayerEntity playerEntity) {
    ItemStack itemstack = playerEntity.getHeldItemMainhand();
    ItemStack itemstack1 = playerEntity.getHeldItemOffhand();
    boolean flag = itemstack.getItem() instanceof BlazingFishingRodItem;
    boolean flag1 = itemstack1.getItem() instanceof BlazingFishingRodItem;

    if (playerEntity.isAlive() && (flag || flag1) && !(this.getDistanceSq(playerEntity)
        > 1024.0D)) {
      return false;
    } else {
      this.remove();
      return true;
    }
  }

  private void checkCollision() {
    RayTraceResult raytraceresult = ProjectileHelper
        .func_234618_a_(this, this::func_230298_a_, RayTraceContext.BlockMode.COLLIDER);
    this.onImpact(raytraceresult);
  }

  @Override
  protected void onEntityHit(@Nonnull EntityRayTraceResult p_213868_1_) {

    if (!this.world.isRemote) {
      this.caughtEntity = p_213868_1_.getEntity();
      this.setHookedEntity();
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

    if (this.rand.nextFloat() < 0.25F && !this.world.canSeeSky(blockpos)) {
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
        double d0 = this.getPosX() + (double) (f1 * (float) this.ticksCatchableDelay * 0.1F);
        double d1 = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        double d2 = this.getPosZ() + (double) (f2 * (float) this.ticksCatchableDelay * 0.1F);
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
        BlockState state = worldserver
            .getBlockState(new BlockPos(this.getPosX(), d1 - 1.0D, this.getPosZ()));
        Vector3d vec3d = this.getMotion();

        if (state.getMaterial() == Material.WATER) {
          this.setMotion(vec3d.x, -0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F), vec3d.z);
          this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
              1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
          worldserver.spawnParticle(ParticleTypes.BUBBLE, this.getPosX(), d3, this.getPosZ(),
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
          worldserver.spawnParticle(ParticleTypes.FISHING, this.getPosX(), d3, this.getPosZ(),
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
        } else if (state.getMaterial() == Material.LAVA) {
          this.setMotion(vec3d.x, -0.4F * MathHelper.nextFloat(this.rand, 0.6F, 1.0F), vec3d.z);
          this.playSound(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH, 0.25F,
              0.4F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
          worldserver.spawnParticle(ParticleTypes.FLAME, this.getPosX(), d3, this.getPosZ(),
              (int) (1.0F + this.getWidth() * 20.0F), this.getWidth(), 0.0D, this.getWidth(), 0.2F);
          worldserver.spawnParticle(ParticleTypes.SMOKE, this.getPosX(), d3, this.getPosZ(),
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
        double d4 = this.getPosX() + (double) (MathHelper.sin(f6) * f7 * 0.1F);
        double d5 = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        double d6 = this.getPosZ() + (double) (MathHelper.cos(f6) * f7 * 0.1F);
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
    PlayerEntity playerentity = this.func_234606_i_();

    if (!this.world.isRemote && playerentity != null) {
      int i = 0;
      net.minecraftforge.event.entity.player.ItemFishedEvent event = null;

      if (this.caughtEntity != null) {
        this.bringInHookedEntity();
        CriteriaTriggers.FISHING_ROD_HOOKED
            .trigger((ServerPlayerEntity) playerentity, itemStack, this, Collections.emptyList());
        this.world.setEntityState(this, (byte) 31);
        i = this.caughtEntity instanceof ItemEntity ? 3 : 5;
      } else if (this.ticksCatchable > 0) {
        LootContext.Builder lootcontext$builder = (new LootContext.Builder(
            (ServerWorld) this.world))
            .withParameter(LootParameters.POSITION, new BlockPos(this.getPositionVec()))
            .withParameter(LootParameters.TOOL, itemStack).withRandom(this.rand)
            .withLuck((float) this.luck + playerentity.getLuck());
        lootcontext$builder.withParameter(LootParameters.KILLER_ENTITY, playerentity)
            .withParameter(LootParameters.THIS_ENTITY, this);
        double d = (float) MathHelper.floor(this.getBoundingBox().minY) + 1.0F;
        BlockState state = this.world
            .getBlockState(new BlockPos(this.getPosX(), d - 1.0D, this.getPosZ()));
        ResourceLocation loottable;

        if (state.getMaterial() == Material.LAVA) {

          if (this.world.func_234923_W_() == World.field_234919_h_) {
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
              this.onGround ? 2 : 1, this);
          net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);

          if (event.isCanceled()) {
            this.remove();
            return event.getRodDamage();
          }
          CriteriaTriggers.FISHING_ROD_HOOKED
              .trigger((ServerPlayerEntity) playerentity, itemStack, this, list);

          for (ItemStack itemstack : list) {
            ItemEntity itementity = new ItemEntity(this.world, this.getPosX(),
                this.getPosY() + 2.0D, this.getPosZ(), itemstack);
            double d0 = playerentity.getPosX() - this.getPosX();
            double d1 = playerentity.getPosY() - this.getPosY();
            double d2 = playerentity.getPosZ() - this.getPosZ();
            double d3 = MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
            double d4 = 0.1D;
            itementity.setMotion(d0 * d4, d1 * d4 + Math.sqrt(d3) * 0.08D, d2 * d4);
            this.world.addEntity(itementity);
            playerentity.world.addEntity(
                new ExperienceOrbEntity(playerentity.world, playerentity.getPosX(),
                    playerentity.getPosY() + 0.5D, playerentity.getPosZ() + 0.5D,
                    this.rand.nextInt(6) + 1));

            if (itemstack.getItem().isIn(ItemTags.FISHES)) {
              playerentity.addStat(Stats.FISH_CAUGHT, 1);
            }
          }
          i = 1;
        }
      }

      if (this.onGround) {
        i = 2;
      }
      this.remove();
      return event == null ? i : event.getRodDamage();
    } else {
      return 0;
    }
  }

  @Override
  protected void bringInHookedEntity() {
    Entity entity = this.func_234616_v_();

    if (entity != null) {
      Vector3d vector3d = (new Vector3d(entity.getPosX() - this.getPosX(),
          entity.getPosY() - this.getPosY(), entity.getPosZ() - this.getPosZ())).scale(0.1D);
      this.caughtEntity.setMotion(this.caughtEntity.getMotion().add(vector3d));
    }
  }

  enum State {
    FLYING, HOOKED_IN_ENTITY, BOBBING
  }

  enum LavaType {
    ABOVE_LAVA, INSIDE_LAVA, INVALID
  }
}
