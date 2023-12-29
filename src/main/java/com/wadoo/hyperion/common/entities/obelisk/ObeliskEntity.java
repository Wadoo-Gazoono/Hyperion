package com.wadoo.hyperion.common.entities.obelisk;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.MinecartSpawner;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ObeliskEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation SITTING_IDLE = RawAnimation.begin().thenPlay("sit_idle");
    private static final RawAnimation SIT = RawAnimation.begin().thenPlay("sit_down");
    private static final RawAnimation STAND = RawAnimation.begin().thenPlay("stand_up");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");
    private static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("spawn");
    private static final RawAnimation BLANK = RawAnimation.begin().thenPlay("blank");

    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(ObeliskEntity.class, EntityDataSerializers.BOOLEAN);


    public ObeliskEntity(EntityType<? extends HyperionLivingEntity> type, Level level) {
        super(type, level);

    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SITTING, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, this::predicate)
                .triggerableAnim("sit",SIT)
                .triggerableAnim("stand",STAND));
        controllers.add(new AnimationController<>(this, "spawnController", 2,  animationState -> (animationState.setAndContinue(BLANK)))
                .triggerableAnim("spawn", SPAWN));
    }

    protected SoundEvent getAmbientSound() {
        return SoundsRegistry.GRUSK_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsRegistry.GRUSK_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundsRegistry.GRUSK_DEATH.get();
    }


    protected PlayState predicate(AnimationState<ObeliskEntity> state) {
        return state.getLimbSwingAmount() > 0.05f ? (state.setAndContinue(WALK)) :
                (getSitting() ? state.setAndContinue(SITTING_IDLE) : state.setAndContinue(IDLE));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new ObeliskAttackAI(this));
        goalSelector.addGoal(0, new ObeliskSitDownGoal(this, 1, "sit", 35));
        goalSelector.addGoal(0, new ObeliskStandUpGoal(this, 2, "stand", 25));

        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, 0, true, false, null));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void tick() {
        super.tick();
        if (getSitting()){
            this.yBodyRotO = Math.round(this.yBodyRot / 90) *90;
            this.setYRot(this.yBodyRotO);
            this.setYHeadRot((this.yBodyRotO));
            this.setYBodyRot((this.yBodyRotO));
            this.getNavigation().stop();
            this.setDeltaMovement(0d, this.getDeltaMovement().y, 0d);
            if (tickCount % (80) == 0){
                triggerAnim("spawnController", "spawn");
                spawnCubes();
            }

            if (level().isClientSide){
                float size = 1.25f;
                Vec3 particlePos = position().add(Math.sin(tickCount / 4f) * size, 1.5f, Math.cos(tickCount / 4f) * size);
                level().addParticle(ParticleTypes.FLAME, particlePos.x, particlePos.y, particlePos.z, 0d, 0d, 0d);


            }
        }
    }

    public void spawnCubes(){
        for (int i = 0; i < random.nextInt(3); i++){

            MagmaCube mob = EntityType.MAGMA_CUBE.create(level());
            mob.setSize(random.nextInt(1,3),true);
            Vec3 pos = position().add(this.random.nextInt(8) - 4, 1d,this.random.nextInt(8) - 4);
            mob.moveTo(pos);
            level().addFreshEntity(mob);

        }
    }


    @Override
    public void push(double pX, double pY, double pZ) {
        if (getSitting()) return;
        super.push(pX, pY, pZ);
    }

    public void sit(){
        this.setSitting(true);
        this.yBodyRotO = Math.round(this.yBodyRot / 90) *90;
    }

    public void stand(){
        this.setSitting(false);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.1F)
                .add(Attributes.ATTACK_DAMAGE, 0.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }


    public void setSitting(boolean sit){
        this.entityData.set(SITTING, sit);
    }

    public boolean getSitting(){
        return this.entityData.get(SITTING);
    }
}
