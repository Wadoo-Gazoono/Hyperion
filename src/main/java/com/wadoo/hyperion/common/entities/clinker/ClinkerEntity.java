package com.wadoo.hyperion.common.entities.clinker;


import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.clinker.attacks.ClinkerLeapAttack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ClinkerEntity extends HyperionLivingEntity implements GeoEntity, TraceableEntity {
    private static final TargetingConditions FIND_OBELISK = TargetingConditions.forNonCombat().range(12.0D);
    private static final EntityDataAccessor<Vector3f> SPAWNPOS = SynchedEntityData.defineId(ClinkerEntity.class, EntityDataSerializers.VECTOR3);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @javax.annotation.Nullable
    Mob owner;

    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlay("death");
    private static final RawAnimation CHARGE = RawAnimation.begin().thenPlay("charge");
    private static final RawAnimation WALK = RawAnimation.begin().thenPlay("walk");


    public ClinkerEntity(EntityType<? extends HyperionLivingEntity> type, Level level) {
        super(type, level);

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SPAWNPOS, new Vector3f());
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, animationState -> (
                animationState.isMoving() ? animationState.setAndContinue(WALK) : animationState.setAndContinue(IDLE)
                ))
                .triggerableAnim("death",DEATH)
                .triggerableAnim("charge",CHARGE));

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new ClinkerLeapAttack(this, 1, "charge", 11));
        goalSelector.addGoal(0, new ClinkerAttackAI(this));
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
        if (getOwner() != null) setSpawnpos(getOwner().position().add(0d,1.5d,0d));
        if (tickCount > 200 && getTarget() == null) {
            kill();
        }
        if (this.isDeadOrDying()) {
            if (level().isClientSide){
                level().addParticle(
                        ParticleTypes.LAVA,
                        position().x,
                        position().y,
                        position().z,
                        0d,
                        0d,0d
                );
            }
            triggerAnim("controller", "death");}
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18F)
                .add(Attributes.ATTACK_DAMAGE, 1.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.0D);
    }

    @Nullable
    @Override
    public Entity getOwner() {
        return this.owner;
    }

    public void setOwner(Mob pOwner) {
        this.owner = pOwner;
    }

    public void setSpawnpos(Vec3 spawnpos){
        this.entityData.set(SPAWNPOS, new Vector3f((float) spawnpos.x,(float) spawnpos.y, (float)spawnpos.z));
    }

    public Vec3 getSpawnPos(){
        Vector3f vec = this.entityData.get(SPAWNPOS);
        return new Vec3(vec.x, vec.y, vec.z);
    }

}
