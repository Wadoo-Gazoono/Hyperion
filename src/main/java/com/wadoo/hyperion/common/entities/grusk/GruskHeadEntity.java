package com.wadoo.hyperion.common.entities.grusk;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.HyperionLivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.checkerframework.checker.units.qual.A;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GruskHeadEntity extends HyperionLivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenPlay("idle");
    private static final RawAnimation ATTACK = RawAnimation.begin().thenPlay("attack");

    public LivingEntity owner;

    public GruskHeadEntity(EntityType<? extends HyperionLivingEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 3, this::predicate)
                .triggerableAnim("idle",IDLE)
                .triggerableAnim("attack",ATTACK));
    }


    protected PlayState predicate(AnimationState<GruskHeadEntity> state) {
        return state.isMoving() ?(state.setAndContinue(ATTACK)) : state.setAndContinue(IDLE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(4, new MeleeAttackGoal(this,1.2f,true));


        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, CapslingEntity.class, 0, true, false, null));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 0, true, false, null));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public void setOwner(LivingEntity owner){
        this.owner = owner;
    }

    @Override
    public void tick() {
        super.tick();
        if(owner != null){
            if(!owner.isDeadOrDying() && owner instanceof GruskEntity)this.setTarget(((GruskEntity) owner).getTarget());
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.45F)
                .add(Attributes.ATTACK_DAMAGE, 3.0F)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7D);
    }
}
