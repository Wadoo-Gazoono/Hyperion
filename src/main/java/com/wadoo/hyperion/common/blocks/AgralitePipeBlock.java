package com.wadoo.hyperion.common.blocks;

import com.wadoo.hyperion.common.registry.BlockHandler;
import com.wadoo.hyperion.common.registry.TagHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class AgralitePipeBlock extends DirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty SMOKED = BooleanProperty.create("smoked");

    public final VoxelShape Y_SHAPE = Shapes.or(Block.box(1d,0d,1d,15d,4d,15d),Block.box(2d,4d,2d,14d,12d,14d),Block.box(1d,12d,1d,15d,16d,15d));
    public final VoxelShape Z_SHAPE = Shapes.or(Block.box(0, 0.0625 * 16, 0.0625 * 16, 0.25 * 16, 0.9375 * 16, 0.9375 * 16),Block.box(0.75  * 16, 0.0625  * 16, 0.0625  * 16, 1  * 16, 0.9375 * 16, 0.9375 * 16),Block.box(0.25  * 16, 0.125 * 16, 0.125 * 16, 0.75 * 16, 0.875 * 16, 0.875 * 16));
    public final VoxelShape X_SHAPE = Shapes.or(Block.box(0.0625 * 16, 0.0625* 16, 0.75* 16, 0.9375* 16, 0.9375* 16, 1* 16),Block.box(0.0625* 16, 0.0625* 16, 0, 0.9375* 16, 0.9375* 16, 0.25* 16),Block.box(0.125* 16, 0.125* 16, 0.25* 16, 0.875* 16, 0.875* 16, 0.75* 16));


    public AgralitePipeBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(SMOKED, Boolean.valueOf(false)));

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING,SMOKED);
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
        if((context.getLevel().getBlockState(context.getClickedPos().below(1)).is(Blocks.MAGMA_BLOCK))){
            Direction direction = context.getNearestLookingDirection().getOpposite().getOpposite();
            if(direction == Direction.DOWN || direction == Direction.UP){
                return this.defaultBlockState().setValue(FACING, direction).setValue(SMOKED,true);
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite().getOpposite()).setValue(SMOKED,false);
    }


    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    public BlockState updateShape(BlockState thisState, Direction direction, BlockState state, LevelAccessor levelAccessor, BlockPos thisPos, BlockPos pos) {
        if(direction == Direction.DOWN && (state.is(Blocks.MAGMA_BLOCK) && (thisState.getValue(FACING) == Direction.UP || thisState.getValue(FACING) == Direction.DOWN))){
            return super.updateShape(thisState.setValue(SMOKED,true), direction, state, levelAccessor, thisPos, pos);
        }
        if(direction == Direction.DOWN && (!state.is(Blocks.MAGMA_BLOCK) && (thisState.getValue(FACING) == Direction.UP || thisState.getValue(FACING) == Direction.DOWN))){
            return super.updateShape(thisState.setValue(SMOKED,false), direction, state, levelAccessor, thisPos, pos);
        }
        return super.updateShape(thisState, direction, state, levelAccessor, thisPos, pos);
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

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource source) {
        super.animateTick(state, level, pos, source);
        if(state.getValue(SMOKED)){
            Vec3 particlePos= ((pos.relative(state.getValue(FACING).getOpposite()).getCenter())).subtract(0d,-0.2d,0d);
            if(level.isClientSide){
                if(source.nextFloat() < 0.4f){
                    level.addParticle(ParticleTypes.FLAME, particlePos.x(), particlePos.y() - 0.5f,particlePos.z(),0d,0.04d,0d);
                }
                if(source.nextFloat() < 0.5f){
                    level.addParticle(ParticleTypes.LAVA, particlePos.x(), particlePos.y() - 0.5f,particlePos.z(),0d,0.04d,0d);
                }
                level.addParticle(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, particlePos.x(), particlePos.y() - 0.2f,particlePos.z(),0d,0.08d,0d);
            }
        }
    }
}
