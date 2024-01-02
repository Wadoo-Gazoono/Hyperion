package com.wadoo.hyperion.common.entities.ai;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;

public class AnimatedTransitionAttack extends AnimatedAttack{
    private final int stateToTransitionTo;
    private final int transitionStateNeeded;
    private final boolean shouldFreeze;
    public AnimatedTransitionAttack(HyperionLivingEntity entity, int transitionStateNeeded, int state, int stateToTransitionTo, String animation, int animLength, boolean shouldFreeze) {
        super(entity, state, animation, animLength);
        this.stateToTransitionTo = stateToTransitionTo;
        this.transitionStateNeeded = transitionStateNeeded;
        this.shouldFreeze = shouldFreeze;
    }

    @Override
    public boolean canUse() {
        return entity.getAnimation() == this.state && this.entity.getTransition() == this.transitionStateNeeded;
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (shouldFreeze){
            this.entity.freeze();
        }
    }

    @Override
    public void stop() {
        this.entity.setTransition(0);
        this.entity.setAnimation(this.stateToTransitionTo);
    }
}
