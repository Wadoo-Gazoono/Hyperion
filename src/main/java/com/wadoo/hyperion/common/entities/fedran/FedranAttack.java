package com.wadoo.hyperion.common.entities.fedran;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;

public class FedranAttack extends AnimatedAttack {
    public FedranEntity entity;
    public FedranAttack(FedranEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
        this.entity = entity;
    }

    @Override
    public void stop() {

        super.stop();
        switch (this.entity.getPhase()){
            case 1:
                this.entity.setAttackCooldown(20);
                return;
            case 2:
                this.entity.setAttackCooldown(10);
                return;
            case 3:
                this.entity.setAttackCooldown(5);
                return;
            case 4:
                this.entity.setAttackCooldown(1);
                return;
        }
    }
}
