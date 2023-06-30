package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GruskEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenPlay("run");
    private static final RawAnimation LEAP = RawAnimation.begin().thenPlay("leap");
    private static final RawAnimation DECAPITATE = RawAnimation.begin().thenPlay("decapitate");
    private static final RawAnimation ROAR = RawAnimation.begin().thenPlay("roar");
    private static final RawAnimation EAT = RawAnimation.begin().thenPlay("eat");

    private static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(GruskEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HEAD = SynchedEntityData.defineId(GruskEntity.class, EntityDataSerializers.BOOLEAN);

    public int decapitateTimer = 0;

    public GruskEntity(EntityType<? extends HyperionLivingEntity> type, Level level) {
        super(type, level);
    }
//TODO GRUSK DOESNT WORK IF HIT WITH BOW
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, this::predicate)
                .triggerableAnim("idle",IDLE)
                .triggerableAnim("walk",WALK)
                .triggerableAnim("run",RUN)
                .triggerableAnim("leap",LEAP)
                .triggerableAnim("decapitate",DECAPITATE)
                .triggerableAnim("roar",ROAR)
                .triggerableAnim("eat",EAT));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(STATE,0);
        this.entityData.define(HEAD,true);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("state",getState());
        tag.putBoolean("head",hasHead());

    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setState(tag.getInt("state"));
        setHasHead(tag.getBoolean("head"));
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        System.out.println(hasHead());
        if(decapitateTimer > 0){
            return super.hurt(source, amount/3f);
        }
        if(this.getHealth() < this.getMaxHealth()/2f && this.random.nextFloat() < 0.9f && hasHead()){
            setState(4);
            setHasHead(false);
            triggerAnim("controller","decapitate");
            decapitateTimer = 40;
            if(!this.level().isClientSide) {
                GruskHeadEntity head = EntityHandler.GRUSK_HEAD.get().create(this.level());
                head.moveTo(this.position().add(0d,0.7d,0d));
                head.setDeltaMovement(0d,1d,0d);
                head.setOwner(this);
                level().addFreshEntity(head);
            }
        }

        return super.hurt(source, amount);
    }

    protected PlayState predicate(AnimationState<GruskEntity> state) {
        return state.isMoving() && this.getState() == 0 ? (state.setAndContinue(RUN)) : state.setAndContinue(IDLE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new GruskAttackAI(this));
        goalSelector.addGoal(3, new GruskLeapGoal(this,1,"leap",15));
        goalSelector.addGoal(3, new GruskRoarGoal(this,2,"roar",40));
        goalSelector.addGoal(3, new GruskGrabGoal(this,3,"eat",40));

        targetSelector.addGoal(1, (new HurtByTargetGoal(this, CrucibleEntity.class)).setAlertOthers());
        targetSelector.addGoal(1, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());

        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WitherSkeleton.class, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, 0, true, false, null));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {
        super.tick();
        if(decapitateTimer>0){
            decapitateTimer--;
            setDeltaMovement(0d,this.getDeltaMovement().y,0d);
            this.setYRot(this.yRotO);
            this.getNavigation().stop();
        }
        else{
            setState(0);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        setHasHead(true);
        return super.finalizeSpawn(accessor, instance, type, data, tag);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.ATTACK_DAMAGE, 3.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    public int getState(){
        return this.entityData.get(STATE);
    }

    public void setState(int state){
        this.entityData.set(STATE,state);
    }

    public boolean hasHead(){
        return this.entityData.get(HEAD);
    }

    public void setHasHead(boolean head){
        this.entityData.set(HEAD,head);
    }

}
