package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.DataTicket;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class HyperionLivingEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");

    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(HyperionLivingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> TRANSITION_STATE = SynchedEntityData.defineId(HyperionLivingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> CAN_LOOK = SynchedEntityData.defineId(HyperionLivingEntity.class, EntityDataSerializers.BOOLEAN);

    public HyperionLivingEntity(EntityType<? extends HyperionLivingEntity> monster, Level level) {
        super(monster, level);
    }

   // public static AttributeSupplier.Builder createAttributes() {
   //     return null;
   // }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE, 0);
        this.entityData.define(TRANSITION_STATE, 0);
        this.entityData.define(CAN_LOOK, true);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1.0d));
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


    public void setAnimation(Integer state){
        this.entityData.set(STATE,state);
    }

    public Integer getAnimation(){
        return this.entityData.get(STATE);
    }

    public void setTransition(int trans){
        this.entityData.set(TRANSITION_STATE, trans);
    }

    public int getTransition(){
        return this.entityData.get(TRANSITION_STATE);
    }

    public void setCanLook(boolean look){
        this.entityData.set(CAN_LOOK, look);
    }

    public boolean getCanLook(){
        return this.entityData.get(CAN_LOOK);
    }

    public void freeze(boolean lookAtTarget){
        if (onGround()) this.setDeltaMovement(0d, this.getDeltaMovement().y, 0d);
        this.getNavigation().stop();
        setCanLook(false);
        if (lookAtTarget && this.getTarget() != null){
            this.getLookControl().setLookAt(getTarget(), 30f, 30f);
        }
        else{
            this.setYRot(this.yRotO);
        }
    }

    public void unfreeze(){
        setCanLook(true);
    }

    public void hurtEntitiesInArcAndRange(float arc_angle, float range, boolean breakShield, float distanceCheck, int shieldCoolDown){
        List<LivingEntity> entitiesHit = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(distanceCheck, 1.0D, distanceCheck));
        float damage = (float)this.getAttribute(Attributes.ATTACK_DAMAGE).getValue();
        for (LivingEntity entityHit : entitiesHit) {
            if(entityHit == this) continue;
            float entityHitAngle = (float) ((Math.atan2(entityHit.getZ() - this.getZ(), entityHit.getX() - this.getX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.yBodyRot % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getZ() - this.getZ()) * (entityHit.getZ() - this.getZ()) + (entityHit.getX() - this.getX()) * (entityHit.getX() - this.getX())) - entityHit.getBbWidth() / 2f;
            if (entityHitDistance <= range && (entityRelativeAngle <= arc_angle / 2 && entityRelativeAngle >= -arc_angle / 2) || (entityRelativeAngle >= 360 - arc_angle / 2 || entityRelativeAngle <= -360 + arc_angle / 2)) {
                entityHit.hurt(this.damageSources().mobAttack(this), damage);
                if (entityHit.isBlocking() && breakShield)
                    entityHit.getUseItem().hurtAndBreak(400, entityHit, player -> player.broadcastBreakEvent(entityHit.getUsedItemHand()));
                if (entityHit.isBlocking() && shieldCoolDown >0 && entityHit instanceof Player player){
                    player.getCooldowns().addCooldown(entityHit.getUseItem().getItem(), shieldCoolDown);
                }
                Vec3 velocity = this.position().vectorTo(entityHit.position()).normalize().multiply(4.5f,1f,4.5f).add(0d, 0.5d,0d);
                entityHit.setDeltaMovement(velocity);
            }
        }
    }
}

