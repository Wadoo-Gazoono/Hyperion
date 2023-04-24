package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.ai.util.Attack;
import com.wadoo.hyperion.common.entities.ai.util.AttackChooser;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import com.wadoo.hyperion.common.util.GrabAnimation;
import com.wadoo.hyperion.common.util.GrabKeyframe;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.At;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.LinkedList;

public class GruskEntity extends HyperionLivingEntity implements GeoEntity {
    private static final EntityDataAccessor<Boolean> HAS_HEAD = SynchedEntityData.defineId(GruskEntity.class, EntityDataSerializers.BOOLEAN);
    private LinkedList<Attack> attacks = new LinkedList<Attack>();

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ROAR = RawAnimation.begin().thenPlay("roar");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation DECAPITATE = RawAnimation.begin().thenPlay("decapitate");
    private static final RawAnimation LEAP = RawAnimation.begin().thenPlay("leap");
    private static final RawAnimation EAT = RawAnimation.begin().thenPlay("grab_test");

    public int roarCoolDown = 20;
    public int leapCoolDown = 10;
    public int eatCoolDown = 10;

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
        //this.goalSelector.addGoal(1, new FloatGoal(this));
       // this.goalSelector.addGoal(8, new RandomStrollGoal(this,1.0f) {
        //    @Override
         //   public boolean canUse(){
          //      if(mob instanceof GruskEntity){
          //          return ((GruskEntity) mob).getAnimation() == 0 && super.canUse();
          //      }
          //      return false;
         //   }
       // });
        //this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        //this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.goalSelector.addGoal(2, new AttackChooser(this,new Attack("leap", 5f, 1f, 15, 7, 20)));
        //this.goalSelector.addGoal(3, new GruskAttackAIGoal(this));
        //this.goalSelector.addGoal(2, new GruskLeapGoal(this));


       // this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());
      //  this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
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
                setHasHead(false);
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


    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if(this.getRandom().nextFloat() < 0.6f){
            this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STONE_PICKAXE));
        }
        this.setHasHead(true);

        return super.finalizeSpawn(accessor, instance, type, data, tag);
    }

    @Override
    public void tick() {
        super.tick();
        //System.out.println(this.getAnimation());

        if(this.decapitateTimer > 0){

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
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate)
                .triggerableAnim("roar", ROAR)
                .triggerableAnim("decapitate", DECAPITATE)
                .triggerableAnim("leap",LEAP)
                .triggerableAnim("eat",EAT));
    }

    protected PlayState predicate(AnimationState<GruskEntity> state) {
        return state.isMoving() && this.getAnimation().equals("Standby") ? ((state.getAnimatable().entityData.get(state.getAnimatable().DATA_SHARED_FLAGS_ID) & 4) != 0) ? state.setAndContinue(RUN) : state.setAndContinue(WALK) : state.setAndContinue(IDLE);
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
        return SoundsRegistry.GRUSK_AMBIENCE.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_33034_) {
        return SoundsRegistry.GRUSK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundsRegistry.GRUSK_DEATH.get();
    }
}


class GruskRoarGoal extends Goal {
    public GruskEntity entity;
    public int tickTimer = 0;
    GrabAnimation grabAnim = new GrabAnimation();
    protected Vec3 lookVec = new Vec3(0,0,0);

