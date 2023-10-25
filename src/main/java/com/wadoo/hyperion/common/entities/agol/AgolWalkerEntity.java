package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.items.ModuleItem;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class AgolWalkerEntity extends AbstractAgolEntity implements GeoEntity, ContainerListener, HasCustomInventoryScreen, OwnableEntity, PlayerRideable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected SimpleContainer inventory;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    @javax.annotation.Nullable
    private UUID owner;


    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);


    public AgolWalkerEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1.0d));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for(Entity e : this.getIndirectPassengers()){
            if (e instanceof AgolSeatEntity && e.isVehicle()) {
                return (LivingEntity) e.getPassengers().get(0);
            }
        }
        return null;
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ANIM_STATE,0);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("animState", this.getAnimState());

    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setAnimState(tag.getInt("animState"));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 45.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        if(player.zza != 0 || player.xxa != 0){
            this.setRot(player.getYRot(), player.getXRot() * 0.25F);
            this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
            this.maxUpStep = 1;
            this.getNavigation().stop();
            this.setTarget(null);
            this.setSprinting(true);
        }

    }

    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        if (player.zza != 0 || player.xxa != 0) {
            float f = player.xxa * 0.5F;
            float f1 = player.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            return new Vec3((double)f, 0.0D, (double)f1).normalize().multiply(0.2d,0.2d,0.2d);
        }
        return Vec3.ZERO;
    }

    @Override
    protected float getRiddenSpeed(Player p_278286_) {
        return (float) this.getAttributes().getValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).isEmpty()){
            InteractionResult interactionresult = this.interactWithContainerVehicle(player);
            if (interactionresult.consumesAction()) {
                this.gameEvent(GameEvent.CONTAINER_OPEN, player);
            }
        }
        if(player.getItemInHand(hand).getItem() instanceof ModuleItem && !this.isVehicle() && !player.getItemInHand(hand).is(ItemHandler.AGOL_WALKER_MODULE.get())){
            LivingEntity module = (LivingEntity) ((ModuleItem) player.getItemInHand(hand).getItem()).getType().create(this.level());
            module.moveTo(this.position().add(0d,1d,0d));
            this.level().addFreshEntity(module);
            module.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public double getMyRidingOffset() {
        return 0.25d;
    }

    public void setAnimState(int AnimState){
        this.entityData.set(ANIM_STATE,AnimState);
    }

    public int getAnimState(){
        return this.entityData.get(ANIM_STATE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "animController", 8, this::predicate));
    }


    protected PlayState predicate(AnimationState<AgolWalkerEntity> state) {
        if (state.isMoving()) return state.setAndContinue(WALK);
        return state.setAndContinue(IDLE);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        this.getIndirectPassengers().forEach(i -> i.animateHurt(1f));
        return super.hurt(source, amount);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.DONKEY_DEATH;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        //player.openMenu((MenuProvider) this);

        if (!player.level().isClientSide) {
            this.gameEvent(GameEvent.CONTAINER_OPEN, player);
        }
    }

    @Override
    public void die(DamageSource source) {
        if(!level().isClientSide) {
            this.getIndirectPassengers().forEach((i) -> i.kill());
        }
        super.die(source);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return this.owner;
    }

    @Override
    public void slotChanged(AbstractContainerMenu p_39315_, int p_39316_, ItemStack p_39317_) {

    }



    @Override
    public void dataChanged(AbstractContainerMenu p_150524_, int p_150525_, int p_150526_) {

    }

    public InteractionResult interactWithContainerVehicle(Player player) {
        player.openMenu((MenuProvider) this);
        return !player.level().isClientSide ? InteractionResult.CONSUME : InteractionResult.SUCCESS;
    }

    public void setOwnerUUID(@javax.annotation.Nullable UUID p_30587_) {
        this.owner = p_30587_;
    }

    public LivingEntity getTop(){
        return (LivingEntity) this.getPassengers().get(this.getPassengers().size() - 1);
    }
}
