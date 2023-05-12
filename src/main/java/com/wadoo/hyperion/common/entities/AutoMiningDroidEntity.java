package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.swing.plaf.synth.SynthUI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;

public class AutoMiningDroidEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> ANIMSTATE = SynchedEntityData.defineId(AutoMiningDroidEntity.class, EntityDataSerializers.INT);


    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation SWING_RIGHT = RawAnimation.begin().thenPlay("swing_right");
    private static final RawAnimation SWING_LEFT = RawAnimation.begin().thenPlay("swing_left");
    private static final RawAnimation SPIKES_LINE = RawAnimation.begin().thenPlay("spikes_line");
    private static final RawAnimation SPIKES_CIRCLE = RawAnimation.begin().thenPlay("spikes_circle");

    public int swingCoolDown = 30;

    public AutoMiningDroidEntity(EntityType<? extends AutoMiningDroidEntity> monster, Level level) {
        super(monster, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -0.2F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.2F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.2F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 180.0D).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.KNOCKBACK_RESISTANCE, 0.35D).add(Attributes.ATTACK_DAMAGE, 18.5D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMSTATE, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.5d));
        //this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        //this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));

        this.goalSelector.addGoal(52, new AMDAttackChooser(this));
        this.goalSelector.addGoal(4, new AMDSwingGoal(this));


        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));


    }

    @Override
    public void tick() {
        super.tick();
        if(swingCoolDown > 0){
            swingCoolDown--;
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate)
                .triggerableAnim("swing_right", SWING_RIGHT)
                .triggerableAnim("swing_left", SWING_LEFT)
                .triggerableAnim("spikes_line", SPIKES_LINE)
                .triggerableAnim("spikes_circle", SPIKES_CIRCLE));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        return state.isMoving() ? state.setAndContinue(WALK) : state.setAndContinue(IDLE);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void setAnimstate(int animstate){
        this.entityData.set(ANIMSTATE,animstate);
    }

    public int getAnimState(){
        return this.entityData.get(ANIMSTATE);
    }
}

 class AMDAttackChooser extends MeleeAttackGoal {
    private AutoMiningDroidEntity entity;

     @Override
     public boolean canContinueToUse() {
         return super.canContinueToUse() && this.entity.getAnimState() == 0;
     }

     public AMDAttackChooser(AutoMiningDroidEntity entity) {
        super(entity, 1.0d, true);
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if(this.entity.getAnimState() == 0){
            return super.canUse();
        }
        return false;
    }


     @Override
     protected void checkAndPerformAttack(LivingEntity entity, double distance) {
         double d0 = this.getAttackReachSqr(entity);
         if (distance <= d0 && getTicksUntilNextAttack() <= 0) {
             this.resetAttackCooldown();
             this.mob.swing(InteractionHand.MAIN_HAND);
             this.mob.doHurtTarget(entity);
            // this.entity.setAnimstate(1);
         }
     }
 }

class AMDSwingGoal extends Goal{
    public AutoMiningDroidEntity entity;

    public String swing = "";
    public int animTick=0;

    public AMDSwingGoal(AutoMiningDroidEntity entity){
        this.entity = entity;
        setFlags(EnumSet.of(Flag.LOOK,Flag.MOVE));
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.getTarget();
        return target != null && target.isAlive() && this.entity.getAnimState() == 1;
    }

    @Override
    public void start() {
        super.start();
        animTick = 0;
        if(this.entity.getRandom().nextInt(11) > 5){
            swing = "swing_right";
        }
        else{
            swing = "swing_left";
        }
        this.entity.triggerAnim("controller", swing);
    }

    @Override
    public void tick() {
        super.tick();
        if(animTick < 64){
            animTick++;
            this.entity.setDeltaMovement(this.entity.getDeltaMovement().multiply(0d,1d,0d));
            if(animTick == 32){
                //System.out.println(this.entity.getViewVector(1f).dot(this.entity.position().subtract(livingentity.position())) > 0.5f);
                CameraShakeEntity.cameraShake(this.entity.level, this.entity.position(), 25, 0.02f, 20, 10);
                for (LivingEntity livingentity : this.entity.level.getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().move(0d,-1d,0d).inflate(3.5D, 1D, 3.5D).move(0d,-1.4d,0d))) {
                    if(livingentity instanceof AutoMiningDroidEntity == false){
                        this.entity.doHurtTarget(livingentity);
                    }
                }
                this.entity.setDeltaMovement(this.entity.getViewVector(1f).multiply(-1f,-1f,-1f));
            }
        }
        else{
            this.entity.setAnimstate(0);
            this.entity.swingCoolDown = 30;
        }
    }
}