    public GruskRoarGoal(GruskEntity entity){
        this.entity = entity;
        grabAnim.addKeyFrame(0,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(3,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(7,new GrabKeyframe(0, 6.25, -19.75));
        grabAnim.addKeyFrame(10,new GrabKeyframe(0, 17.75, -17.75));
        grabAnim.addKeyFrame(13,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(17,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(20,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(23,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(27,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(30,new GrabKeyframe(2, 25.75, -0.75));
        grabAnim.addKeyFrame(33,new GrabKeyframe(4.75, 26.25, -2.75));
        grabAnim.addKeyFrame(37,new GrabKeyframe(4.75, 21.25, -14.25));
        grabAnim.addKeyFrame(40,new GrabKeyframe(10, 4, -3));
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
        lookVec = grabAnim.getKeyframes().get(0).toWorldCoords(this.entity);
        this.entity.triggerAnim("controller", "eat");
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        this.entity.getLookControl().setLookAt(target);
        if (tickTimer < 40) {

            if(tickTimer % 6 == 0 && tickTimer > 15) this.entity.playSound(SoundEvents.GENERIC_EAT,4.0f,0.9f);

            this.entity.setDeltaMovement(0d,0d,0d);
            if(grabAnim.isOnKeyframe(tickTimer) != null){
                if(target instanceof CapslingEntity){
                    target.noPhysics = true;
                    //target.playSound(SoundEvents.ALLAY_DEATH);
                    if(tickTimer >= 35){
                        target.kill();
                    }
                    if(tickTimer > 2) {
                        ((CapslingEntity) target).triggerAnim("animController", "eaten");
                    }
                }
                target.setDeltaMovement(0d,0d,0d);
                target.setPos(grabAnim.getKeyframes().get(tickTimer).toWorldCoords(this.entity));
            }

            //this.entity.getLookControl().setLookAt(target);
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
        this.entity.roarCoolDown = this.entity.getRandom().nextInt(15) + 15;
        tickTimer = 0;
    }
}

class GruskAttackAIGoal extends Goal{
    public GruskEntity entity;
    public int rePathTime = 0;
    public GruskAttackAIGoal(GruskEntity entity) {
        this.entity = entity;
        setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(entity.getTarget() != null && this.entity.getAnimation().equals("Standby")  && this.entity.decapitateTimer == 0){
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
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() && this.entity.decapitateTimer == 0&& this.entity.getAnimation().equals("Standby") ;
    }

    @Override
    public void tick() {
        super.tick();
        LivingEntity target = this.entity.getTarget();
        this.entity.getLookControl().setLookAt(this.entity.getTarget());
        this.entity.setAggressive(true);
        if(rePathTime > 0){
            rePathTime--;
        }
        else{
            Path path = this.entity.getNavigation().createPath(this.entity.getTarget(), 0);
            this.entity.getNavigation().moveTo(path, 1f);
        }
//        if(this.entity.distanceTo(target) < 15f){
//            if(this.entity.roarCoolDown > 0){
//                this.entity.roarCoolDown--;
//            }
//            else {
//                this.entity.setAnimation("Roar");
//            }
//        }
        if(this.entity.distanceTo(target) < 8f){
            if(this.entity.leapCoolDown > 0){
                this.entity.leapCoolDown--;
            }
            else {
                //this.entity.setAnimation("Leap");
            }
        }
    }

    public void attack(LivingEntity target){
        this.entity.setDeltaMovement(this.entity.position().vectorTo(this.entity.getTarget().position()).normalize().multiply(1.2f,1f,1.2f).add(0d,0.2d,0d));
        this.entity.getLookControl().setLookAt(target);
        if(this.entity.distanceToSqr(target) < 5.5f){
            this.entity.doHurtTarget(target);
        }
    }
}

class GruskLeapGoal extends Goal{
    public GruskEntity entity;
    private int tickTimer = 0;

    public GruskLeapGoal(GruskEntity entity){
        this.entity = entity;
        setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return this.entity.getAnimation().equals("Leap") && this.entity.getTarget() != null && this.entity.getTarget().isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getAnimation().equals("Leap");
    }

    @Override
    public void start() {
        super.start();
        this.entity.triggerAnim("controller","leap");
        LivingEntity target = this.entity.getTarget();
        this.entity.setDeltaMovement(this.entity.position().vectorTo(this.entity.getTarget().position()).normalize().multiply(1.7f,0.6f,1.7f).add(0d,0.2d,0d));
        this.entity.getLookControl().setLookAt(target);
    }

    @Override
    public void tick() {
        super.tick();
        if(tickTimer <= 15){
            if(this.entity.getTarget() != null) {
                LivingEntity target = this.entity.getTarget();
                this.entity.getLookControl().setLookAt(target);
                if(this.entity.distanceTo(target) < 2.5f){
                    this.entity.doHurtTarget(target);
                }
            }
            tickTimer++;
        }
        else{
            //this.entity.setAnimation("Standby");
            this.entity.leapCoolDown = 10 + this.entity.getRandom().nextInt(10);
        }
    }
}

