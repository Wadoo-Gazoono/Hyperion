package com.wadoo.hyperion.common.entities.clinker.attacks;

import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.clinker.ClinkerEntity;

public class ClinkerLeapAttack extends AnimatedAttack {
    public ClinkerLeapAttack(ClinkerEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        this.entity.getNavigation().stop();
        this.entity.setDeltaMovement(this.entity.getViewVector(1f).normalize().multiply(0.3d, 0.7d, 0.3d));
        if(this.entity.getTarget() != null){
            this.entity.getLookControl().setLookAt(this.entity.getTarget());
            if (this.entity.distanceTo(this.entity.getTarget()) < 1f){
                this.entity.doHurtTarget(this.entity.getTarget());
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }
}
