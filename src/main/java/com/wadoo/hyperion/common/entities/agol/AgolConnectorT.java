package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.entities.fedran.FedranPart;
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
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;
import java.util.UUID;

public class AgolConnectorT extends AbstractAgolEntity implements ContainerListener, HasCustomInventoryScreen, PlayerRideable, OwnableEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(AgolConnectorT.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<ItemStack> RIGHT = SynchedEntityData.defineId(AgolConnectorT.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<ItemStack> LEFT = SynchedEntityData.defineId(AgolConnectorT.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Optional<UUID>> LEFTUUID = SynchedEntityData.defineId(AgolConnectorT.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<UUID>> RIGHTUUID = SynchedEntityData.defineId(AgolConnectorT.class, EntityDataSerializers.OPTIONAL_UUID);

    private String gui_name = "agol_connector_t";
    private double health = 20;
    private double defense = 20;
    private double damage = 20;
    private int energy_use = 1;
    private int weight = 5;
    private UUID[] passengers = {null,null};

    //MULTIPART
    private final AgolPart[] parts;
    private final AgolPart right;
    private final Vec3 rightPos = new Vec3(1.125f,0f,0f);
    private final AgolPart left;
    private final Vec3 leftPos = new Vec3(-1.125f,0f,0f);

    public final SimpleContainer inventory = new SimpleContainer(12);
    /*
    sensory,
    basem
    combat,
    upgrade,
    special
     */

    public AgolConnectorT(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setAgolType("base");
        this.right = new AgolPart(this,"right",1f,1f);
        this.left = new AgolPart(this,"left",1f,1f);
        parts = new AgolPart[]{this.left,this.right};
    }

    public AgolConnectorT(EntityType<? extends PathfinderMob> mob, Level level, int health, int defense, int damage, int energy_use, int weight, String type) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.health = health;
        this.defense = defense;
        this.damage = damage;
        this.energy_use = energy_use;
        this.weight = weight;
        setAgolType(type);
        this.right = new AgolPart(this,"right",1f,1f);
        this.left = new AgolPart(this,"left",1f,1f);
        parts = new AgolPart[]{this.left,this.right};
    }

    public void updatePartPosition(AgolPart part, Vec3 vec){
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
    public void aiStep() {
        super.aiStep();
        if (tickCount % 3 == 0) {
            updatePartPosition(this.right, this.rightPos);
            updatePartPosition(this.left, this.leftPos);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    public net.minecraftforge.entity.PartEntity<?>[] getParts() {
        return this.parts;
    }

    @Override
    public boolean isMultipartEntity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, "base");
        this.entityData.define(RIGHT, new ItemStack(Items.AIR));
        this.entityData.define(LEFT, new ItemStack(Items.AIR));

        this.entityData.define(LEFTUUID, Optional.of(this.uuid));
        this.entityData.define(RIGHTUUID, Optional.of(this.uuid));

    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "animController", 8, this::predicate));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        updatePartPosition(this.right,this.rightPos);
        updatePartPosition(this.left,this.leftPos);
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.health);
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.defense);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.damage);

    }

    protected PlayState predicate(AnimationState<AbstractAgolEntity> state) {
        state.setAndContinue(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public String getAgolName(){
        return "agol_connector_t";
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putUUID("right",getRightUUID());
        pCompound.putUUID("left",getLeftUUID());
        if (!this.inventory.getItem(2).isEmpty()) {
            pCompound.put("leftItem", this.inventory.getItem(2).save(new CompoundTag()));

        }
        if (!this.inventory.getItem(3).isEmpty()) {
            pCompound.put("rightItem", this.inventory.getItem(3).save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        setRightUUID(pCompound.getUUID("right"));
        setLeftUUID(pCompound.getUUID("left"));
        if (pCompound.contains("leftItem", 10)) {
            ItemStack itemstack = ItemStack.of(pCompound.getCompound("leftItem"));
            if (itemstack.getItem() instanceof ModuleItem) {
                this.inventory.setItem(2, itemstack);
                setRight(itemstack);
            }
        }
        if (pCompound.contains("rightItem", 10)) {
            ItemStack itemstack = ItemStack.of(pCompound.getCompound("rightItem"));
            if (itemstack.getItem() instanceof ModuleItem) {
                this.inventory.setItem(3, itemstack);
                setLeft(itemstack);
            }
        }
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction moveFunction) {
        if (this.hasPassenger(pPassenger)){
            if(this.getRightUUID().equals(pPassenger.getUUID())){
                Vec3 posVec = (new Vec3(-1.125D, 0.0D, 0.0D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                moveFunction.accept(pPassenger, posVec.x + this.getX(), this.getY() + m_6048_(), posVec.z + this.getZ());
                return;
            }
            if(this.getLeftUUID().equals(pPassenger.getUUID())){
                Vec3 posVec = (new Vec3(1.125D, 0.0D, 0.0D)).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                moveFunction.accept(pPassenger, posVec.x + this.getX(), this.getY() + m_6048_(), posVec.z + this.getZ());
                return;
            }
        }
        super.positionRider(pPassenger,moveFunction);
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
    protected void spawnSprintParticle() {


    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return true;
    }

    @Override
    public void updateParent(Container pContainer) {
        if (!level().isClientSide) {
            Item right = pContainer.getItem(2).getItem();
            if (right instanceof ModuleItem) {
                if (getRight().is(Items.AIR)){
                    System.out.println("hello");
                    AbstractAgolEntity module = (AbstractAgolEntity) ((ModuleItem) right).getType().create(level());
                    module.moveTo(position());
                    level().addFreshEntity(module);
                    module.startRiding(this);
                    setRight(pContainer.getItem(2));
                    setRightUUID(module.getUUID());
                }
            }
            else{
                if (isVehicle()){
                    for(Entity e: getPassengers()){
                        if (e.getUUID().equals(this.getRightUUID())){
                            ((AbstractAgolEntity) e).destroyAgol();
                        }
                    }
                }
            }
            Item left = pContainer.getItem(3).getItem();

            if (left instanceof ModuleItem) {
                if (getLeft().is(Items.AIR)){
                    System.out.println("hello");
                    AbstractAgolEntity module = (AbstractAgolEntity) ((ModuleItem) left).getType().create(level());
                    module.moveTo(position());
                    level().addFreshEntity(module);
                    module.startRiding(this);
                    setLeft(pContainer.getItem(3));
                    setLeftUUID(module.getUUID());
                }
            }
            else{
                if (isVehicle()){
                    for(Entity e: getPassengers()){
                        if (e.getUUID().equals(this.getLeftUUID())){
                            ((AbstractAgolEntity) e).destroyAgol();
                        }
                    }
                }
            }
        }

    }


    @Override
    public double m_6048_() {
        return 1f;
    }

    public void setLeft(ItemStack stack){
        this.entityData.set(LEFT, stack);
    }

    public ItemStack getLeft(){
        return this.entityData.get(LEFT);
    }

    public void setRight(ItemStack stack){
        this.entityData.set(RIGHT, stack);
    }

    public ItemStack getRight(){
        return this.entityData.get(RIGHT);
    }

    public void setRightUUID(UUID id){
        this.entityData.set(RIGHTUUID, Optional.of(id));
    }

    public UUID getRightUUID(){
        return this.entityData.get(RIGHTUUID).get();
    }

    public void setLeftUUID(UUID id){
        this.entityData.set(LEFTUUID, Optional.of(id));
    }

    public UUID getLeftUUID(){
        return this.entityData.get(LEFTUUID).get();
    }

}
