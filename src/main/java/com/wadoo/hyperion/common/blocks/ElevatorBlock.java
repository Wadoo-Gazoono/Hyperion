package com.wadoo.hyperion.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ElevatorBlock extends BaseEntityBlock {
    protected static final VoxelShape SHAPE = Block.box(0D, 0.0D, 0D, 16D, 16D, 16D);


    public static final EnumProperty MOVESTATE = EnumProperty.create("movestate",ElevatorMoveState.class);
    public static final DirectionProperty FACING = BlockStateProperties.FACING;




    protected ElevatorBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        super.createBlockStateDefinition(stateBuilder);
        stateBuilder.add(FACING).add(MOVESTATE);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return null;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        super.stepOn(level, pos, state, entity);
    }


    public ElevatorMoveState getMoveDirection(Level level, BlockPos pos, BlockState state){
        if(level.getBlockState(pos.above()).is(Blocks.AIR) && level.getBlockState(pos.above(2)).is(Blocks.AIR)&& level.getBlockState(pos.above(3)).is(Blocks.AIR)){
            return ElevatorMoveState.UP;
        }
        return ElevatorMoveState.DOWN;
    }
}
