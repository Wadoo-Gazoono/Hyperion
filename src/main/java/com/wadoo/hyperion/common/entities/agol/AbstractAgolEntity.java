package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import com.wadoo.hyperion.common.inventory.menu.agol.AgolOpenContainer;
import com.wadoo.hyperion.common.network.NetworkHandler;
import com.wadoo.hyperion.common.network.OpenAgolScreenPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkHooks;
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

public class AbstractAgolEntity extends PathfinderMob implements ContainerListener, HasCustomInventoryScreen, OwnableEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(AbstractAgolEntity.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS = SynchedEntityData.defineId(AbstractAgolEntity.class, EntityDataSerializers.BYTE);

    private double health = 20;
    private double defense = 20;
    private double damage = 20;
    private int energy_use = 1;
    private int weight = 5;
    public final SimpleContainer inventory = new SimpleContainer(12);
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
    }



    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TYPE, "base");
        this.entityData.define(DATA_ID_FLAGS, (byte)0);
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
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
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
            serverPlayer.containerMenu = new AbstractAgolMenu(serverPlayer.containerCounter, serverPlayer.getInventory(), this.inventory, this);
            serverPlayer.initMenu(serverPlayer.containerMenu);
            MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(serverPlayer, serverPlayer.containerMenu));
        }
    }

    private int getInventorySize() {
        return 12;
    }


    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Override
    public void containerChanged(Container pContainer) {

    }

    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }

}
