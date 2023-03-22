package com.wadoo.hyperion.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class AgralitePipeBlock extends DirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public final VoxelShape Y_SHAPE = Shapes.or(Block.box(1d,0d,1d,15d,4d,15d),Block.box(2d,4d,2d,14d,12d,14d),Block.box(1d,12d,1d,15d,16d,15d));
    public final VoxelShape Z_SHAPE = Shapes.or(Block.box(0, 0.0625 * 16, 0.0625 * 16, 0.25 * 16, 0.9375 * 16, 0.9375 * 16),Block.box(0.75  * 16, 0.0625  * 16, 0.0625  * 16, 1  * 16, 0.9375 * 16, 0.9375 * 16),Block.box(0.25  * 16, 0.125 * 16, 0.125 * 16, 0.75 * 16, 0.875 * 16, 0.875 * 16));
    public final VoxelShape X_SHAPE = Shapes.or(Block.box(0.0625 * 16, 0.0625* 16, 0.75* 16, 0.9375* 16, 0.9375* 16, 1* 16),Block.box(0.0625* 16, 0.0625* 16, 0, 0.9375* 16, 0.9375* 16, 0.25* 16),Block.box(0.125* 16, 0.125* 16, 0.25* 16, 0.875* 16, 0.875* 16, 0.75* 16));


    public AgralitePipeBlock(Properties properties) {
        super(properties);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mir) {
        return state.rotate(mir.getRotation(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        switch (state.getValue(FACING)) {
            case WEST,EAST: return Z_SHAPE;
            case NORTH, SOUTH: return X_SHAPE;
            case DOWN, UP: return Y_SHAPE;
        }
        return Y_SHAPE;
    }

}
