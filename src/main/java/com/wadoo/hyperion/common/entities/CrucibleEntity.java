package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

import static com.wadoo.hyperion.common.registry.SoundsRegistry.*;

public class CrucibleEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> ANIMSTATE = SynchedEntityData.defineId(CrucibleEntity.class, EntityDataSerializers.INT);
    public int texture_frame = 1;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation SLAM = RawAnimation.begin().thenPlay("slam");
    private static final RawAnimation RELOAD = RawAnimation.begin().thenPlay("reload");
    private static final RawAnimation DIE = RawAnimation.begin().thenPlay("die");

    private static final RawAnimation THROWR = RawAnimation.begin().thenPlay("throwRight");
    private static final RawAnimation THROWL = RawAnimation.begin().thenPlay("throwLeft");

    private static final RawAnimation RIGHTHAND = RawAnimation.begin().thenPlay("rightHand");
    private static final RawAnimation RIGHTFIST = RawAnimation.begin().thenPlay("rightFist");
    private static final RawAnimation LEFTHAND = RawAnimation.begin().thenPlay("leftHand");
    private static final RawAnimation LEFTFIST = RawAnimation.begin().thenPlay("leftFist");

    public int slamCoolDown = 5;
    public int throwCoolDown = 5;
    public CrucibleEntity(EntityType<? extends CrucibleEntity> monster, Level level) {
        super(monster, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1);
        this.setPathfindingMalus(BlockPathTypes.LAVA, -0.2F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.2F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.2F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 160.0D).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.KNOCKBACK_RESISTANCE, 0.8D).add(Attributes.ATTACK_DAMAGE, 12D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIMSTATE, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //TODO make it so goals can work if player goes into creative
        //TODO Add Flags to goals
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8d));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 8));

        this.goalSelector.addGoal(3, new CrucibleSlamGoal(this));
        this.goalSelector.addGoal(4, new CrucibleThrowGoal(this));
        this.goalSelector.addGoal(2, new CrucibleReloadGoal(this));

        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, CrucibleEntity.class)).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, WitherSkeleton.class, true));


    }

    protected SoundEvent getAmbientSound() {
        return CRUCIBLE_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return CRUCIBLE_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return CRUCIBLE_DEATH.get();
    }

    @Override
    public boolean canBeCollidedWith() {
        return this.isAlive();
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("controller").transitionLength(0);

    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount > 2) {
            getAnimatableInstanceCache().getManagerForId(this.getId()).getAnimationControllers().get("controller").transitionLength(5);
        }
            Vec3 projectilePos = new Vec3(0D,0D,2D).yRot((float)Math.toRadians(-getYRot())).add(this.position());
        if(level().isClientSide && this.getRandom().nextFloat() < 0.06f){
            for (int i = 0; i < random.nextInt(8); ++i) {
                this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.25D), this.getY() + 2D, this.getRandomZ(0.25D), 0, 0.1D + this.getRandom().nextFloat()/3, 0);
            }
            this.level().addParticle(ParticleTypes.LAVA, this.getRandomX(0.15D), this.getY() + 2D, this.getRandomZ(0.15D), 0, 0.1D + this.getRandom().nextFloat()/3, 0);
            this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.45D), this.getY() + 2D, this.getRandomZ(0.45D), 0, 0.1D + this.getRandom().nextFloat()/3, 0);

        }

        if(this.isDeadOrDying()){
            triggerAnim("controller","die");
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor accessor, DifficultyInstance instance, MobSpawnType type, @Nullable SpawnGroupData data, @Nullable CompoundTag tag) {
        if(this.getRandom().nextFloat() < 0.6f){
            if(this.getRandom().nextFloat() < 0.6f){
                    if(this.getRandom().nextFloat() < 0.5f) {
                        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.GOLDEN_PICKAXE));

                    }
                    else{
                        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.STONE_PICKAXE));

                    }
                }
            }
            else {
                float rand = this.getRandom().nextFloat();
                if(rand < 0.3f) {
                    this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.OBSIDIAN));
                }
                else if(rand > 0.5f && rand < 0.7f){
                    this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.BLACKSTONE));
                }
                else{
                    this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.NETHERRACK));

                }
            }
        return super.finalizeSpawn(accessor, instance, type, data, tag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).setCustomInstructionKeyframeHandler(this::instructionListener)
                .triggerableAnim("slam", SLAM)
                .triggerableAnim("reload", RELOAD)
                .triggerableAnim("throwRight", THROWR)
                .triggerableAnim("throwLeft", THROWL)
                .triggerableAnim("die", DIE)
        );



        controllers.add(new AnimationController<>(this, "rightArmController", 3, this::rightArm));

        controllers.add(new AnimationController<>(this, "leftArmController", 3, this::leftArm));

    }



    private <ENTITY extends GeoEntity> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        if(event.getKeyframeData().getInstructions().contains("boom")){
            if(this.level().isClientSide()){
                for (int i = 0; i < random.nextInt(550) + 350; ++i) {
                    Vec3 partSpawn0 = new Vec3(this.getRandomX(5.5d), this.getY(), this.getRandomZ(5.5d));
                    if(Math.sqrt(this.distanceToSqr(partSpawn0)) < 8) {
                        this.level().addParticle(ParticleTypes.POOF, partSpawn0.x, this.getY(), partSpawn0.z, 0, 0.08d, 0);
                    }
                }
                for (int i = 0; i < random.nextInt(550) + 330; ++i) {
                    Vec3 partSpawn1 = new Vec3(this.getRandomX(5.5d), this.getY(), this.getRandomZ(5.5d));
                    if (Math.sqrt(this.distanceToSqr(partSpawn1)) < 8.0d) {
                        this.level().addParticle(ParticleTypes.LAVA, partSpawn1.x, this.getY(), partSpawn1.z, 0, 0.08d, 0);
                    }
                    Vec3 partSpawn2 = new Vec3(this.getRandomX(5.5d), this.getY(), this.getRandomZ(5.5d));

                    if (Math.sqrt(this.distanceToSqr(partSpawn2)) < 8.0d) {
                        this.level().addParticle(ParticleTypes.FLAME, partSpawn2.x, this.getY(), partSpawn2.z, 0, 0.08d, 0);
                    }
                    Vec3 partSpawn3= new Vec3(this.getRandomX(5.5d), this.getY(), this.getRandomZ(5.5d));

                    if (Math.sqrt(this.distanceToSqr(partSpawn3)) < 8.0d) {
                        this.level().addParticle(ParticleTypes.WHITE_ASH, partSpawn3.x, this.getY(), partSpawn3.z, 0, 0.08d, 0);
                    }

//                    this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(5.5D), this.getY(), this.getRandomZ(5.5D), 0, 0.08d, 0);
//                    this.level.addParticle(ParticleTypes.LAVA, this.getRandomX(5.5D), this.getY(), this.getRandomZ(5.5D), 0, 0.08d, 0);
//                    this.level.addParticle(ParticleTypes.WHITE_ASH, this.getRandomX(5.5D), this.getY(), this.getRandomZ(5.5D), 0, 2.08d, 0);
                }
            }
        }
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> state) {
        if(!state.isMoving()){
            return state.setAndContinue(IDLE);
        }
        return state.setAndContinue(WALK);
    }

    private <T extends GeoAnimatable> PlayState rightArm(AnimationState<T> state) {
        if(getRightArmed()){
            return state.setAndContinue(RIGHTFIST);
        }
        else {
            return state.setAndContinue(RIGHTHAND);
        }
    }

    private <T extends GeoAnimatable> PlayState leftArm(AnimationState<T> state) {
        if(getLeftArmed()){
            return state.setAndContinue(LEFTFIST);
        }
        else {
            return state.setAndContinue(LEFTHAND);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public boolean getRightArmed(){
        return this.getItemBySlot(EquipmentSlot.MAINHAND).is(ItemHandler.VOLATILE_GOOP.get());
    }

    public boolean getLeftArmed(){
        return this.getItemBySlot(EquipmentSlot.OFFHAND).is(ItemHandler.VOLATILE_GOOP.get());
    }

    public void setAnimstate(int state){
        this.entityData.set(ANIMSTATE,state);
    }

    public int getAnimState(){
        return this.entityData.get(ANIMSTATE);
    }

}

class CrucibleSlamGoal extends Goal {
    public CrucibleEntity entity;
    public int tickTimer = 0;

    public CrucibleSlamGoal(CrucibleEntity entity){
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET,Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(this.entity.slamCoolDown > 0){
            this.entity.slamCoolDown--;
            return false;
        }

        return this.entity.getTarget() != null && this.entity.getTarget().isAlive() &&this.entity.getLeftArmed() && this.entity.getRightArmed()&& this.entity.distanceTo(this.entity.getTarget()) < 5f && this.entity.slamCoolDown <= 0 && (this.entity.getAnimState() == 0 || this.entity.getAnimState() == 2);
    }

    @Override
    public boolean canContinueToUse() {
        return this.tickTimer < 80;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        entity.playSound(CRUCIBLE_SLAM.get(), 1, 1);
        this.entity.triggerAnim("controller", "slam");
        this.entity.setAnimstate(2);
    }

    @Override
    public void tick() {
        super.tick();
        if (tickTimer < 80) {
            if(this.entity.getTarget() != null) {
                this.entity.getLookControl().setLookAt(this.entity.getTarget());
            }
            tickTimer++;
            this.entity.getNavigation().stop();
            this.entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y,0d);
            if(tickTimer == 31){
                CameraShakeEntity.cameraShake(this.entity.level(), this.entity.position(), 45, 0.08f, 30, 20);
                this.entity.playSound(SoundEvents.GENERIC_EXPLODE);
                this.entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.AIR));
                this.entity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(Items.AIR));
                for (LivingEntity livingentity : this.entity.level().getEntitiesOfClass(LivingEntity.class, this.entity.getBoundingBox().move(0d,-1d,0d).inflate(7.5D, 1D, 7.5D).move(0d,-1.4d,0d))) {
                    if(livingentity instanceof CrucibleEntity == false){
                       this.entity.doHurtTarget(livingentity);
                       livingentity.setDeltaMovement(0d,1d,0d);
                    }
                }
            }
        }
        else{
            stop();
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.slamCoolDown = this.entity.getRandom().nextInt(10) + 15;
        this.entity.setAnimstate(0);
        tickTimer = 0;
    }
}

