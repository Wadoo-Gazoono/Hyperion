package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class AutoMiningDroidEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> ANIMSTATE = SynchedEntityData.defineId(AutoMiningDroidEntity.class, EntityDataSerializers.INT);


    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation SWING_RIGHT = RawAnimation.begin().thenLoop("swing_right");
    private static final RawAnimation SWING_LEFT = RawAnimation.begin().thenLoop("swing_left");
    private static final RawAnimation SPIKES_LINE = RawAnimation.begin().thenLoop("spikes_line");
    private static final RawAnimation SPIKES_CIRCLE = RawAnimation.begin().thenLoop("spikes_circle");


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
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.5d));
        //this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        //this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));



        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, GruskEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, true));


    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
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

class AMDSWINGGOAL extends Goal {
    public AutoMiningDroidEntity entity;
    public int tickTimer = 0;
    public int hand = 0;
    public Path path;
    public AMDSWINGGOAL(AutoMiningDroidEntity entity){
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET,Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.entity.getTarget();
        return false;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && this.tickTimer < 40 && (this.entity.getAnimState() == 0 || this.entity.getAnimState() == 3);
    }

    @Override
    public void start() {
        super.start();
        this.entity.setAnimstate(3);
        if(this.entity.getItemBySlot(EquipmentSlot.OFFHAND).is(ItemHandler.VOLATILE_GOOP.get())) {
            hand = 2;
            this.entity.triggerAnim("controller", "throwLeft");
        }
        if(this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemHandler.VOLATILE_GOOP.get())){
            hand = 1;
            this.entity.triggerAnim("controller", "throwRight");
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.entity.getLookControl().setLookAt(this.entity.getTarget());
        if (tickTimer < 40) {
            this.entity.getLookControl().setLookAt(this.entity.getTarget());
            tickTimer++;
            this.entity.getNavigation().stop();
            this.entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y,0d);
            if(tickTimer == 17){
                if(hand == 1){
                    this.entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
                }
                if (hand == 2){
                    this.entity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AIR));
                }
                VolatileGoopProjectile snowball = new VolatileGoopProjectile(this.entity.level, this.entity);
                double d0 = this.entity.getTarget().getEyeY() - (double)1.1F;
                double d1 = this.entity.getTarget().getX() - this.entity.getX();
                double d2 = d0 - snowball.getY();
                double d3 = this.entity.getTarget().getZ() - this.entity.getZ();
                double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.5F;
                snowball.shoot(d1, d2 + d4, d3, 0.8F, 5.0F);
                this.entity.playSound(SoundEvents.LAVA_POP, 1.0F, 0.4F / (this.entity.getRandom().nextFloat() * 0.4F + 0.8F));
                this.entity.level.addFreshEntity(snowball);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        tickTimer = 0;
        hand = 0;
    }
}

