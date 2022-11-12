package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
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
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    public int wakeup_timer = 0;
    public int ticks_without_target = 0;


    public GruskEntity(EntityType<? extends Monster> monster, Level level) {
        super(monster, level);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));
        this.goalSelector.addGoal(5,new RandomStrollGoal(this,1.0f));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE,0);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch (getAnimState()) {
            case 0:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_deactivated", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            case 1:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("roar", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            case 2:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle_activated", ILoopType.EDefaultLoopTypes.LOOP));
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
        System.out.println(this.wakeup_timer);
        if(getTarget() != null && distanceTo(getTarget()) < 10f){
            ticks_without_target = 0;
            setAnimState(1);
            if (this.wakeup_timer < 40) {
                getLookControl().setLookAt(getTarget());
                this.wakeup_timer++;
                if (this.wakeup_timer == 7) {
                    playSound(SoundEvents.RAVAGER_ROAR);
                    for (LivingEntity livingentity : this.level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(10.2D, 1.0D, 10.2D))) {
                        if(livingentity instanceof GruskEntity == false){
                            livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, true));
                        }
                    }
                }
            }
            else{
                setAnimState(2);
            }
        }
        else{
            ticks_without_target++;
        }

        if(ticks_without_target > 200){
            this.getNavigation().stop();
            setTarget(null);
            ticks_without_target = 0;
            this.wakeup_timer = 0;
            setAnimState(0);
        }

    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<GruskEntity>(this, "controller", 8, this::predicate));
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
}