class CrucibleReloadGoal extends Goal{
    public CrucibleEntity entity;
    public int tickTimer = 0;

    public CrucibleReloadGoal(CrucibleEntity entity){
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET, Flag.MOVE));
    }

    public boolean canUse() {
        return this.entity.getTarget() != null && (!this.entity.getLeftArmed() && !this.entity.getRightArmed()) && (this.entity.getAnimState() == 0 || this.entity.getAnimState() == 1);
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        super.start();
        this.entity.triggerAnim("controller","reload");
        this.entity.setAnimstate(1);
    }

    @Override
    public boolean canContinueToUse() {
        return tickTimer < 40;
    }

    @Override
    public void tick() {
        super.tick();
        if (tickTimer < 40) {
            tickTimer++;
            this.entity.getNavigation().stop();
            this.entity.setDeltaMovement(0d,this.entity.getDeltaMovement().y,0d);
            if(tickTimer == 16){
                this.entity.playSound(SoundEvents.BUCKET_EMPTY_LAVA);
                this.entity.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemHandler.VOLATILE_GOOP.get()));
                this.entity.setItemSlot(EquipmentSlot.OFFHAND, new ItemStack(ItemHandler.VOLATILE_GOOP.get()));
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimstate(0);
        tickTimer = 0;
    }
}

