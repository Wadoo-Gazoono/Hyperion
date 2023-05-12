package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class GruskRoarGoal extends AnimatedAttack {
    public GruskRoarGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.setYRot(this.entity.yRotO);
        entity.getNavigation().stop();
        if(currentTick == 5){
            this.entity.playSound(SoundEvents.RAVAGER_ROAR);
            for (LivingEntity livingentity : this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(18.2D, 1.0D, 18.2D))) {
                if(livingentity instanceof GruskEntity == false){
                    livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 120, 2, false, true));
                }
            }
        }
    }
}
