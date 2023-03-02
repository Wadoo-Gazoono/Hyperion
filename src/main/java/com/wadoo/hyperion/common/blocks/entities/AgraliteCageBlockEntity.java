package com.wadoo.hyperion.common.blocks.entities;

import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;


public class AgraliteCageBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    //Animations
    private static final RawAnimation LOOP = RawAnimation.begin().thenLoop("loop");
    private boolean has_capsling = false;

    public AgraliteCageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.AGRALITE_CAGE.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "loop", 5, state -> state.setAndContinue(LOOP)));
    }

    public boolean getHasCapsling(){
        return this.has_capsling;
    }

    public void setHasCapsling(boolean has_capsling){
        this.has_capsling = has_capsling;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
