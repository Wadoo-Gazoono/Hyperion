package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNPunchAttack extends AnimatedAttack {
    public FNPunchAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (entity instanceof ForgenautEntity fn){
            fn.freeze(true);

            if (currentTick == 16) {
                CameraShakeEntity.cameraShake(fn.level(), fn.position(), 4, 0.22f, 30, 5);
                fn.hurtEntitiesInArcAndRange(100f, 5.5f, false, 8f, 90);

            }


        }
    }

    @Override
    public void stop() {
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        if (state == 2 || state == 4) {
            float rand = this.entity.getRandom().nextFloat();
            if (fn.getTarget() != null &&fn.distanceTo(fn.getTarget()) > 6f) {
                if (state == 2) fn.setTransition(4);
                else{fn.setTransition(1);}
            } else {
                if (rand < 0.3f) {
                    fn.setTransition(1);
                } else if (rand <= 0.7) {
                    fn.setTransition(2);
                } else {
                    fn.setTransition(3);
                }
            }
        }
        else{
            fn.setTransition(0);
            super.stop();
        }

        fn.setAttackCooldown(4);
    }
}
