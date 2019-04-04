package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Particles;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import top.theillusivec4.combustivefishing.common.init.CombustiveFishingEntities;

import javax.annotation.Nonnull;

public class EntityThrownCombustiveCod extends EntityThrowable {

    public EntityThrownCombustiveCod(World worldIn) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, worldIn);
    }

    public EntityThrownCombustiveCod(World worldIn, EntityLivingBase throwerIn) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, throwerIn, worldIn);
    }

    public EntityThrownCombustiveCod(World worldIn, double x, double y, double z) {
        super(CombustiveFishingEntities.THROWN_COMBUSTIVE_COD, x, y, z, worldIn);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {

        if (id == 3) {
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(Particles.FLAME, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {

        if (result.entity != null) {
            result.entity.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()).setFireDamage(), 1.0F);
        }

        if (!this.world.isRemote) {
            this.world.newExplosion(null, this.posX, this.posY, this.posZ, 1, true, true);
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }
}