class CrucibleThrowGoal extends Goal {
    public CrucibleEntity entity;
    public int tickTimer = 0;
    public int hand = 0;
    public CrucibleThrowGoal(CrucibleEntity entity){
        this.entity = entity;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.TARGET,Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(this.entity.getTarget() == null){
            return false;
        }
        if(this.entity.throwCoolDown > 0){
            this.entity.throwCoolDown--;
            return false;
        }
        if(this.entity.getRightArmed() && this.entity.getLeftArmed()){
            return this.entity.getTarget().isAlive() && (this.entity.getLeftArmed() || this.entity.getLeftArmed()) && this.entity.distanceTo(this.entity.getTarget()) > 5f && this.entity.distanceTo(this.entity.getTarget()) < 15f && this.entity.throwCoolDown <= 0 && this.entity.getAnimState() == 0 || this.entity.getAnimState() == 3;
        }
        return this.entity.getTarget().isAlive() && (this.entity.getLeftArmed() || this.entity.getLeftArmed()) && this.entity.throwCoolDown <= 0 && this.entity.getAnimState() == 0 || this.entity.getAnimState() == 3&& this.entity.distanceTo(this.entity.getTarget()) < 15f;
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
                VolatileGoopProjectile snowball = new VolatileGoopProjectile(this.entity.level(), this.entity);
                double d0 = this.entity.getTarget().getEyeY() - (double)1.1F;
                double d1 = this.entity.getTarget().getX() - this.entity.getX();
                double d2 = d0 - snowball.getY();
                double d3 = this.entity.getTarget().getZ() - this.entity.getZ();
                double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double)0.5F;
                snowball.shoot(d1, d2 + d4, d3, 0.8F, 5);
                if (entity.level().addFreshEntity(snowball)) entity.playSound(CRUCIBLE_THROW.get(), 1, 1);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.throwCoolDown = this.entity.getRandom().nextInt(5) + 15;
        this.entity.setAnimstate(0);
        tickTimer = 0;
        hand = 0;
    }
}