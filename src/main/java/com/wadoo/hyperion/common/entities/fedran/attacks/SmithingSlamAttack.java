package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class SmithingSlamAttack extends AnimatedAttack {
    public SmithingSlamAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(entity.getTarget() != null) entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        if (currentTick == 79) {
            CameraShakeEntity.cameraShake(this.entity.level(), this.entity.position(), 45, 0.08f, 20, 10);
            for(int i = 0; i < 5; i++){
                BasaltSpikeEntity spike = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike.setPos(new Vec3(0D,0D,7.5D + (i * 2)).yRot((float)Math.toRadians(-this.entity.yBodyRot)).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike);
            }


            List<LivingEntity> entitiesHit = this.entity.level().getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(7.2D, 1.0D, 7.2D));
            float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            for (LivingEntity entityHit : entitiesHit) {
                float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - entity.getZ(), entityHit.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
                float entityAttackingAngle = entity.yBodyRot % 360;
                if (entityHit instanceof FedranEntity) continue;
                if (entityHitAngle < 0) {
                    entityHitAngle += 360;
                }
                if (entityAttackingAngle < 0) {
                    entityAttackingAngle += 360;
                }
                float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                float arc_angle = 20;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                if (entityHitDistance <= 8f && (entityRelativeAngle <= arc_angle / 2 && entityRelativeAngle >= -arc_angle / 2) || (entityRelativeAngle >= 360 - arc_angle / 2 || entityRelativeAngle <= -360 + arc_angle / 2)) {
                    entityHit.hurt(this.entity.damageSources().mobAttack(entity), damage);
                    if (entityHit.isBlocking())
                        entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                    entityHit.setDeltaMovement(entityHit.getDeltaMovement().x * 2, entityHit.getDeltaMovement().y + 0.2d, entityHit.getDeltaMovement().z * 2);
                }
            }
        }

    }
}
