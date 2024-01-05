package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;

public class FNSpawnAttack extends AnimatedAttack {
    public FNSpawnAttack(ForgenautEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);

    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (this.entity instanceof ForgenautEntity e) {
            if (currentTick < 35) e.freeze(false);
            else {e.freeze(true);}
            if (currentTick == 85) {
                e.setPhase(1);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (this.entity instanceof ForgenautEntity e) e.setPhase(1);
    }
}
