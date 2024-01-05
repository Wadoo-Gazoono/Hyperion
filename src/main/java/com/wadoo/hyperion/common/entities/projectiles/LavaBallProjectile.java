package com.wadoo.hyperion.common.entities.projectiles;

import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LavaBallProjectile extends AbstractArrow implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation FLY = RawAnimation.begin().thenPlay("fly");
    private static final RawAnimation SPAWN = RawAnimation.begin().thenPlay("spawn");
    private static final RawAnimation LAND = RawAnimation.begin().thenPlay("land");

    public int ticksOnGround = 0;
    public LivingEntity owner;

    public LavaBallProjectile(EntityType<? extends AbstractArrow> entityType, Level world) {
        super(entityType, world);
        this.pickup = AbstractArrow.Pickup.DISALLOWED;
    }

    public LavaBallProjectile(Level world, LivingEntity owner) {
        super(EntityHandler.LAVA_BALL.get(), owner, world);
        this.owner = owner;
    }

    protected LavaBallProjectile(EntityType<? extends VolatileGoopProjectile> type, double x, double y, double z, Level world) {
        this(type, world);
    }

    protected LavaBallProjectile(EntityType<? extends VolatileGoopProjectile> type, LivingEntity owner, Level world) {
        this(type, owner.getX(), owner.getEyeY() - 0.1D, owner.getZ(), world);
        this.setOwner(owner);
        this.owner = owner;
        if (owner instanceof Player) {
            this.pickup = AbstractArrow.Pickup.DISALLOWED;
        }
    }


    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void onHit(HitResult p_37260_) {
        super.onHit(p_37260_);
        explode();
    }

    @Override
    protected void onHitEntity(EntityHitResult p_36757_) {
        explode();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 1, animationState -> (animationState.setAndContinue(FLY)))
                .triggerableAnim("land", LAND));
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.LAVA_POP;
    }


    @Override
    public void tick() {
        super.tick();
        if (inGround){
            ticksOnGround++;
        }
        if (ticksOnGround > 1){
            kill();
        }
        if (tickCount == 0) triggerAnim("shoot_controller", "spawn");
        if(this.level().isClientSide && this.tickCount % 2 == 0){
            for(int i =0; i < 3; i++){
                this.level().addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getRandomX(0.2D), this.getY() , this.getRandomZ(0.2D), 0, 0, 0);
                this.level().addParticle(ParticleTypes.LAVA, this.getRandomX(0.1D), this.getY() , this.getRandomZ(0.1D), 0, 0.2, 0);
            }

            this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(0.02D), this.getY() , this.getRandomZ(0.02D), 0, 0, 0);
        }

    }

    public void explode(){
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0f,0.2f);
        for (LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().move(0d,-1d,0d).inflate(2.1D, 3.4D, 2.1D))) {
            if (livingentity.is(owner)) continue;
            livingentity.hurt(this.damageSources().thrown(this, this.owner), 18f + this.random.nextInt(9));
            livingentity.setSecondsOnFire(8);
        }
        //if(this.level().isClientSide) {
            for (int i = 0; i < 8; i++){
                this.level().addParticle(ParticleTypes.EXPLOSION, this.getRandomX(0.4D), this.getY(), this.getRandomZ(0.4D), 0, 0, 0);
            }
            for(int i = 0; i < this.random.nextInt(25) + 17; i++){
                this.level().addParticle(ParticleTypes.LAVA, this.getRandomX(0.31D), this.getY() , this.getRandomZ(0.31D), 0, 0.2, 0);
            }
            for(int i = 0; i < this.random.nextInt(13) + 6; i++){
                this.level().addParticle(ParticleTypes.FLAME, this.getRandomX(4.81D), this.getY() + this.random.nextFloat() * 2 , this.getRandomZ(4.81D), 0, 0.4 + this.random.nextFloat()/4, 0);
            }
            for(int i = 0; i < this.random.nextInt(7) + 5; i++){
                this.level().addParticle(ParticleTypes.SMOKE, this.getRandomX(3.81D), this.getY() + this.random.nextFloat() * 2, this.getRandomZ(3.81D), 0, 0.8 + this.random.nextFloat()/5, 0);
            }
        //}
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(Items.AIR);
    }
}
