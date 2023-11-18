package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import com.wadoo.hyperion.common.items.ModuleItem;
import com.wadoo.hyperion.common.network.NetworkHandler;
import com.wadoo.hyperion.common.network.OpenAgolScreenPacket;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
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
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class AgolWalker extends AbstractAgolEntity implements ContainerListener, HasCustomInventoryScreen, PlayerRideable, OwnableEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(AgolWalker.class, EntityDataSerializers.STRING);

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

    public AgolWalker(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setAgolType("base");
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    public AgolWalker(EntityType<? extends PathfinderMob> mob, Level level, int health, int defense, int damage, int energy_use, int weight, String type) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.health = health;
        this.defense = defense;
        this.damage = damage;
        this.energy_use = energy_use;
        this.weight = weight;
        setAgolType(type);
    }

    @Override
    protected boolean canAddPassenger(Entity pPassenger) {
        return this.getPassengers().size() <= 2 || (inventory.getItem(2) != null && inventory.getItem(3) != null);
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
        controllers.add(new AnimationController<>(this, "animController", 2, this::predicate).setCustomInstructionKeyframeHandler(this::instructionListener));
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(this.health);
        this.getAttribute(Attributes.ARMOR).setBaseValue(this.defense);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(this.damage);

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        for(Entity e : this.getIndirectPassengers()){
            if (e instanceof Player) {
                return (LivingEntity) e;
            }
        }
        return null;
    }

    protected void tickRidden(Player player, Vec3 vec3) {
        super.tickRidden(player, vec3);
        if(player.xxa != 0){
            this.setRot(getYRot() - player.xxa * 5, player.getXRot() * 0.25F);
            this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
            this.maxUpStep = 1;
            this.getNavigation().stop();
            this.setTarget(null);
            this.setSprinting(true);
        }

    }

    protected Vec3 getRiddenInput(Player player, Vec3 vec3) {
        if (player.zza != 0 || player.xxa != 0) {
            float f = 0F;
            float f1 = player.zza;
            if (f1 <= 0.0F) {
                f1 *= 0.25F;
            }

            return new Vec3(f, 0.0D, f1).normalize().multiply(0.2d,0.2d,0.2d);
        }
        return Vec3.ZERO;
    }

    private <ENTITY extends GeoEntity> void instructionListener(CustomInstructionKeyframeEvent<ENTITY> event) {

    }

    @Override
    protected float getRiddenSpeed(Player p_278286_) {
        return (float) this.getAttributes().getValue(Attributes.MOVEMENT_SPEED) * 2;
    }

    @Override
    public void openCustomInventoryScreen(Player player) {
        super.openCustomInventoryScreen(player);
    }

    protected PlayState predicate(AnimationState<AbstractAgolEntity> state) {
        if (state.isMoving()){
            state.setAndContinue(WALK);
        }
        else{
            state.setAndContinue(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide && getRandom().nextFloat() < 0.1f){
            Vec3 particlePos = new Vec3(0f,1.4f,-1f).yRot(-this.yBodyRot * ((float)Math.PI / 180F)).add(position());
            Vec3 velocityVec = new Vec3(0f,0.2f,-0.02f).yRot(-this.yBodyRot * ((float)Math.PI / 180F));
            this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, particlePos.x, particlePos.y, particlePos.z, velocityVec.x, velocityVec.y, velocityVec.z);
        }
    }

    public String getAgolName(){
        return "agol_base";
    }
    @Override
    public double m_6048_() {
        return 2f;
    }
}
