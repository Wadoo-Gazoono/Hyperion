package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class FNSlamRightAttack extends AnimatedAttack {
    public FNSlamRightAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        if (entity instanceof ForgenautEntity fn){
            fn.freeze();
            if (currentTick == 10) {
                CameraShakeEntity.cameraShake(fn.level(), fn.position(), 12, 0.32f, 30, 3);
                List<LivingEntity> entitiesHit = fn.level().getEntitiesOfClass(LivingEntity.class, fn.getBoundingBox().inflate(7.2D, 1.0D, 7.2D));
                float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
                for (LivingEntity entityHit : entitiesHit) {
                    if(entityHit == fn) continue;
                    float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - entity.getZ(), entityHit.getX() - entity.getX()) * (180 / Math.PI) - 90) % 360);
                    float entityAttackingAngle = entity.yBodyRot % 360;
                    //if (entityHit instanceof HyperionLivingEntity) continue;
                    if (entityHitAngle < 0) {
                        entityHitAngle += 360;
                    }
                    if (entityAttackingAngle < 0) {
                        entityAttackingAngle += 360;
                    }
                    float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
                    float arc_angle = 180;
                    float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                    if (entityHitDistance <= 4f && (entityRelativeAngle <= arc_angle / 2 && entityRelativeAngle >= -arc_angle / 2) || (entityRelativeAngle >= 360 - arc_angle / 2 || entityRelativeAngle <= -360 + arc_angle / 2)) {
                        entityHit.hurt(fn.damageSources().mobAttack(entity), damage);
                        if (entityHit.isBlocking())
                            entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                        Vec3 velocity = fn.position().vectorTo(entityHit.position()).normalize().multiply(4.5f,1f,4.5f).add(0d, 0.5d,0d);
                        entityHit.setDeltaMovement(velocity);
                    }
                }
            }


        }
    }

}
