package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import net.minecraft.world.damagesource.DamageSource;

public class KickAttack extends FedranAttack {
    public KickAttack(FedranEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        this.entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y,0d);
        if(entity.getTarget() == null) return;
        entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        if (currentTick == 20) {
            entity.getTarget().setDeltaMovement(entity.position().vectorTo(entity.getTarget().position()).normalize().multiply(7f, 1.1f, 7f).add(0d, 0.2d, 0d));
            if (entity.distanceTo(this.entity.getTarget()) < 10f) {
                this.entity.getTarget().hurt(this.entity.damageSources().mobAttack(this.entity), 4f);
            }
        }
    }
}
