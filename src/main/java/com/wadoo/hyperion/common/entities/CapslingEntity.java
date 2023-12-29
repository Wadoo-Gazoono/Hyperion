package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.registry.ItemHandler;
import com.wadoo.hyperion.common.registry.SoundsRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CapslingEntity extends Animal implements GeoEntity, Bucketable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);



    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation IDLE_OPEN = RawAnimation.begin().thenLoop("idle_open");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation WALK_OPEN = RawAnimation.begin().thenLoop("walk_open");
    private static final RawAnimation EAT_START = RawAnimation.begin().thenPlay("eat_start");
    private static final RawAnimation EAT_LOOP = RawAnimation.begin().thenLoop("eat_loop");
    private static final RawAnimation EATEN = RawAnimation.begin().thenLoop("eaten");


    private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Predicate<ItemEntity> ALLOWED_ITEMS = (item) -> {
        return !item.hasPickUpDelay() && item.isAlive() && item.onGround();
    };
    public final int ITEMCOOLDOWN = 300;

    public CapslingEntity(EntityType<? extends Animal> animal, Level level) {
        super(animal, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 0.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 0.0F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new CapslingEatGoal(this));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1.0d));
        this.goalSelector.addGoal(6, new FindMagmaCreamGoal(this));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(3, new CapslingTemptGoal(this, 1.0D, Ingredient.of(Items.MAGMA_CREAM), false));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, Player.class, 6F, 1, 1.2));
        this.goalSelector.addGoal(6, new AvoidEntityGoal<>(this, GruskEntity.class, 6F, 1, 1.2));

        this.goalSelector.addGoal(9, new CapslingSocializeGoal(this));

    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OPEN,false);
        this.entityData.define(ANIM_STATE,0);
        this.entityData.define(FROM_BUCKET,false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.fromBucket());
        tag.putBoolean("isOpen", this.getOpen());
        tag.putInt("animState", this.getAnimState());

    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.getBoolean("FromBucket"));
        this.setAnimState(tag.getInt("animState"));
        this.setOpen(tag.getBoolean("isOpen"));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    protected SoundEvent getAmbientSound() {
        if (getOpen()) return SoundsRegistry.CAPSLING_IDLE_PLEAD.get();
        if (getAnimState() == 2) return null;
        return SoundsRegistry.CAPSLING_IDLE.get();
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundsRegistry.CAPSLING_HURT.get();
    }

    protected SoundEvent getDeathSound() {
        return SoundsRegistry.CAPSLING_DEATH.get();
    }

    @Override
    public void tick() {
        super.tick();

        if(this.isInWater()){
            this.setDeltaMovement(this.getDeltaMovement().x,-0.6f,this.getDeltaMovement().y);
        }
        if(getAnimState() == 2){
            if(this.level().isClientSide()) {
                if (random.nextFloat() < 0.11F) {
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
                    }
                }
                if (random.nextFloat() < 0.2F) {
                    for (int i = 0; i < random.nextInt(5) + 2; ++i) {
                        this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
                    }
                }
            }
        }

    }


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.MAGMA_CREAM) && this.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR)){
            this.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.MAGMA_CREAM,1));
            player.getItemInHand(hand).shrink(1);
            return InteractionResult.CONSUME;
        }
        if (player.getItemInHand(hand).is(Items.LAVA_BUCKET)) {
            return this.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }
        return InteractionResult.FAIL;
    }

    public void setOpen(boolean open){
        this.entityData.set(OPEN,open);
    }

    public boolean getOpen(){
        return this.entityData.get(OPEN);
    }

    public void setAnimState(int AnimState){
        this.entityData.set(ANIM_STATE,AnimState);
    }

    public int getAnimState(){
        return this.entityData.get(ANIM_STATE);
    }


    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET,fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag tag) {
        Bucketable.loadDefaultDataFromBucketTag(this, tag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ItemHandler.CAPSLING_BUCKET.get(), 1);
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundsRegistry.CAPSLING_INTO_BUCKET.get();
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player p_148829_, InteractionHand p_148830_, T p_148831_) {
        ItemStack itemstack = p_148829_.getItemInHand(p_148830_);
        if (itemstack.getItem() == Items.LAVA_BUCKET && p_148831_.isAlive()) {
            p_148831_.playSound(p_148831_.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = p_148831_.getBucketItemStack();
            p_148831_.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_148829_, itemstack1, false);
            p_148829_.setItemInHand(p_148830_, itemstack2);
            Level level = p_148831_.level();
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)p_148829_, itemstack1);
            }

            p_148831_.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();

        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "animController", 8, this::predicate)
                .triggerableAnim("eaten",EATEN));
    }



    protected PlayState predicate(AnimationState<CapslingEntity> state) {
        switch(getAnimState()){
            case 0:

                if(state.isMoving()){
                    if(getOpen()){
                        return state.setAndContinue(WALK_OPEN);
                    }
                    return state.setAndContinue(WALK);
                }
                else{
                    if(getOpen()){
                        return state.setAndContinue((IDLE_OPEN));
                    }
                    return state.setAndContinue(IDLE);
                }
            case 1:
                return state.setAndContinue(EAT_START);
            case 2:
                return state.setAndContinue(EAT_LOOP);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return levelReader.getPathfindingCostFromLightLevels(blockPos);
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static boolean checkRules(EntityType<CapslingEntity> p_219129_, LevelAccessor p_219130_, MobSpawnType p_219131_, BlockPos p_219132_, RandomSource p_219133_) {
        return true;
    }

}

class CapslingTemptGoal extends TemptGoal{
    public CapslingEntity entity;
    public CapslingTemptGoal(CapslingEntity mob, double speed, Ingredient ingredient, boolean canScare) {
        super((PathfinderMob) mob, speed, ingredient, canScare);
        this.entity = mob;
    }

    @Override
    public boolean canUse() {
        if(this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR)) {
            return super.canUse();
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.entity.setOpen(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setOpen(false);
    }
}

class CapslingEatGoal extends Goal{
    public CapslingEntity entity;
    public int tickTimer = 0;
    
    public CapslingEatGoal(CapslingEntity entity){
        this.entity = entity;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public boolean canUse() {
        return !this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR) && this.entity.onGround();
    }

    @Override
    public boolean canContinueToUse() {
        return !this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR);
    }

    @Override
    public void start() {
        super.start();
        this.entity.setAnimState(1);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setAnimState(0);
        tickTimer = 0;
    }

    @Override
    public void tick() {
        super.tick();

        this.entity.getNavigation().stop();
        if (tickTimer <= this.entity.ITEMCOOLDOWN){
            tickTimer++;
            if (tickTimer < 30){
                this.entity.setAnimState(1);
                if(tickTimer == 12){
                    this.entity.playSound(SoundsRegistry.CAPSLING_BITE.get(),1,1);
                }
            } else {
                this.entity.setAnimState(2);
            }
            if (tickTimer %  8 == 0 && tickTimer > 45){
                this.entity.playSound(SoundsRegistry.CAPSLING_CHEW.get(),1,1);
            }
            if (tickTimer %  5 == 0 && tickTimer > 45){
                this.entity.playSound(SoundEvents.FIRE_AMBIENT,1,1);
            }
        } else {
            this.entity.playSound(SoundsRegistry.CAPSLING_SPIT.get(),1, 1);
            this.entity.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
            this.entity.level().addFreshEntity(new ItemEntity(this.entity.level(),this.entity.getX(),this.entity.getY(),this.entity.getZ(),new ItemStack(ItemHandler.AGRALITE_SHEET.get(),1)));
        }
    }
}

class FindMagmaCreamGoal extends Goal{
    public CapslingEntity entity;
    public ItemEntity wantedItem;
    public FindMagmaCreamGoal(CapslingEntity entity){
        this.entity = entity;
    }


    @Override
    public boolean canUse() {
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR);
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR) && this.entity.getAnimState() == 0;
                }
            }
        }
        return false;
    }

    @Override
    public void start() {
        super.start();
        this.entity.setOpen(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.setOpen(false);
    }

    @Override
    public void tick() {
        super.tick();
        List<ItemEntity> list = this.entity.level().getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getItem().is(Items.MAGMA_CREAM)){
                wantedItem = list.get(i);
            }
        }



        if(wantedItem != null){
            this.entity.getNavigation().moveTo(wantedItem,1.2f);
            if(this.entity.distanceToSqr(wantedItem) <= 1.85d){
                this.entity.getNavigation().moveTo(wantedItem,1.2f);
                //this.entity.playSound(SoundEvents.STRIDER_EAT);
                this.entity.setItemInHand(InteractionHand.MAIN_HAND,wantedItem.getItem());
                this.wantedItem.getItem().shrink(1);
            }
        }
    }
}

class CapslingSocializeGoal extends Goal{
    public CapslingEntity entity;
    private static final TargetingConditions LEADER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();

    public CapslingSocializeGoal(CapslingEntity entity){
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        List<? extends CapslingEntity> list = this.entity.level().getNearbyEntities(CapslingEntity.class, LEADER_TARGETING, this.entity, this.entity.getBoundingBox().inflate(25.0D));
        return !list.isEmpty() && this.entity.getItemBySlot(EquipmentSlot.MAINHAND).is(Items.AIR);

    }


    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        List<? extends CapslingEntity> list = this.entity.level().getNearbyEntities(CapslingEntity.class, LEADER_TARGETING, this.entity, this.entity.getBoundingBox().inflate(25.0D));
        if(!list.isEmpty()) {
            if(this.entity.getRandom().nextFloat() < 0.01f) {
                this.entity.getNavigation().moveTo(list.get(0), 0.8f);
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
        this.entity.getNavigation().stop();
    }
}

