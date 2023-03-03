package com.wadoo.hyperion.common.entities;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation ROAR = RawAnimation.begin().thenPlay("roar");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation DECAPITATE = RawAnimation.begin().thenPlay("decapitate");
    private static final RawAnimation LEAP = RawAnimation.begin().thenPlay("leap");

    public int roarCoolDown = 100;

    public GruskEntity(EntityType<? extends GruskEntity> monster, Level level) {
        super(monster, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -0.2F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.2F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.2F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 32.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.4D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8d));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.goalSelector.addGoal(10, new GruskRoarGoal(this));


        this.targetSelector.addGoal(2, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));


    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 8, this::predicate).triggerableAnim("roar", ROAR).triggerableAnim("decapitate", DECAPITATE));
    }

    protected PlayState predicate(AnimationState<GruskEntity> state) {
        return state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
        System.out.println(this.entity.roarCoolDown);
        if(this.entity.roarCoolDown <= 0){
            return this.entity.getTarget() != null;
        }
        else{
            this.entity.roarCoolDown--;
            return false;
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.entity.getTarget() != null && tickTimer < 40;
    }

    @Override
    public void start() {
        super.start();
        this.entity.triggerAnim("controller", "roar");
    }

    @Override
    public void tick() {
        super.tick();
        if (tickTimer < 40) {
            tickTimer++;
            this.entity.getNavigation().stop();
            if(tickTimer == 5){
                this.entity.playSound(SoundEvents.RAVAGER_ROAR);
                for (LivingEntity livingentity : this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().inflate(10.2D, 1.0D, 10.2D))) {
                    if(livingentity instanceof GruskEntity == false){
                        livingentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1, false, true));
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        tickTimer = 40;
        this.entity.roarCoolDown = this.entity.getRandom().nextInt(200) + 100;
    }
}