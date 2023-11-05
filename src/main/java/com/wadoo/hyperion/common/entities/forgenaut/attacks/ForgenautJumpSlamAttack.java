package com.wadoo.hyperion.common.entities.forgenaut.attacks;

import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautAttack;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class ForgenautJumpSlamAttack extends ForgenautAttack {
    public ForgenautJumpSlamAttack(ForgenautEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        this.entity.setDeltaMovement(0d, this.entity.getDeltaMovement().y, 0d);
        this.entity.getNavigation().stop();
        LivingEntity target;
        if(this.entity.getTarget() != null){
            this.entity.getLookControl().setLookAt(this.entity.getTarget());
            target= this.entity.getTarget();
            this.entity.yBodyRot = (float) Math.atan2(target.position().z - this.entity.position().z, target.position().x- this.entity.position().x);
        }
        if(currentTick == 4){
            if (this.entity.getTarget() != null) this.entity.setDeltaMovement(this.entity.position().vectorTo(this.entity.getTarget().position()).multiply(0.75f,0f,0.75f).add(0d,0.2d,0d));
        }
        //Credit to Bob Mowzie
        if (currentTick == 14) {
            CameraShakeEntity.cameraShake(this.entity.level(), this.entity.position(), 15, 0.2f, 10, 5);
            List<LivingEntity> entitiesHit = this.entity.level().getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(7.2D, 1.0D, 7.2D));
            float damage = (float)entity.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
            for (LivingEntity entityHit : entitiesHit) {
                if(entityHit == this.entity) continue;
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
                float arc_angle = 360;
                float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - entity.getZ()) * (entityHit.getZ() - entity.getZ()) + (entityHit.getX() - entity.getX()) * (entityHit.getX() - entity.getX())) - entityHit.getBbWidth() / 2f;
                if (entityHitDistance <= 8f && (entityRelativeAngle <= arc_angle / 2 && entityRelativeAngle >= -arc_angle / 2) || (entityRelativeAngle >= 360 - arc_angle / 2 || entityRelativeAngle <= -360 + arc_angle / 2)) {
                    entityHit.hurt(this.entity.damageSources().mobAttack(entity), damage);
                    if (entityHit.isBlocking())
                        entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                    Vec3 velocity = this.entity.position().vectorTo(entityHit.position()).normalize().multiply(4.5f,0f,4.5f).add(0d, 0.5d,0d);
                    entityHit.setDeltaMovement(velocity);
                }

            }
        }
        if(currentTick == 16){
            for(int i = 0; i <= 18; i ++) {
                BasaltSpikeEntity spike = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike.setPos(new Vec3(0D, 0D, 6).yRot((float) Math.toRadians(-this.entity.yBodyRot + (i * 20))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike);
            }
        }
        if(currentTick > 20){
            this.entity.setDeltaMovement(0d,-0.2d,0d);
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setJumpSlamAttackCooldown(90);
    }
}
