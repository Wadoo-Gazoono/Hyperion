package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.forgenaut.ForgenautAttack;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;

public class ForgenautSpawnAttack extends ForgenautAttack {
    public ForgenautSpawnAttack(ForgenautEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        this.entity.setDeltaMovement(0d, this.entity.getDeltaMovement().y, 0d);
        this.entity.getNavigation().stop();
        if(this.entity.getTarget() != null){
            this.entity.getLookControl().setLookAt(this.entity.getTarget());
        }
        if (currentTick == 85){
            this.entity.setPhase(1);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setPhase(1);
    }
}
