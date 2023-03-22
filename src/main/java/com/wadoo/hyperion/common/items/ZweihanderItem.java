package com.wadoo.hyperion.common.items;

import com.wadoo.hyperion.client.renderers.item.ZweihanderRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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

public class ZweihanderItem extends SwordItem implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation LOOP = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation BLOCKR = RawAnimation.begin().thenPlay("blockRight");
    private static final RawAnimation BLOCKL = RawAnimation.begin().thenPlay("blockLeft");

    //TODO make two blocking anims for each hand

    public ZweihanderItem(HyperionTiers tier, int damage, float attackSpeed, Properties properties) {
        super(tier, damage,attackSpeed,properties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);

    }


    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final ZweihanderRenderer renderer = new ZweihanderRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity user, int i, boolean j) {
        super.inventoryTick(stack, level, user, i, j);
        if (user instanceof  Player){
            if(!((Player) user).isUsingItem() && !level.isClientSide){

                triggerAnim(user, GeoItem.getOrAssignId(stack, (ServerLevel)user.level), "controller", "idle");
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand hand) {
        ItemStack itemstack = user.getItemInHand(hand);
        user.setPose(Pose.CROAKING);
        user.setDeltaMovement(0d,-5d, 0d);
        user.startUsingItem(hand);
        if(level.isClientSide()){
        }
        else{
            if(hand == InteractionHand.MAIN_HAND){
                triggerAnim(user, GeoItem.getOrAssignId(user.getItemInHand(hand), (ServerLevel)user.level), "controller", "blockRight");

            }
            else {
                triggerAnim(user, GeoItem.getOrAssignId(user.getItemInHand(hand), (ServerLevel) user.level), "controller", "blockLeft");
            }
        }
        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public int getUseDuration(ItemStack p_41454_) {
        return 72000;

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", state -> state.setAndContinue(LOOP))
                .triggerableAnim("blockRight", BLOCKR)
                .triggerableAnim("blockLeft", BLOCKL)
                .triggerableAnim("idle", LOOP));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BLOCK;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity user) {
        if(!user.level.isClientSide()) {
            triggerAnim(user, GeoItem.getOrAssignId(stack, (ServerLevel) user.level), "controller", "block");
        }
        return super.hurtEnemy(stack, entity, user);
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
