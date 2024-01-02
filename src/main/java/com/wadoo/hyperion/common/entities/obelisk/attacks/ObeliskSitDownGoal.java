package com.wadoo.hyperion.common.entities.obelisk.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.obelisk.ObeliskEntity;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class ObeliskSitDownGoal extends AnimatedAttack {
    public ObeliskSitDownGoal(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.setYRot(this.entity.yRotO);
        entity.getNavigation().stop();
        entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y(), 0d);
    }

    @Override
    public void stop() {
        super.stop();
        if (entity instanceof ObeliskEntity e) e.sit();
    }
}
