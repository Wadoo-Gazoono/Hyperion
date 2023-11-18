package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;

public class GruskAttackGoal extends AnimatedAttack {
    public GruskAttackGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void start() {
        super.start();
        this.timer = 0;
        this.entity.triggerAnim("attackController", this.animation);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.setYRot(this.entity.yRotO);
        if(entity.getTarget() == null) return;
            if (currentTick == 15) {
                if (entity.distanceTo(this.entity.getTarget()) < 1f) {
                    entity.doHurtTarget(entity.getTarget());
                }
            }


    }
}
