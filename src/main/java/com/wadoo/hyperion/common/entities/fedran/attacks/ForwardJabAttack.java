package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;

public class ForwardJabAttack extends FedranAttack {
    public ForwardJabAttack(FedranEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(entity.getTarget() == null) return;
        entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        if (currentTick == 25) {
            entity.getTarget().setDeltaMovement(entity.position().vectorTo(entity.getTarget().position()).normalize().multiply(5f,1.1f,5f).add(0d,1d,0d));
            if (entity.distanceTo(this.entity.getTarget()) < 10f) {
                entity.doHurtTarget(entity.getTarget());
            }
        }

    }
}
