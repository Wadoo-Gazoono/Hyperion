package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.SoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Evoker;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GruskEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> HAS_HEAD = SynchedEntityData.defineId(GruskEntity.class, EntityDataSerializers.BOOLEAN);



    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ROAR = RawAnimation.begin().thenPlay("roar");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation DECAPITATE = RawAnimation.begin().thenPlay("decapitate");
    private static final RawAnimation LEAP = RawAnimation.begin().thenPlay("leap");

    public int roarCoolDown = 20;
    public int decapitateTimer = 0;
    public GruskEntity(EntityType<? extends GruskEntity> monster, Level level) {
        super(monster, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -0.2F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.2F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.2F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.4D).add(Attributes.ATTACK_DAMAGE, 12D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8d));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.goalSelector.addGoal(6, new GruskRoarGoal(this));
        this.goalSelector.addGoal(8, new GruskLeapGoal(this));


        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));


    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("hasHead", this.getHasHead());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasHead(tag.getBoolean("hasHead"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(HAS_HEAD, true);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(decapitateTimer > 0){
            return false;
        }
        if(getHasHead() && this.getHealth() - amount < 12f) {
                triggerAnim("controller", "decapitate");
                this.decapitateTimer = 40;
                BlockPos blockpos = this.blockPosition().offset(0, 1, 0);
                GruskHeadEntity head = EntityHandler.GRUSK_HEAD.get().create(this.level);
                if (head != null) {
                    head.moveTo(blockpos, 0.0F, 0.0F);
                    head.owner = this;
                    this.level.addFreshEntity(head);
                    head.setDeltaMovement(0d,0.3d,0d);
                }
                roarCoolDown = 100;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.setHasHead(true);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.decapitateTimer > 0){
            if(this.decapitateTimer == 36){
                setHasHead(false);
            }
            this.decapitateTimer--;
            this.getNavigation().stop();
            this.setDeltaMovement(0d,this.getDeltaMovement().y(),0d);
        }
        if(this.getTarget() == null){
            this.setAggressive(false);
        }

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).triggerableAnim("roar", ROAR).triggerableAnim("decapitate", DECAPITATE).triggerableAnim("leap",LEAP));
    }

    protected PlayState predicate(AnimationState<GruskEntity> state) {
        return state.isMoving() ? (this.isAggressive()) ? state.setAndContinue(RUN) : state.setAndContinue(WALK) : state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setHasHead(boolean hasHead){
        this.entityData.set(HAS_HEAD, hasHead);
    }

    public boolean getHasHead(){
        return this.entityData.get(HAS_HEAD);
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundRegistry.GRUSK_AMBIENCE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundRegistry.GRUSK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundRegistry.GRUSK_DEATH.get();
    }
}


class GruskRoarGoal extends Goal {
    public GruskEntity entity;
    public int tickTimer = 0;

    public GruskRoarGoal(GruskEntity entity){
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if(this.entity.roarCoolDown <= 0 && this.entity.decapitateTimer == 0 && entity.getHasHead()){
            return this.entity.getTarget() != null;
        }
        else{
            if(entity.getHasHead()) {
                this.entity.roarCoolDown--;
            }
            return false;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.entity.getTarget().isAlive() && this.entity.getTarget() != null && tickTimer < 38 && this.entity.decapitateTimer == 0 && entity.getHasHead();
    }

    @Override
    public void start() {
        super.start();
        this.entity.triggerAnim("controller", "roar");
    }

    @Override
    public void tick() {
        super.tick();
//        float angle = (float) Math.acos((this.entity.getTarget().position().x -  this.entity.position().x)/(this.entity.distanceTo(this.entity.getTarget())));
//        this.entity.setYBodyRot(angle);
        this.entity.getLookControl().setLookAt(this.entity.getTarget());
        if (tickTimer < 38) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget());
            tickTimer++;
            this.entity.getNavigation().stop();
            if(tickTimer == 5){
                this.entity.playSound(SoundEvents.RAVAGER_ROAR);
                for (LivingEntity livingentity : this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(10.2D, 1.0D, 10.2D))) {
                    if(livingentity instanceof GruskEntity == false){
                        livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2, false, true));
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.roarCoolDown = this.entity.getRandom().nextInt(200) + 100;
        tickTimer = 0;
    }
}

class GruskLeapGoal extends Goal{
    public GruskEntity entity;
    public int rePathTime = 0;
    public int leapCoolDown = 0;
    public GruskLeapGoal(GruskEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if(entity.getTarget() != null && this.entity.roarCoolDown > 10 && this.entity.decapitateTimer == 0){
            if(rePathTime > 0){
                rePathTime--;
                return false;
            }
            else{
                Path path = this.entity.getNavigation().createPath(this.entity.getTarget(), 0);
                rePathTime = this.entity.getRandom().nextInt(30) + 10;
                return path != null;
            }
        }
        this.entity.setAggressive(false);
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.roarCoolDown > 10 && this.entity.decapitateTimer == 0;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
//        float angle = (float) Math.acos((target.position().x -  this.entity.position().x)/(this.entity.distanceTo(target)));
//        this.entity.setYBodyRot(angle);
        this.entity.getLookControl().setLookAt(this.entity.getTarget());
        this.entity.setAggressive(true);
        if(rePathTime > 0){
            rePathTime--;
        }
        else{
            Path path = this.entity.getNavigation().createPath(this.entity.getTarget(), 0);
            this.entity.getNavigation().moveTo(path, 1f);
        }

        if(this.entity.distanceTo(target) < 10f){
            if(leapCoolDown > 0){
                leapCoolDown--;
            }
            else {
                attack(target);
                this.entity.triggerAnim("controller", "leap");
                leapCoolDown = 35;
            }
        }
    }

    public void attack(LivingEntity target){
        this.entity.setDeltaMovement(this.entity.position().vectorTo(this.entity.getTarget().position()).normalize().multiply(1.2f,1f,1.2f).add(0d,0.2d,0d));
        this.entity.getLookControl().setLookAt(target);
        if(this.entity.distanceTo(target) < 4.5f){
            this.entity.doHurtTarget(target);
        }
    }
}