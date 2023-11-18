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
    }



    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
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
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.health);
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.defense);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.damage);

    }

    protected PlayState predicate(AnimationState<AbstractAgolEntity> state) {
        state.setAndContinue(IDLE);
        return PlayState.CONTINUE;
    }


    public String getAgolName(){
        return "agol_connector_t";
    }

    @Override
    protected void positionRider(Entity pPassenger, MoveFunction moveFunction) {
        if (!this.hasPassenger(pPassenger)) return;
        Vec3 leftVec = new Vec3(-1.125D, 0D, 0D).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
        Vec3 rightVec = new Vec3(1.125D, 0D, 0D).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
        for (Entity entity : getPassengers()){
            if (entity.getUUID().equals(passengers[0])){
                pPassenger.setPos(position().x + leftVec.x, position().y + 1d, position().z + leftVec.z);
                return;
            }
            if (entity.getUUID().equals(passengers[1])) {
                pPassenger.setPos(position().x + rightVec.x, position().y + 1d, position().z + rightVec.z);
                return;
                }
            }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide) System.out.println(getLeft());
        passengers[0] = getLeftUUID();
        passengers[1] = getRightUUID();
    }

    @Override
    public void updateParent(Container pContainer){
        Item left = this.getInventory().getItem(2).getItem();
        Item right = this.getInventory().getItem(3).getItem();
        if (left instanceof ModuleItem && !(getLeft().getItem() instanceof ModuleItem)){
            System.out.println("testing");
            AbstractAgolEntity module = (AbstractAgolEntity) ((ModuleItem)left).getType().create(level());
            module.moveTo(position());
            level().addFreshEntity(module);
            module.startRiding(this);
            setLeft(getInventory().getItem(2));
            setLeftUUID(module.getUUID());
            System.out.println(module);
        }
        else{
            //setLeft(new ItemStack(Items.AIR));
            for(Entity e: getPassengers()){
                if (e.getUUID() == getLeftUUID() && e instanceof AbstractAgolEntity) ((AbstractAgolEntity) e).destroyAgol();
            }
            setLeftUUID(this.uuid);
        }

        if (right instanceof ModuleItem && !(getRight().getItem() instanceof ModuleItem)){
            AbstractAgolEntity module = (AbstractAgolEntity) ((ModuleItem)right).getType().create(level());
            module.moveTo(position());
            level().addFreshEntity(module);
            module.startRiding(this);
            setRight(getInventory().getItem(3));
            setRightUUID(module.getUUID());
        }
        else{
            //setRight(new ItemStack(Items.AIR));
            for(Entity e: getPassengers()){
                if (e.getUUID() == getRightUUID() && e instanceof AbstractAgolEntity) ((AbstractAgolEntity) e).destroyAgol();
            }
            setRightUUID(this.uuid);

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
