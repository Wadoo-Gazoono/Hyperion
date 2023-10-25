package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.items.ModuleItem;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class AgolModuleEntity extends AbstractAgolEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");


    enum TYPE {
        SENSORY,
        COMBAT,
        UPGRADE,
        SPECIAL
    }
    public int height;
    public int energyCost;

    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(AgolModuleEntity.class, EntityDataSerializers.INT);


    public AgolModuleEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, LivingEntity.class, 10.0F));
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
        return createMobAttributes().add(Attributes.MAX_HEALTH, 45.0D).add(Attributes.MOVEMENT_SPEED, 0.2F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getVehicle() != null){
            LivingEntity root = (LivingEntity) this.getRootVehicle();
            this.setYRot(root.getYRot());
            this.yRotO = getYRot();
            this.yBodyRot= getYRot();
            this.yHeadRot = getYRot();
        }
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


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
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
    public boolean canBeLeashed(Player p_21418_) {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))return super.hurt(source,amount);
        if(this.isPassenger())return this.getRootVehicle().hurt(source,amount);
        return super.hurt(source,amount);
    }

    @Override
    public double getMyRidingOffset() {
        return 0.4d;
    }

    protected PlayState predicate(AnimationState<AgolModuleEntity> state) {
        return state.setAndContinue(IDLE);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


}
