package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class SteamReleaseAttack extends AnimatedAttack {
    public SteamReleaseAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(entity.getTarget() != null) entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        if (currentTick > 35 && currentTick < 130) {
            List<LivingEntity> entitiesHit = this.entity.level().getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(12.2D, 1.0D, 12.2D));
            for(LivingEntity e: entitiesHit){
                if (e instanceof FedranEntity) continue;
                e.setDeltaMovement(this.entity.position().vectorTo(e.position()).normalize().multiply(0.2f,0.2f,0.2f));
                if (currentTick % 2 == 0){
                    e.hurt(this.entity.damageSources().mobAttack(this.entity),2);
                    e.setSecondsOnFire(4);
                }
            }
        }
        if (currentTick == 150){
            if (this.entity instanceof FedranEntity){
                ((FedranEntity) this.entity).setHealthThreshold(((FedranEntity) this.entity).getHealthThreshold() - 150);
                ((FedranEntity) this.entity).setTransitioned(true);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
