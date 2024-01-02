package com.wadoo.hyperion.common.entities.forgenaut;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.AnimatedTransitionAttack;
import com.wadoo.hyperion.common.entities.forgenaut.attacks.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
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
    private static final RawAnimation BOOST_TO_DOUBLE_SLAM = RawAnimation.begin().thenPlay("boost_to_double_slam");

    private static final RawAnimation START_UP_LOOP = RawAnimation.begin().thenPlay("start_up_loop");
    private static final RawAnimation START_UP = RawAnimation.begin().thenPlay("start_up");



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
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,1,2,0,"punch_left_end",10, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,2,2,3,"punch_left_to_slam_right",13, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,3,2,6,"punch_left_to_double_slam",13, true));
        goalSelector.addGoal(2, new FNSlamRightAttack(this,3,"slam_right",40));
        goalSelector.addGoal(2, new FNPunchAttack(this,4,"punch_right",25));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,1,4,0,"punch_right_end",10, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,2,4,5,"punch_right_to_slam_left",13, true));
        goalSelector.addGoal(2, new AnimatedTransitionAttack(this,3,4,6,"punch_right_to_double_slam",13, true));
        goalSelector.addGoal(2, new FNSlamLeftAttack(this,5,"slam_left",40));
        goalSelector.addGoal(2, new FNDoubleSlamAttack(this,6,"double_slam",40));

        targetSelector.addGoal(1, (new HurtByTargetGoal(this, ForgenautEntity.class)));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(PHASE, 0);

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
        );
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
            freeze();
        }

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

}
