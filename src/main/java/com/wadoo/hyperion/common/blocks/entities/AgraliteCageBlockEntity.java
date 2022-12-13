package com.wadoo.hyperion.common.blocks.entities;

import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class AgraliteCageBlockEntity extends BlockEntity implements IAnimatable {
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private boolean has_capsling = false;

    public AgraliteCageBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.AGRALITE_CAGE.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <T extends IAnimatable> PlayState predicate(AnimationEvent<T> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("loop", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    public boolean getHasCapsling(){
        return this.has_capsling;
    }

    public void setHasCapsling(boolean has_capsling){
        this.has_capsling = has_capsling;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
