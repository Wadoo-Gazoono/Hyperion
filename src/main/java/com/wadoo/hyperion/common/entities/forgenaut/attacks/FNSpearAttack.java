package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNSpearAttack extends AnimatedAttack {
    public FNSpearAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        ForgenautEntity fn = (ForgenautEntity) this.entity;

        if (this.entity.getTarget() != null){
            LivingEntity t = fn.getTarget();
            if (currentTick < 2)fn.getLookControl().setLookAt(t, 60f,60f);
            else{
                fn.setYRot(fn.yRotO);
            }
            if(currentTick < 15)fn.setDeltaMovement(fn.position().vectorTo(t.position()).scale(0.3f).multiply(1d,0d,1d));
        }
        if (currentTick < 10){
            fn.hurtEntitiesInArcAndRange(50, 4, false, 8, 50);
        }
    }

    @Override
    public void stop() {
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        if (state == 10) {
            float rand = this.entity.getRandom().nextFloat();
            if (rand < 0.5f) {
                fn.setTransition(1);
            }
            else {
                fn.setTransition(2);
            }
        }
        else{
            fn.setTransition(0);
            super.stop();
        }

        fn.setAttackCooldown(4);
    }
}
