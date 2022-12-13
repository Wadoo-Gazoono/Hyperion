package com.wadoo.hyperion.common.entities;

import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CapslingEntity extends Animal implements IAnimationTickable, IAnimatable, Bucketable {
    private AnimationFactory factory = GeckoLibUtil.createFactory((IAnimatable) this);
    private static final EntityDataAccessor<Boolean> OPEN = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_ITEM = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANIM_STATE = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(CapslingEntity.class, EntityDataSerializers.BOOLEAN);

    public static final Predicate<ItemEntity> ALLOWED_ITEMS = (item) -> {
        return !item.hasPickUpDelay() && item.isAlive() && item.isOnGround();
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
        this.goalSelector.addGoal(9, new CapslingSocializeGoal(this));

    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OPEN,false);
        this.entityData.define(HAS_ITEM,false);
        this.entityData.define(ANIM_STATE,0);
        this.entityData.define(FROM_BUCKET,false);
    }

    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("FromBucket", this.fromBucket());
    }

    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFromBucket(tag.getBoolean("FromBucket"));
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        switch(getAnimState()) {
            case 1:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("eat_start", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            case 2:
                event.getController().setAnimation(new AnimationBuilder().addAnimation("eat_loop", ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
            case 0:
                if (event.isMoving()) {
                    event.getController().setAnimation(new AnimationBuilder().addAnimation("walk" + (getOpen() ? "_open" : ""), ILoopType.EDefaultLoopTypes.LOOP));
                    return PlayState.CONTINUE;
                }
                event.getController().setAnimation(new AnimationBuilder().addAnimation("idle" + (getOpen() ? "_open" : ""), ILoopType.EDefaultLoopTypes.LOOP));
                return PlayState.CONTINUE;
        }
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isInWater()){
            this.setDeltaMovement(this.getDeltaMovement().x,-0.6f,this.getDeltaMovement().y);
        }
        if(getAnimState() == 2){
            if(this.level.isClientSide()) {
                if (random.nextFloat() < 0.11F) {
                    for (int i = 0; i < random.nextInt(2) + 2; ++i) {
                        this.level.addParticle(ParticleTypes.FLAME, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
                    }
                }
                if (random.nextFloat() < 0.2F) {
                    for (int i = 0; i < random.nextInt(5) + 2; ++i) {
                        this.level.addParticle(ParticleTypes.SMOKE, this.getRandomX(0.5D), this.getRandomY() - 0.25D, this.getRandomZ(0.5D), 0, 0.08d, 0);
                    }
                }
            }
        }

    }

    

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<CapslingEntity>(this, "controller", 8, this::predicate));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.getItemInHand(hand).is(Items.MAGMA_CREAM)){
            this.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.MAGMA_CREAM,1));
            player.getItemInHand(hand).shrink(1);
            setHasItem(true);
            return InteractionResult.CONSUME;
        }
        if (player.getItemInHand(hand).is(Items.LAVA_BUCKET)) {
            return this.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }
        return InteractionResult.FAIL;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel p_146743_, AgeableMob p_146744_) {
        return null;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    public AnimationFactory getFactory() {
        return this.factory;
    }

    public void setOpen(boolean open){
        this.entityData.set(OPEN,open);
    }

    public boolean getOpen(){
        return this.entityData.get(OPEN);
    }

    public void setHasItem(boolean hasItem){
        this.entityData.set(HAS_ITEM,hasItem);
    }

    public boolean getHasItem(){
        return this.entityData.get(HAS_ITEM);
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
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    static <T extends LivingEntity & Bucketable> Optional<InteractionResult> bucketMobPickup(Player p_148829_, InteractionHand p_148830_, T p_148831_) {
        ItemStack itemstack = p_148829_.getItemInHand(p_148830_);
        if (itemstack.getItem() == Items.LAVA_BUCKET && p_148831_.isAlive()) {
            p_148831_.playSound(p_148831_.getPickupSound(), 1.0F, 1.0F);
            ItemStack itemstack1 = p_148831_.getBucketItemStack();
            p_148831_.saveToBucketTag(itemstack1);
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, p_148829_, itemstack1, false);
            p_148829_.setItemInHand(p_148830_, itemstack2);
            Level level = p_148831_.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)p_148829_, itemstack1);
            }

            p_148831_.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
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
        if(!this.entity.getHasItem()) {
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
        return this.entity.getHasItem() && this.entity.isOnGround();
    }

    @Override
    public boolean canContinueToUse() {
        return this.entity.getHasItem();
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
            if(tickTimer < 30){
                this.entity.setAnimState(1);
                if(tickTimer == 16){
                    this.entity.playSound(SoundEvents.STRIDER_EAT,1.0f,1.6f);
                }
            }
            else{
                this.entity.setAnimState(2);
            }
            if(tickTimer %  8 == 0 && tickTimer > 45){
                this.entity.playSound(SoundEvents.ANVIL_PLACE,0.1f,2.2f);
            }

        }
        else{
            this.entity.playSound(SoundEvents.ITEM_PICKUP,1.0f, 1.8f);
            this.entity.level.addFreshEntity(new ItemEntity(this.entity.level,this.entity.getX(),this.entity.getY(),this.entity.getZ(),new ItemStack(ItemHandler.AGRALITE_SHEET.get(),1)));
            this.entity.setHasItem(false);
            this.entity.setItemInHand(InteractionHand.MAIN_HAND,ItemStack.EMPTY);
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
        List<ItemEntity> list = this.entity.level.getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return !this.entity.getHasItem();
                }
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        List<ItemEntity> list = this.entity.level.getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        if(!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getItem().is(Items.MAGMA_CREAM)) {
                    return !this.entity.getHasItem() && this.entity.getAnimState() == 0;
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
        List<ItemEntity> list = this.entity.level.getEntitiesOfClass(ItemEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), CapslingEntity.ALLOWED_ITEMS);
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getItem().is(Items.MAGMA_CREAM)){
                wantedItem = list.get(i);
            }
        }



        if(wantedItem != null){
            this.entity.getNavigation().moveTo(wantedItem,1.2f);
            if(this.entity.distanceToSqr(wantedItem) <= 1.85d){
                this.entity.getNavigation().moveTo(wantedItem,1.2f);
                this.entity.playSound(SoundEvents.STRIDER_EAT);
                this.entity.setItemInHand(InteractionHand.MAIN_HAND,wantedItem.getItem());
                this.entity.setHasItem(true);
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
        List<? extends CapslingEntity> list = this.entity.level.getNearbyEntities(CapslingEntity.class, LEADER_TARGETING, this.entity, this.entity.getBoundingBox().inflate(25.0D));
        return !list.isEmpty() && !this.entity.getHasItem();

    }


    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    @Override
    public void tick() {
        List<? extends CapslingEntity> list = this.entity.level.getNearbyEntities(CapslingEntity.class, LEADER_TARGETING, this.entity, this.entity.getBoundingBox().inflate(25.0D));
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

