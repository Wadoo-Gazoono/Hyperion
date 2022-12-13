package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Predicate;

public class GruskEntity extends Monster implements IAnimationTickable, IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory((IAnimatable) this);
    private static final EntityDataAccessor<Boolean> ACTIVATED =  SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LEAPING =  SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    public int wakeup_timer = 0;
    public int decapitate_timer = 0;
    public int leap_timer = 0;

    public GruskEntity(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));
        this.goalSelector.addGoal(5,new RandomStrollGoal(this,1.0f));
        this.goalSelector.addGoal(3, new GruskAttackGoal(this));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE,0);
        this.entityData.define(ACTIVATED,false);
        this.entityData.define(LEAPING,false);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimationSpeed(1f);
        switch (getAnimState()) {
            case 0:
                if(getActivated()){
                    if(event.isMoving()){
                        if(getLeaping()){
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("leap", ILoopType.EDefaultLoopTypes.LOOP));
                        }
                        else {
                            event.getController().setAnimation(new AnimationBuilder().addAnimation("walk", ILoopType.EDefaultLoopTypes.LOOP));
                        }
                    }
                    else{
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_activated", ILoopType.EDefaultLoopTypes.LOOP));
                    }
                }
                else {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_deactivated", ILoopType.EDefaultLoopTypes.LOOP));
                }
                return PlayState.CONTINUE;
            case 1:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("roar", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
                return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 35.0D).add(Attributes.MOVEMENT_SPEED, 0.26F).add(Attributes.KNOCKBACK_RESISTANCE, 0.3D).add(Attributes.ATTACK_DAMAGE,4f);
    }

    @Override
    public void tick() {
        super.tick();
        if(!getActivated()){
            this.setDeltaMovement(0d,this.getDeltaMovement().y(),0d);
            this.getNavigation().stop();
            if(this.getTarget() != null && this.distanceToSqr(this.getTarget()) < 15f){
                setActivated(true);
            }
        }
        else{
            if(wakeup_timer < 32){
                this.setDeltaMovement(0d,this.getDeltaMovement().y(),0d);
                this.getNavigation().stop();
                setAnimState(1);
                if(this.getTarget() != null){
                    this.getLookControl().setLookAt(this.getTarget(),30f,30f);
                }
                wakeup_timer++;
            }
            else{
                setAnimState(0);
                if(getLeaping() && leap_timer < 20){
                    leap_timer++;
                }
                else{
                    leap_timer = 0;
                    setLeaping(true);
                }
            }
        }

        if(getLeaping()){
            System.out.println("leaping");
        }
    }


    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<GruskEntity>(this, "controller", 2, this::predicate));
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }


    public void setAnimState(int AnimState){
        this.entityData.set(ANIM_STATE,AnimState);
    }

    public int getAnimState(){
        return this.entityData.get(ANIM_STATE);
    }

    public void setActivated(boolean activated){
        this.entityData.set(ACTIVATED,activated);
    }

    public boolean getActivated(){
        return this.entityData.get(ACTIVATED);
    }

    public boolean getLeaping(){return this.entityData.get(LEAPING);}

    public void setLeaping(boolean leaping){this.entityData.set(LEAPING, leaping);}


}

class GruskAttackGoal extends Goal{
    public GruskEntity entity;
    public GruskAttackGoal(GruskEntity entity){
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return entity.getTarget() != null && entity.getActivated();
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = entity.getTarget();
        this.entity.getLookControl().setLookAt(livingEntity, 30, 30);
        this.entity.getNavigation().moveTo(this.entity.getNavigation().createPath(livingEntity, 1), 1.25d);
        if (this.entity.tickCount % 18 == 0) {
            System.out.println("grrr");
            this.entity.setLeaping(true);
            Vec3 vec3 = this.entity.getViewVector(1.0F);
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().add(vec3.x() * 1.5, 0.45, vec3.z() * 1.5));
        }
    }
}
