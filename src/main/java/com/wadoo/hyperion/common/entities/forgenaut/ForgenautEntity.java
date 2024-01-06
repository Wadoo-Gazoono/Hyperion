package com.wadoo.hyperion.common.entities.forgenaut;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedTransitionAttack;
import com.wadoo.hyperion.common.entities.ai.MMPathNavigatorGround;
import com.wadoo.hyperion.common.entities.ai.SmartBodyHelper;
import com.wadoo.hyperion.common.entities.forgenaut.attacks.*;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.example.registry.SoundRegistry;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.ClientUtils;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ForgenautEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);


    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(ForgenautEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_COOLDOWN = SynchedEntityData.defineId(ForgenautEntity.class, EntityDataSerializers.INT);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation MOVE = RawAnimation.begin().thenLoop("move");

    private static final RawAnimation PUNCH_LEFT = RawAnimation.begin().thenPlay("punch_left");
    private static final RawAnimation PUNCH_LEFT_END = RawAnimation.begin().thenPlay("punch_left_end");
    private static final RawAnimation PUNCH_LEFT_TO_DOUBLE_SLAM = RawAnimation.begin().thenPlay("punch_left_to_double_slam");
    private static final RawAnimation PUNCH_LEFT_TO_SLAM_RIGHT = RawAnimation.begin().thenPlay("punch_left_to_slam_right");

    private static final RawAnimation PUNCH_RIGHT = RawAnimation.begin().thenPlay("punch_right");
    private static final RawAnimation PUNCH_RIGHT_END = RawAnimation.begin().thenPlay("punch_right_end");
    private static final RawAnimation PUNCH_RIGHT_TO_DOUBLE_SLAM = RawAnimation.begin().thenPlay("punch_right_to_double_slam");
    private static final RawAnimation PUNCH_RIGHT_TO_SLAM_LEFT = RawAnimation.begin().thenPlay("punch_right_to_slam_left");

    private static final RawAnimation SLAM_LEFT = RawAnimation.begin().thenPlay("slam_left");
    private static final RawAnimation SLAM_RIGHT = RawAnimation.begin().thenPlay("slam_right");

    private static final RawAnimation DOUBLE_SLAM = RawAnimation.begin().thenPlay("double_slam");

    private static final RawAnimation BOOST = RawAnimation.begin().thenPlay("boost");
    private static final RawAnimation BOOST_END = RawAnimation.begin().thenPlay("boost_end");

    private static final RawAnimation BOOST_TO_DOUBLE_SLAM = RawAnimation.begin().thenPlay("boost_to_double_slam");
    private static final RawAnimation BOOST_TO_PUNCH_RIGHT = RawAnimation.begin().thenPlay("boost_to_punch_right");
    private static final RawAnimation BOOST_TO_PUNCH_LEFT = RawAnimation.begin().thenPlay("boost_to_punch_left");

    private static final RawAnimation START_UP_LOOP = RawAnimation.begin().thenPlay("start_up_loop");
    private static final RawAnimation START_UP = RawAnimation.begin().thenPlay("start_up");

    private static final RawAnimation PLAYER_GRAB = RawAnimation.begin().thenPlay("player_grab");
    private static final RawAnimation PLAYER_GRAB_TO_PLAYER_THROW = RawAnimation.begin().thenPlay("player_grab_to_player_throw");
    private static final RawAnimation PLAYER_THROW = RawAnimation.begin().thenPlay("player_throw");

    private static final RawAnimation PLAYER_GRAB_TO_PLAYER_SLAM = RawAnimation.begin().thenPlay("player_grab_to_player_slam");
    private static final RawAnimation PLAYER_SLAM = RawAnimation.begin().thenPlay("player_slam");

    private static final RawAnimation PLAYER_SLAM_TO_PLAYER_THROW = RawAnimation.begin().thenPlay("player_slam_to_player_throw");

    private static final RawAnimation THUNDER_CLAP = RawAnimation.begin().thenPlay("thunder_clap");

    private static final RawAnimation ROCK_THROW = RawAnimation.begin().thenPlay("rock_throw");
    private static final RawAnimation PUNCH_LEFT_TO_SPEAR = RawAnimation.begin().thenPlay("punch_left_to_spear");
    private static final RawAnimation SPEAR = RawAnimation.begin().thenPlay("spear");
    private static final RawAnimation SPEAR_END = RawAnimation.begin().thenPlay("spear_end");
    private static final RawAnimation SPEAR_TO_SLAM_LEFT = RawAnimation.begin().thenPlay("spear_to_slam_left");

    public int stun_timer = 21;

    public ForgenautEntity(EntityType<? extends HyperionLivingEntity> monster, Level level) {
        super(monster, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 300.0D).add(Attributes.FOLLOW_RANGE,80f).add(Attributes.MOVEMENT_SPEED, 0.5F).add(Attributes.KNOCKBACK_RESISTANCE, 1.8D).add(Attributes.ATTACK_DAMAGE, 12D);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new ForgenautAttackAI(this));
        goalSelector.addGoal(2, new FNSpawnAttack(this,1,"start_up",85));
        goalSelector.addGoal(2, new FNPunchAttack(this,2,"punch_left",25));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,1,2,0,"punch_left_end",12, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,2,2,3,"punch_left_to_slam_right",13, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,3,2,6,"punch_left_to_double_slam",13, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,4,2,10,"punch_left_to_spear",15, true));
        goalSelector.addGoal(2, new FNSpearAttack(this,10,"spear",20));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,1,10,0,"spear_end",10, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,2,10,5,"spear_to_slam_left",10, true));

        goalSelector.addGoal(2, new FNSlamRightAttack(this,3,"slam_right",40));
        goalSelector.addGoal(2, new FNPunchAttack(this,4,"punch_right",25));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,1,4,0,"punch_right_end",12, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,2,4,5,"punch_right_to_slam_left",13, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,3,4,6,"punch_right_to_double_slam",13, true));
        goalSelector.addGoal(2, new FNSlamLeftAttack(this,5,"slam_left",40));
        goalSelector.addGoal(2, new FNDoubleSlamAttack(this,6,"double_slam",40));

        goalSelector.addGoal(2, new FNRockThrowAttack(this,7,"rock_throw",60));

        goalSelector.addGoal(2, new FNBoostAttack(this,8,"boost",40));
        goalSelector.addGoal(2, new FNThunderClapAttack(this,9,"thunder_clap",50));


        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, ForgenautEntity.class)).setAlertOthers());

        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, false));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);
        this.entityData.define(ATTACK_COOLDOWN, 0);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate)
                .triggerableAnim("start_up", START_UP)
                .triggerableAnim("punch_right", PUNCH_RIGHT)
                .triggerableAnim("punch_right_end", PUNCH_RIGHT_END)
                .triggerableAnim("punch_right_to_slam_left", PUNCH_RIGHT_TO_SLAM_LEFT)
                .triggerableAnim("punch_right_to_double_slam", PUNCH_RIGHT_TO_DOUBLE_SLAM)
                .triggerableAnim("punch_left", PUNCH_LEFT)
                .triggerableAnim("punch_left_end", PUNCH_LEFT_END)
                .triggerableAnim("punch_left_to_slam_right", PUNCH_LEFT_TO_SLAM_RIGHT)
                .triggerableAnim("punch_left_to_double_slam", PUNCH_LEFT_TO_DOUBLE_SLAM)
                .triggerableAnim("slam_left", SLAM_LEFT)
                .triggerableAnim("slam_right", SLAM_RIGHT)
                .triggerableAnim("double_slam", DOUBLE_SLAM)
                .triggerableAnim("boost", BOOST)
                .triggerableAnim("boost_to_double_slam", BOOST_TO_DOUBLE_SLAM)
                .triggerableAnim("boost_to_punch_right", BOOST_TO_PUNCH_RIGHT)
                .triggerableAnim("boost_to_punch_left", BOOST_TO_PUNCH_LEFT)
                .triggerableAnim("player_grab", PLAYER_GRAB)
                .triggerableAnim("player_grab_to_player_slam", PLAYER_GRAB_TO_PLAYER_SLAM)
                .triggerableAnim("player_grab_to_player_throw", PLAYER_GRAB_TO_PLAYER_THROW)
                .triggerableAnim("player_slam", PLAYER_SLAM)
                .triggerableAnim("player_throw", PLAYER_THROW)
                .triggerableAnim("player_slam_to_player_throw", PLAYER_SLAM_TO_PLAYER_THROW)
                .triggerableAnim("rock_throw", ROCK_THROW)
                .triggerableAnim("boost_end", BOOST_END)
                .triggerableAnim("thunder_clap", THUNDER_CLAP)
                .triggerableAnim("punch_left_to_spear", PUNCH_LEFT_TO_SPEAR)
                .triggerableAnim("spear", SPEAR)
                .triggerableAnim("spear_end", SPEAR_END)
                .triggerableAnim("spear_to_slam_left", SPEAR_TO_SLAM_LEFT)
        );
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new MMPathNavigatorGround(this, pLevel);
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
            freeze(false);
        }
        if (tickCount == 5)getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("controller").transitionLength(5);

    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (getAnimation() == 1) return false;
        return super.hurt(pSource, pAmount);

    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("controller").transitionLength(0);
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

    public void setAttackCooldown(int cool){
        this.entityData.set(ATTACK_COOLDOWN, cool);
    }

    public int getAttackCooldown(){
        return this.entityData.get(ATTACK_COOLDOWN);
    }

}
