package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranAttack;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class StompAttack extends FedranAttack {
    public StompAttack(FedranEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(entity.getTarget() == null) return;
        entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        if (currentTick == 34) {
            CameraShakeEntity.cameraShake(this.entity.level(), this.entity.position(), 45, 0.08f, 30, 20);
            this.entity.playSound(SoundEvents.GENERIC_EXPLODE);
            for (LivingEntity livingentity : this.entity.level().getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().move(0d,-1d,0d).inflate(7.5D, 1D, 7.5D).move(0d,-1.4d,0d))) {
                if(livingentity instanceof FedranEntity == false){
                    this.entity.doHurtTarget(livingentity);
                    livingentity.setDeltaMovement(0d,1d,0d);
                }
            }
        }

    }
}
