package com.wadoo.hyperion.common.entities.forgenaut;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.forgenaut.attacks.ForgenautPunchAttack;
import com.wadoo.hyperion.common.entities.forgenaut.attacks.ForgenautSpawnAttack;
import com.wadoo.hyperion.common.entities.forgenaut.attacks.ForgenautSlamAttack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ForgenautEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(ForgenautEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_ARM = SynchedEntityData.defineId(ForgenautEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ATTACK_COOLDOWN = SynchedEntityData.defineId(ForgenautEntity.class, EntityDataSerializers.INT);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");

    private static final RawAnimation STUN = RawAnimation.begin().thenPlay("stun");
    private static final RawAnimation GRAB = RawAnimation.begin().thenPlay("grab");
    private static final RawAnimation FLAMETHROWER = RawAnimation.begin().thenPlay("flamethrower");
    private static final RawAnimation PUNCH_LEFT = RawAnimation.begin().thenPlay("punch_left");
    private static final RawAnimation PUNCH_RIGHT = RawAnimation.begin().thenPlay("punch_right");
    private static final RawAnimation SLAM = RawAnimation.begin().thenPlay("slam");
    private static final RawAnimation SPINNING_SLAM = RawAnimation.begin().thenPlay("spinningSlam");
    private static final RawAnimation DEATH = RawAnimation.begin().thenPlayAndHold("death");
    private static final RawAnimation START_UP_LOOP = RawAnimation.begin().thenLoop("start_up_loop");
    private static final RawAnimation START_UP = RawAnimation.begin().thenPlay("start_up");

    public ForgenautEntity(EntityType<? extends HyperionLivingEntity> monster, Level level) {
        super(monster, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 200.0D).add(Attributes.FOLLOW_RANGE,80f).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.KNOCKBACK_RESISTANCE, 1.8D).add(Attributes.ATTACK_DAMAGE, 12D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new ForgenautAttackAI(this));
        goalSelector.addGoal(2, new ForgenautSpawnAttack(this,1,"start_up",85));
        goalSelector.addGoal(2, new ForgenautPunchAttack(this,2,"punch_left",30));
        goalSelector.addGoal(2, new ForgenautPunchAttack(this,3,"punch_right",30));
        goalSelector.addGoal(2, new ForgenautSlamAttack(this,4,"slam",52));

        targetSelector.addGoal(1, (new HurtByTargetGoal(this, ForgenautEntity.class)));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);
        this.entityData.define(ATTACK_COOLDOWN, 0);

        this.entityData.define(HAS_ARM, true);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate)
                .triggerableAnim("start_up", START_UP)
                .triggerableAnim("punch_right", PUNCH_RIGHT)
                .triggerableAnim("punch_left", PUNCH_LEFT)
                .triggerableAnim("slam", SLAM));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if(getPhase() == 0){
            return state.setAndContinue(START_UP_LOOP);
        }
        if(state.isMoving()){
            return state.setAndContinue(MOVE);
        }
        return state.setAndContinue(IDLE);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(getPhase() == 0){
            this.getNavigation().stop();
            this.setDeltaMovement(0d, this.getDeltaMovement().y, 0d);
        }
        if (getAnimation() == 0){
            if (getAttackCooldown() > 0){
                setAttackCooldown(getAttackCooldown()-1);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if(getHealth() - amount < getMaxHealth() / 2){
            //TODO Lose arm
        }
        return super.hurt(source, amount);

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setPhase(int phase){
        this.entityData.set(PHASE,phase);
    }

    public int getPhase(){
        return this.entityData.get(PHASE);
    }

    public void setAttackCooldown(int attack){this.entityData.set(ATTACK_COOLDOWN,attack);}
    public int getAttackCooldown(){return this.entityData.get(ATTACK_COOLDOWN);}
}
