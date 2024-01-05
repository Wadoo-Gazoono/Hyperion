package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNThunderClapAttack extends AnimatedAttack {
    public FNThunderClapAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.freeze(true);
        if (currentTick == 31){
            fn.hurtEntitiesInArcAndRange(320f, 10f, false, 20f, 300);
        }
    }

    @Override
    public void stop() {
        super.stop();
        ForgenautEntity fn = (ForgenautEntity) this.entity;
        fn.setAttackCooldown(8);
    }
}
