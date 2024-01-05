package com.wadoo.hyperion.common.entities.fedran;

import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.ai.MMPathNavigatorGround;
import com.wadoo.hyperion.common.entities.ai.SmartBodyHelper;
import com.wadoo.hyperion.common.entities.fedran.attacks.*;
import com.wadoo.hyperion.common.util.verlet.VerletLine;
import com.wadoo.hyperion.common.util.verlet.VerletPoint;
import com.wadoo.hyperion.common.util.verlet.VerletUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

import javax.annotation.Nullable;

public class FedranEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ServerBossEvent bossEvent = (new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS));

    //DATA
    private static final EntityDataAccessor<Integer> ATTACK_COOLDOWN = SynchedEntityData.defineId(FedranEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PHASE = SynchedEntityData.defineId(FedranEntity.class, EntityDataSerializers.INT);

    //MULTIPART
    private final FedranPart[] parts;
    private final FedranPart body;
    private final Vec3 bodyPos = new Vec3(0f,0f,0f);
    private final FedranPart chest;
    private final Vec3 chestPos = new Vec3(0f,1.9375f,0f);
    private final FedranPart leftShoulder;
    private final Vec3 leftShoulderPos = new Vec3(-2.1875f,4.0625f,0.2f);
    private final FedranPart rightShoulder;
    private final Vec3 rightShoulderPos = new Vec3(2.1875f,4.0625f,0.2f);

    //ANIMATIONS
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("WALK");
    private static final RawAnimation IDLE_DEACTIVATED = RawAnimation.begin().thenLoop("IDLE_DEACTIVATED");
    private static final RawAnimation IDLE_ACTIVATED = RawAnimation.begin().thenLoop("IDLE_ACTIVATED");
    private static final RawAnimation FORWARD_JAB = RawAnimation.begin().thenPlay("FJ_FULL_ATTACK");
    private static final RawAnimation HORIZONTAL_SWEEP = RawAnimation.begin().thenPlay("HS_FULL_ATTACK_SHORT");
    private static final RawAnimation STOMP_SHORT = RawAnimation.begin().thenPlay("STOMP_FULL_ATTACK_SHORT");
    private static final RawAnimation SMITHING_SLAM = RawAnimation.begin().thenPlay("SS_FULL_ATTACK");
    private static final RawAnimation STEAM_RELEASE = RawAnimation.begin().thenPlay("STEAM_RELEASE");
    private static final RawAnimation KICK = RawAnimation.begin().thenPlay("KICK");


    //VERLET INTEGRATION
//    private int attackTimer = 0;
//
//    private final int STIFFNESS = 5;
//
//    public VerletPoint leftP1 = new VerletPoint( 0.578, 1.093,0.018,true);
//    public VerletPoint leftP2 = new VerletPoint(0.578, 0.843,0.018,false);
//    public VerletPoint leftP3 = new VerletPoint(0.578,0.718,0.018,false);
//    public VerletPoint leftP4 = new VerletPoint(0.578,0.468,0.018,false);
//    public VerletPoint leftP5 = new VerletPoint(0.578,0.343,0.018,false);
//    public VerletPoint leftP6 = new VerletPoint(0.578,0.093,0.018,false);
//    public VerletPoint leftP7 = new VerletPoint(0.578,-0.031,0.018,false);
//    public VerletPoint leftP8 = new VerletPoint(0.578,-0.281,0.018,false);
//    public VerletPoint leftP9 = new VerletPoint(0.578,-0.406,0.018,false);
//    public VerletPoint leftP10 = new VerletPoint(0.578,-0.656,0.018,false);
//    public VerletPoint leftP11 = new VerletPoint(0.578,-0.906,0.018,false);
//
//    public VerletPoint[] leftPoints = {leftP1,leftP2,leftP3,leftP4,leftP5,leftP6,leftP7,leftP8,leftP9,leftP10,leftP11};
//
//    public VerletLine leftL1 = new VerletLine(leftP1,leftP2,0.312f);
//    public VerletLine leftL2 = new VerletLine(leftP2,leftP3,0.312f);
//    public VerletLine leftL3 = new VerletLine(leftP3,leftP4,0.312f);
//    public VerletLine leftL4 = new VerletLine(leftP4,leftP5,0.312f);
//    public VerletLine leftL5 = new VerletLine(leftP5,leftP6,0.312f);
//    public VerletLine leftL6 = new VerletLine(leftP6,leftP7,0.312f);
//    public VerletLine leftL7 = new VerletLine(leftP7,leftP8,0.312f);
//    public VerletLine leftL8 = new VerletLine(leftP8,leftP9,0.312f);
//    public VerletLine leftL9 = new VerletLine(leftP9,leftP10,0.312f);
//    public VerletLine leftL10 = new VerletLine(leftP10,leftP11,0.312f);
//    public VerletLine[] leftLines = {leftL1,leftL2,leftL3,leftL4,leftL5,leftL6,leftL7,leftL8,leftL9,leftL10};
//
//    public Vec3 currentPos = position();
//    public Vec3 oldPos = currentPos;

    public FedranEntity(EntityType<? extends HyperionLivingEntity> monster, Level level) {
        super(monster, level);
        noCulling = true;
        this.body = new FedranPart(this,"body",1f,1.9375f);
        this.chest = new FedranPart(this,"chest",2.4275f,3.4375f);
        this.leftShoulder = new FedranPart(this,"leftShoulder",1.5f,1.5f);
        this.rightShoulder = new FedranPart(this,"rightShoulder",1.5f,1.5f);
        parts = new FedranPart[]{this.body,this.chest,this.rightShoulder,this.leftShoulder};
    }



    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 600.0D).add(Attributes.FOLLOW_RANGE,80f).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.KNOCKBACK_RESISTANCE, 1.8D).add(Attributes.ATTACK_DAMAGE, 12D);
    }

    public void setPartPosition(FedranPart part,Vec3 vec){
        Vec3 rotatedVec = position().add((new Vec3(vec.x,vec.y,vec.z).yRot((float)Math.toRadians(-this.yBodyRot))));
        part.moveTo(rotatedVec);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (this.hasCustomName()) {
            this.bossEvent.setName(this.getDisplayName());
        }
    }

    @Override
    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        this.bossEvent.setName(this.getDisplayName());
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    protected void doPush(Entity entity) {
    }

    public void updatePartPosition(FedranPart part, Vec3 vec){
        Vec3 rotatedVec = position().add((new Vec3(vec.x,vec.y,vec.z).yRot((float)Math.toRadians(-this.yBodyRot))));
        part.moveTo(rotatedVec);
    }

    @Override
    public void setId(int id) {
        super.setId(id);
        for (int i = 0; i < this.parts.length; i++)
            this.parts[i].setId(id + i + 1);
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        updatePartPosition(this.body,this.bodyPos);
        updatePartPosition(this.chest,this.chestPos);
        updatePartPosition(this.leftShoulder,this.leftShoulderPos);
        updatePartPosition(this.rightShoulder,this.rightShoulderPos);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new FedranAttackAI(this));
        goalSelector.addGoal(2, new ForwardJabAttack(this,2,"FORWARD_JAB",63));
        goalSelector.addGoal(2, new StompAttack(this,3,"STOMP_SHORT",75));
        goalSelector.addGoal(2, new HorizontalSweepAttack(this,4,"HORIZONTAL_SWEEP",100));
        goalSelector.addGoal(2, new SmithingSlamAttack(this,5,"SMITHING_SLAM",130));
        goalSelector.addGoal(2, new SteamReleaseAttack(this,6,"STEAM_RELEASE",160));
        goalSelector.addGoal(2, new KickAttack(this,7,"KICK",62));

        targetSelector.addGoal(1, (new HurtByTargetGoal(this, FedranEntity.class)));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, false));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate).setCustomInstructionKeyframeHandler(this::instructionListener)
                .triggerableAnim("FORWARD_JAB",FORWARD_JAB)
                .triggerableAnim("HORIZONTAL_SWEEP",HORIZONTAL_SWEEP)
                .triggerableAnim("STOMP_SHORT",STOMP_SHORT)
                .triggerableAnim("SMITHING_SLAM",SMITHING_SLAM)
                .triggerableAnim("STEAM_RELEASE",STEAM_RELEASE)
                .triggerableAnim("KICK",KICK));
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
        if(state.isMoving()){
            return state.setAndContinue(WALK);
        }
        return state.setAndContinue(IDLE_ACTIVATED);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (tickCount % 3 == 0){
        setPartPosition(this.body,this.bodyPos);
        setPartPosition(this.chest,this.chestPos);
        setPartPosition(this.leftShoulder,this.leftShoulderPos);
        setPartPosition(this.rightShoulder,this.rightShoulderPos);
        }


        if (getAnimation() == 0){
            if (getAttackCooldown() > 0){
                setAttackCooldown(getAttackCooldown()-1);
            }
        }
        else{
        }



        //VERLET
