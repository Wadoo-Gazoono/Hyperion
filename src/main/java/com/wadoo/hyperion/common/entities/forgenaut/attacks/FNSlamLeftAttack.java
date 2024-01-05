package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNSlamLeftAttack extends AnimatedAttack {
    public FNSlamLeftAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (entity instanceof ForgenautEntity fn){
            fn.freeze(true);
            if (currentTick == 10) {
                CameraShakeEntity.cameraShake(fn.level(), fn.position(), 12, 0.32f, 30, 3);
                fn.hurtEntitiesInArcAndRange(170f, 5f, false, 7.2f, 100);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.setAttackCooldown(4);
    }
}
