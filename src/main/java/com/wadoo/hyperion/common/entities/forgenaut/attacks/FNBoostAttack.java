package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FNBoostAttack extends AnimatedAttack {
    public FNBoostAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.freeze(true);
        if (currentTick > 12 && currentTick < 23){
            Vec3 moveVec = this.entity.getViewVector(1f).add(0d,0.5d,0d);
            if (fn.getTarget() != null){
                moveVec = fn.position().vectorTo(fn.getTarget().position()).normalize().scale(5f).add(0d,01d,0d);
                if (fn.distanceTo(fn.getTarget()) < 7f){
                    stop();
                }
            }
            fn.setDeltaMovement(moveVec.scale(0.5f));
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
