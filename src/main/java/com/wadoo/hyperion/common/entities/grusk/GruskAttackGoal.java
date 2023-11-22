package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import net.minecraft.sounds.SoundEvents;
import software.bernie.example.registry.SoundRegistry;

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
        if (currentTick == 5) this.entity.playSound(SoundsRegistry.GRUSK_IDLE.get(), 1f, 0.8f);
        if(entity.getTarget() == null) return;
            if (currentTick == 15) {
                if (entity.distanceTo(this.entity.getTarget()) < 2.5f) {
                    entity.doHurtTarget(entity.getTarget());
                }
            }


    }
}
