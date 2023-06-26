package com.wadoo.hyperion.common.entities.fedran;

import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import com.wadoo.hyperion.common.entities.fedran.attacks.ForwardJabAttack;
import com.wadoo.hyperion.common.entities.fedran.attacks.HorizontalSweepAttack;
import com.wadoo.hyperion.common.entities.fedran.attacks.SmithingSlamAttack;
import com.wadoo.hyperion.common.entities.fedran.attacks.StompAttack;
import com.wadoo.hyperion.common.entities.grusk.GruskAttackAI;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskLeapGoal;
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
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.boss.enderdragon.phases.EnderDragonPhase;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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
    private final FedranPart[] parts;
    private final FedranPart body;
    private final Vec3 bodyPos = new Vec3(0f,0f,0f);
    private final FedranPart chest;
    private final Vec3 chestPos = new Vec3(0f,1.9375f,0f);
    private final FedranPart leftShoulder;
    private final Vec3 leftShoulderPos = new Vec3(-2.1875f,4.0625f,0.2f);
    private final FedranPart rightShoulder;
    private final Vec3 rightShoulderPos = new Vec3(2.1875f,4.0625f,0.2f);


    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("WALK");
    private static final RawAnimation IDLE_DEACTIVATED = RawAnimation.begin().thenLoop("IDLE_DEACTIVATED");
    private static final RawAnimation IDLE_ACTIVATED = RawAnimation.begin().thenLoop("IDLE_ACTIVATED");
    private static final RawAnimation FORWARD_JAB = RawAnimation.begin().thenPlay("FJ_FULL_ATTACK");
    private static final RawAnimation HORIZONTAL_SWEEP = RawAnimation.begin().thenPlay("HS_FULL_ATTACK_SHORT");
    private static final RawAnimation STOMP_SHORT = RawAnimation.begin().thenPlay("STOMP_FULL_ATTACK_SHORT");
    private static final RawAnimation SMITHING_SLAM = RawAnimation.begin().thenPlay("SS_FULL_ATTACK");



    public FedranEntity(EntityType<? extends HyperionLivingEntity> monster, Level level) {
        super(monster, level);
        this.body = new FedranPart(this,"body",1f,1.9375f);
        this.chest = new FedranPart(this,"chest",2.4275f,3.4375f);
        this.leftShoulder = new FedranPart(this,"leftShoulder",1.5f,1.5f);
        this.rightShoulder = new FedranPart(this,"rightShoulder",1.5f,1.5f);
        parts = new FedranPart[]{this.body,this.chest,this.rightShoulder,this.leftShoulder};
    }



    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 600.0D).add(Attributes.MOVEMENT_SPEED, 0.35F).add(Attributes.KNOCKBACK_RESISTANCE, 1.8D).add(Attributes.ATTACK_DAMAGE, 12D);
    }

    public void setPartPosition(FedranPart part,Vec3 vec){
        Vec3 rotatedVec = position().add((new Vec3(vec.x,vec.y,vec.z).yRot((float)Math.toRadians(-getYRot()))));
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
        Vec3 rotatedVec = position().add((new Vec3(vec.x,vec.y,vec.z).yRot((float)Math.toRadians(-getYRot()))));
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



        targetSelector.addGoal(1, (new HurtByTargetGoal(this, FedranEntity.class)));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }



    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate).setCustomInstructionKeyframeHandler(this::instructionListener)
                .triggerableAnim("FORWARD_JAB",FORWARD_JAB)
                .triggerableAnim("HORIZONTAL_SWEEP",HORIZONTAL_SWEEP)
                .triggerableAnim("STOMP_SHORT",STOMP_SHORT)
                .triggerableAnim("SMITHING_SLAM",SMITHING_SLAM));
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
        updatePartPosition(this.body,this.bodyPos);
        setPartPosition(this.chest,this.chestPos);
        setPartPosition(this.leftShoulder,this.leftShoulderPos);
        setPartPosition(this.rightShoulder,this.rightShoulderPos);
        }
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
        if(!this.level.isClientSide()){
            return super.hurt(source, amount);
        }
        return false;
    }


    private <ENTITY extends GeoEntity> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {
        System.out.println(1);
        if(event.getKeyframeData().getInstructions().contains("boom")){
            if(this.level.isClientSide()){
                for (int i = 0; i < random.nextInt(550) + 350; ++i) {
                    Vec3 partSpawn0 = new Vec3(this.getRandomX(15.5d), this.getY(), this.getRandomZ(15.5d));
                    if(Math.sqrt(this.distanceToSqr(partSpawn0)) < 8.0f) {
                        this.level.addParticle(ParticleTypes.POOF, partSpawn0.x, this.getY(), partSpawn0.z, 0, 0.08d, 0);
                    }
                }
            }
        }
    }
}


