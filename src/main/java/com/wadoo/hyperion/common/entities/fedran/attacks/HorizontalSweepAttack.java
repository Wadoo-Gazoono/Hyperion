package com.wadoo.hyperion.common.entities.fedran.attacks;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedAttack;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class HorizontalSweepAttack extends AnimatedAttack {
    public HorizontalSweepAttack(HyperionLivingEntity entity, int state, String animation, int animLength) {
        super(entity, state, animation, animLength);
    }

    @Override
    public void doEffects(int currentTick) {
        super.doEffects(currentTick);
        entity.getNavigation().stop();
        if(entity.getTarget() != null) entity.getLookControl().setLookAt(this.entity.getTarget(),80f,80f);
        switch (currentTick){
            case 43:
                BasaltSpikeEntity spike = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike.setPos(new Vec3(0D,0D,4).yRot((float)Math.toRadians(-this.entity.yBodyRot - (100))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike);
                break;
            case 44:
                BasaltSpikeEntity spike4 = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike4.setPos(new Vec3(0D,0D,4).yRot((float)Math.toRadians(-this.entity.yBodyRot - (40))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike4);
                break;
            case 45:
                BasaltSpikeEntity spike1 = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike1.setPos(new Vec3(0D,0D,4).yRot((float)Math.toRadians(-this.entity.yBodyRot - (00))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike1);
            break;
            case 48:
                BasaltSpikeEntity spike5 = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike5.setPos(new Vec3(0D,0D,4).yRot((float)Math.toRadians(-this.entity.yBodyRot + (30))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike5);
                break;
            case 50:
                BasaltSpikeEntity spike2 = EntityHandler.BASALT_SPIKE.get().create(this.entity.level());
                spike2.setPos(new Vec3(0D,0D,4).yRot((float)Math.toRadians(-this.entity.yBodyRot + (60))).add(this.entity.position()));
                this.entity.level().addFreshEntity(spike2);
                break;
        }
        //Credit to Bob Mowzie
        if (currentTick == 41) {
            CameraShakeEntity.cameraShake(this.entity.level(), this.entity.position(), 45, 0.08f, 20, 10);
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
                float arc_angle = 180;
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
