package com.wadoo.hyperion.common.entities.obelisk;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;

public class ObeliskStandUpGoal extends AnimatedAttack {
    public ObeliskStandUpGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.setYRot(this.entity.yRotO);
        entity.getNavigation().stop();
        entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y(), 0d);
    }

    @Override
    public void stop() {
        super.stop();
        if (entity instanceof ObeliskEntity e) e.stand();
    }
}
