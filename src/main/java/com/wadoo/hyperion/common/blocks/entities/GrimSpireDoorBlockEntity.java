package com.wadoo.hyperion.common.blocks.entities;

import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GrimSpireDoorBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int ticksExisted = 0;
    //Animations
    private static final RawAnimation OPEN = RawAnimation.begin().thenPlayAndHold("open");


    public GrimSpireDoorBlockEntity(BlockPos pos, BlockState state){
        super(BlockEntityHandler.GRIMSPIRE_DOOR.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "loop", 0, state -> state.setAndContinue(OPEN)));

    }


    @Override
    public double getTick(Object blockEntity) {
        return GeoBlockEntity.super.getTick(blockEntity);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GrimSpireDoorBlockEntity entity) {
         if(++entity.ticksExisted > 152){
            level.setBlock(pos, Blocks.AIR.defaultBlockState(),3);
        }
    }

        @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}