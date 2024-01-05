package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import com.wadoo.hyperion.common.entities.projectiles.LavaBallProjectile;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNRockThrowAttack extends AnimatedAttack {
    public FNRockThrowAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.freeze(true);
        if (currentTick == 21){
            Vec3 lookVec = fn.getViewVector(1f);
            if (fn.getTarget() == null) return;
            CameraShakeEntity.cameraShake(fn.level(), fn.position(), 25, 0.32f, 30, 3);

            for (int i= 0; i < fn.getRandom().nextInt(3,6); i++){
                Vec3 spawnPos = new Vec3(0d,0d, 3d).yRot((float)Math.toRadians(-fn.getYRot()) + i).add(fn.position());
                LavaBallProjectile lava = new LavaBallProjectile(this.entity.level(), this.entity);
                lava.moveTo(spawnPos);
                LivingEntity t = fn.getTarget();
                double d0 = t.getEyeY() - (double)0F;
                double d1 = (t.getX() + t.getDeltaMovement().x) - fn.getX() + fn.getRandom().nextFloat() / 5f;
                double d2 = d0 - lava.getY();
                double d3 = (t.getZ() + t.getDeltaMovement().z) - fn.getZ() + fn.getRandom().nextFloat() / 5f;
                double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.5F;
                lava.shoot(d1, d2 + d4, d3, 1.1f,fn.getRandom().nextFloat());
                fn.level().addFreshEntity(lava);


            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.setAttackCooldown(20);
    }
}