//        oldPos = currentPos;
//        currentPos = position();
//        Vec3 dist = position().subtract(oldPos).normalize();
//        leftP2.lx += dist.x/30;
//        leftP2.lz += dist.z/30;
//        for(VerletPoint p: leftPoints){
//            VerletUtil.movePoint(p);
//            VerletUtil.constrainToGround(p,position().y());
//        }
//        for (int i = 0; i < STIFFNESS; i++){
//            for (VerletLine l: leftLines){
//                VerletUtil.constrainToLine(l);
//            }
//        }
//        for(VerletPoint p: leftPoints){
//            if (level().isClientSide){
//                Vec3 pos = new Vec3(p.x * 1.2,p.y,p.z * 1.2).yRot((float)Math.toRadians(-this.yBodyRot + 180)).add(position());
//                //System.out.println();
//                //level().addParticle(ParticleTypes.BUBBLE,pos.x,pos.y,pos.z,0d,0d,0d);
//            }
//        }
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.parts;
    }


    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (getAnimation() != 6){
            return super.hurt(source, amount);
        }
        return false;
    }


    private <ENTITY extends GeoEntity> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        if(event.getKeyframeData().getInstructions().contains("boom")){
            if(this.level().isClientSide()){
                for (int i = 0; i < random.nextInt(550) + 350; ++i) {
                    Vec3 partSpawn0 = new Vec3(this.getRandomX(15.5d), this.getY(), this.getRandomZ(15.5d));
                    if(Math.sqrt(this.distanceToSqr(partSpawn0)) < 8.0f) {
                        this.level().addParticle(ParticleTypes.POOF, partSpawn0.x, this.getY(), partSpawn0.z, 0, 0.08d, 0);
                    }
                }
            }
        }
        if(event.getKeyframeData().getInstructions().contains("down")){
            if(this.level().isClientSide()){
                for (int i = 0; i < random.nextInt(250) + 150; ++i) {
                    Vec3 particleSpawn = new Vec3(0D,1.5D,1.2D).yRot((float)Math.toRadians(-this.yBodyRot)).add(this.position());
                    this.level().addParticle(ParticleTypes.POOF, particleSpawn.x, particleSpawn.y, particleSpawn.z, 0, -0.5d, 0);
                }
                Vec3 particleSpawn = new Vec3(0D,0.0D,1.8D).yRot((float)Math.toRadians(-this.yBodyRot)).add(this.position());
                for (int i = 0; i < random.nextInt(550) + 450; ++i) {
                    this.level().addParticle(ParticleTypes.POOF, particleSpawn.x, particleSpawn.y, particleSpawn.z, (random.nextFloat() *2) - 1f, 0, (random.nextFloat() *2) - 1f);
                    this.level().addParticle(ParticleTypes.FLAME, particleSpawn.x, particleSpawn.y, particleSpawn.z, (random.nextFloat() *2) - 1f, 0, (random.nextFloat() *2) - 1f);
                    this.level().addParticle(ParticleTypes.ASH, particleSpawn.x, particleSpawn.y, particleSpawn.z, (random.nextFloat() *2) - 1f, 0, (random.nextFloat() *2) - 1f);
                }
            }
        }
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_COOLDOWN,20);
        this.entityData.define(PHASE,1);
    }

    public void setAttackCooldown(int attack){this.entityData.set(ATTACK_COOLDOWN,attack);}
    public int getAttackCooldown(){return this.entityData.get(ATTACK_COOLDOWN);}
    public void setPhase(int phase){this.entityData.set(PHASE,phase);}
    public int getPhase(){return this.entityData.get(PHASE);}
}


