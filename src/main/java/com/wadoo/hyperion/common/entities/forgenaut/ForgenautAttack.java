package com.wadoo.hyperion.common.entities.forgenaut;

import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;

public class ForgenautAttack extends AnimatedAttack {
    public ForgenautEntity entity;
    public ForgenautAttack(ForgenautEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        this.entity = entity;
    }

    @Override
    public void stop() {
        super.stop();
    }
}
