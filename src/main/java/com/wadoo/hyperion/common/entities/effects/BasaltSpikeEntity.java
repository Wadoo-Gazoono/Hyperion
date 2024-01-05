package com.wadoo.hyperion.common.entities.effects;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class BasaltSpikeEntity extends Entity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation SUMMON = RawAnimation.begin().thenLoop("summon");
    public LivingEntity owner;


    public BasaltSpikeEntity(EntityType<? extends BasaltSpikeEntity> entityType, Level level) {
        super(entityType, level);
    }

    public BasaltSpikeEntity(Level level, LivingEntity owner) {
        super(EntityHandler.BASALT_SPIKE.get(), level);
        this.owner = owner;
    }
    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount == 3){
            for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.5D, 0.0D, 0.5D))) {
                if(!(livingentity instanceof ForgenautEntity)) {
                    if(this.owner != null){
                        livingentity.hurt(this.damageSources().mobAttack(this.owner), 8f);
                        livingentity.setDeltaMovement(livingentity.getDeltaMovement().add(-0.5 + random.nextDouble(),0.9d,-0.5 + random.nextDouble()));
                    }
                    else {
                        livingentity.hurt(this.damageSources().generic(), 8f);
                        livingentity.setDeltaMovement(livingentity.getDeltaMovement().add(-0.5 + random.nextDouble(),0.9d,-0.5 + random.nextDouble()));

                    }
                }
            }
        }
        if(tickCount == 60){
            discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag p_20052_) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag p_20139_) {

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> state.setAndContinue(SUMMON)));
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setOwner(LivingEntity entity){
        this.owner = entity;
    }

}