package com.wadoo.hyperion.common.items;

import com.wadoo.hyperion.client.renderers.item.GigaHammerRenderer;
import com.wadoo.hyperion.client.renderers.item.VolatileGoopItemRenderer;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class GigaHammerItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation LOOP = RawAnimation.begin().thenLoop("loop");
    private static final RawAnimation FIRE = RawAnimation.begin().thenLoop("fire");

    public GigaHammerItem(Properties properties) {
        super(properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final GigaHammerRenderer renderer = new GigaHammerRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemstack = user.getItemInHand(hand);
        user.setDeltaMovement(0d,-5d, 0d);
        if(level.isClientSide()){
            for(int i = 0; i < 200; i++){
                Vec3 projectilePos = new Vec3(0D,0D,2D).yRot((float)Math.toRadians(-user.getYRot())).add(user.position());

                user.level.addParticle(ParticleTypes.LAVA, projectilePos.x, user.getY() + 0.3d, projectilePos.z, 0d,0d,0d);
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idle", state -> state.setAndContinue(LOOP)).triggerableAnim("fire", FIRE));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
