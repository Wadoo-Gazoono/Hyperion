package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import com.wadoo.hyperion.common.items.ModuleItem;
import com.wadoo.hyperion.common.network.NetworkHandler;
import com.wadoo.hyperion.common.network.OpenAgolScreenPacket;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.PacketDistributor;
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

public class AbstractAgolEntity extends PathfinderMob implements ContainerListener, HasCustomInventoryScreen, PlayerRideable, OwnableEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(AbstractAgolEntity.class, EntityDataSerializers.STRING);


    private double health = 20;
    private double defense = 20;
    private double damage = 20;
    private int energy_use = 1;
    private int weight = 5;
    public SimpleContainer inventory;
    /*
    sensory,
    basem
    combat,
    upgrade,
    special
     */

    public AbstractAgolEntity(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setAgolType("base");
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        createInventory();
    }

    public AbstractAgolEntity(EntityType<? extends PathfinderMob> mob, Level level, int health, int defense, int damage, int energy_use, int weight, String type) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.health = health;
        this.defense = defense;
        this.damage = damage;
        this.energy_use = energy_use;
        this.weight = weight;
        setAgolType(type);
        createInventory();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("type", getAgolType());
        if (!this.inventory.getItem(2).isEmpty()) {
            System.out.println("added");
            pCompound.put("module", this.inventory.getItem(2).save(new CompoundTag()));

        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setAgolType(pCompound.getString("type"));
        if (pCompound.contains("module", 10)) {
            System.out.println("reading");
            ItemStack itemstack = ItemStack.of(pCompound.getCompound("module"));
            if (itemstack.getItem() instanceof ModuleItem) {
                this.inventory.setItem(2, itemstack);
            }
        }
    }

    @Override
    protected void spawnSprintParticle() {


    }

    private net.minecraftforge.common.util.LazyOptional<?> itemHandler = null;

    @Override
    public <T> net.minecraftforge.common.util.LazyOptional<T> getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @javax.annotation.Nullable net.minecraft.core.Direction facing) {
        if (this.isAlive() && capability == net.minecraftforge.common.capabilities.ForgeCapabilities.ITEM_HANDLER && itemHandler != null)
            return itemHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        if (itemHandler != null) {
            net.minecraftforge.common.util.LazyOptional<?> oldHandler = itemHandler;
            itemHandler = null;
            oldHandler.invalidate();
        }
    }

    protected void createInventory() {
        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            simplecontainer.removeListener(this);
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }

        this.inventory.addListener(this);
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.inventory));
    }


    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, "base");
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "animController", 8, this::predicate));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.health);
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.defense);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.damage);

    }

    protected PlayState predicate(AnimationState<AbstractAgolEntity> state) {
        state.setAndContinue(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(player.getItemInHand(hand).isEmpty() && this instanceof AgolHead){
            player.startRiding(this);
            return InteractionResult.SUCCESS;
        }
        this.openCustomInventoryScreen(player);
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    public void setAgolType(String type){
        this.entityData.set(TYPE, type);
    }

    public String getAgolType(){
        return this.entityData.get(TYPE);
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return null;
    }


    @Override
    public void openCustomInventoryScreen(Player player) {
        if (!this.level().isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (serverPlayer.containerMenu != serverPlayer.inventoryMenu)
                serverPlayer.closeContainer();
            serverPlayer.nextContainerCounter();
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new OpenAgolScreenPacket(serverPlayer.containerCounter, this.getInventorySize(), this.getId()));
            serverPlayer.containerMenu = new AbstractAgolMenu(serverPlayer.containerCounter, serverPlayer.getInventory(), this.inventory, this, player);
            serverPlayer.initMenu(serverPlayer.containerMenu);
            MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverPlayer, serverPlayer.containerMenu));
        }
    }

    public String getAgolName(){
        return "agol_base";
    }


    @Override
    public double m_6048_() {
        return 1f;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if(getRootEntity() != this){
            getRootEntity().hurt(pSource,pAmount);
            return false;
        }
        else {
            for(Entity e: this.getIndirectPassengers()){
                e.animateHurt(1f);
            }
            return super.hurt(pSource, pAmount);
        }
    }

    @Override
    public void die(DamageSource pDamageSource) {
        if(getRootEntity() == this){
            this.getIndirectPassengers().forEach((i) -> i.discard());
        }
        super.die(pDamageSource);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        LivingEntity root = getRootEntity();
        this.setYRot(root.getYRot());
        this.yRotO = getYRot();
        this.yBodyRot= getYRot();
        this.yHeadRot = getYRot();
    }

    public Entity getAboveEntity(){
        if (this.isVehicle()){
            return this.getPassengers().get(0);
        }
        return null;
    }

    public Entity getBelowEntity(){
        if (this.isPassenger()){
            return this.getVehicle();
        }
        return null;
    }

    public Entity getRightEntity(){
        if (this.isPassenger()){
            Entity vehicle = this.getVehicle();
            int index = vehicle.getPassengers().indexOf(this);
            int next_index = index;
            int length = vehicle.getPassengers().size();
            if (length > 1) {
                if (index + 1 == length) {
                    index = 0;
                }
                else{
                    next_index += 1;
                }
                return vehicle.getPassengers().get(next_index);
            }
            return null;
        }
        return null;
    }

    public Entity getLeftEntity(){
        if (this.isPassenger()){
            Entity vehicle = this.getVehicle();
            int index = vehicle.getPassengers().indexOf(this);
            int next_index = index;
            int length = vehicle.getPassengers().size();
            if (length > 1) {
                if (index == 0) {
                    index = length - 1;
                }
                else{
                    next_index -= 1;
                }
                return vehicle.getPassengers().get(next_index);
            }
            return null;
        }
        return null;
    }

    public int getInventorySize() {
        return 12;
    }


    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void containerChanged(Container pContainer) {
        updateParent(pContainer);
    }

    public void updateParent(Container pContainer){
        if (!level().isClientSide) {
            Item slot_item = pContainer.getItem(2).getItem();
            if (slot_item instanceof ModuleItem) {
                if (!isVehicle()){
                    AbstractAgolEntity module = (AbstractAgolEntity) ((ModuleItem) slot_item).getType().create(level());
                    module.moveTo(position());
                    level().addFreshEntity(module);
                    module.startRiding(this);
                }
            }
            else{
                if (isVehicle()){
                    for(Entity e: getPassengers()){
                        if (e instanceof AbstractAgolEntity){
                            ((AbstractAgolEntity) e).destroyAgol();
                        }
                    }
                }
            }
        }
    }

    public void destroyAgol(){
        discard();
        for (Entity e: this.getPassengers()){
            if (e instanceof AbstractAgolEntity) ((AbstractAgolEntity) e).destroyAgol();
        }
        playSound(SoundEvents.ANCIENT_DEBRIS_BREAK);
    }

    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }

    public AbstractAgolEntity getRootEntity(){
        AbstractAgolEntity e = this;
        while (e.isPassenger()){
            e = (AbstractAgolEntity) e.getVehicle();
        }
        return e;
    }

}
