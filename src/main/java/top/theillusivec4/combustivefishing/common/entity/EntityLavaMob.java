package top.theillusivec4.combustivefishing.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityLavaMob extends EntityWaterMob {

    protected EntityLavaMob(EntityType<?> type, World world) {
        super(type, world);
        this.isImmuneToFire = true;
    }

    @Override
    protected int getExperiencePoints(EntityPlayer player) {
        return 1 + this.world.rand.nextInt(6);
    }

    @Override
    protected void updateAir(int currentAir) {

        if (this.isAlive() && !this.isInLava()) {
            this.setAir(currentAir - 1);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }

    }
}
