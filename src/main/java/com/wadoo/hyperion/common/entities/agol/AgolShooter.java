package com.wadoo.hyperion.common.entities.agol;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.net.ssl.SNIHostName;

public class AgolShooter extends AbstractAgolEntity implements ContainerListener, HasCustomInventoryScreen, PlayerRideable, OwnableEntity, GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation SHOOT = RawAnimation.begin().thenPlay("shoot");

    private static final EntityDataAccessor<String> TYPE = SynchedEntityData.defineId(AgolShooter.class, EntityDataSerializers.STRING);

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

    public AgolShooter(EntityType<? extends PathfinderMob> mob, Level level) {
        super(mob, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        setAgolType("base");
    }

    public AgolShooter(EntityType<? extends PathfinderMob> mob, Level level, int health, int defense, int damage, int energy_use, int weight, String type) {
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
        return createMobAttributes().add(Attributes.FOLLOW_RANGE, 35.0D).add(Attributes.MAX_HEALTH, 15.0D).add(Attributes.ATTACK_DAMAGE, 2.0D).add(Attributes.ARMOR, 2F).add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
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

    @Override
    public void openCustomInventoryScreen(Player player) {
        super.openCustomInventoryScreen(player);
    }

    protected PlayState predicate(AnimationState<AbstractAgolEntity> state) {
        state.setAndContinue(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void tick() {
        super.tick();
    }

    public String getAgolName(){
        return "agol_base";
    }
    @Override
    public double m_6048_() {
        return 1f;
    }

    @Override
    public void updateParent(Container pContainer) {
        super.updateParent(pContainer);
    }

    @Override
    public void executeAction(byte id) {
        super.executeAction(id);
            Arrow snowball = new Arrow(this.level(), this);
            Vec3 lookVec = this.getViewVector(1f).scale(1f);
            Vec3 targetPos = this.position().add(lookVec);
            snowball.moveTo(targetPos);
            double d0 = targetPos.y;
            double d1 = targetPos.x - this.getX();
            double d2 = d0 - snowball.getY();
            double d3 = targetPos.z - this.getZ();
            double d4 = Math.sqrt(d1 * d1 + d3 * d3) * (double) 0.5F;
            snowball.shoot(d1, d2 + d4, d3, 0.8F, 40f);

            this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 0.4F / (this.getRandom().nextFloat() * 2F + 1F));
            this.level().addFreshEntity(snowball);
    }
}
