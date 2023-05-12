package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;

public class GruskLeapGoal extends AnimatedAttack {
    public GruskLeapGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.setYRot(this.entity.yRotO);
        if(entity.getTarget() == null) return;
            if (currentTick == 2) {

                entity.setDeltaMovement(entity.position().vectorTo(entity.getTarget().position()).normalize().multiply(1.6f, 0.6f, 1.6f).add(0d, 0.2d, 0d));
            }
            if (entity.distanceTo(this.entity.getTarget()) < 0.8f) {
                entity.doHurtTarget(entity.getTarget());
            }

    }
}
