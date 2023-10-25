package com.wadoo.hyperion.common.blocks.agralite_cage;

import com.wadoo.hyperion.common.registry.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.stringtemplate.v4.ST;

import java.awt.*;
import java.util.stream.Stream;

public class AgraliteCageBlock extends Block {
    public static final IntegerProperty STATE = IntegerProperty.create("state",0,2);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected static final VoxelShape OPEN = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(1, 14, 0, 15, 16, 1),
            Block.box(1, 14, 15, 15, 16, 16),
            Block.box(0, 14, 0, 1, 16, 16),
            Block.box(15, 14, 0, 16, 16, 16),
            Block.box(0.8, 2, 1, 1, 14, 15),
            Block.box(0.8000000000000007, 2, 0.7999999999999999, 15.2, 14, 1),
            Block.box(0.8000000000000007, 2, 15, 15.2, 14, 15.2),
            Block.box(15, 2, 1, 15.2, 14, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape OPEN_TOP = Stream.of(
            Block.box(1, 14, 0, 15, 16, 1),
            Block.box(1, 14, 15, 15, 16, 16),
            Block.box(0, 14, 0, 1, 16, 16),
            Block.box(15, 14, 0, 16, 16, 16),
            Block.box(0.8, 1, 0.8000000000000007, 1, 14, 15.2),
            Block.box(1, 1, 0.8, 15, 14, 1),
            Block.box(1, 1, 15, 15, 14, 15.2),
            Block.box(15, 1, 0.8000000000000007, 15.2, 14, 15.2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape TOP = Stream.of(
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(0.7999999999999999, 0, 0.8000000000000007, 1, 14, 15.2),
            Block.box(1, 0, 0.8, 15, 14, 1),
            Block.box(1, 0, 15, 15, 14, 15.2),
            Block.box(15, 0, 0.8000000000000007, 15.2, 14, 15.2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape BOTTOM = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0.8, 2, 1, 1, 14, 15.2),
            Block.box(0.8, 14, 1, 1, 16, 15.2),
            Block.box(15, 14, 1, 15.2, 16, 15),
            Block.box(1, 14, 15, 15.2, 16, 15.2),
            Block.box(0.8000000000000007, 14, 0.8, 15.2, 16, 1),
            Block.box(0.8000000000000007, 2, 0.8, 15.2, 14, 1),
            Block.box(1, 2, 15, 15.2, 14, 15.2),
            Block.box(15, 2, 1, 15.2, 14, 15)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    protected static final VoxelShape NORMAL = Stream.of(
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 14, 0, 16, 16, 16),
            Block.box(0.8, 2, 0.8000000000000007, 1, 14, 15.2),
            Block.box(1, 2, 0.8, 15, 14, 1),
            Block.box(1, 2, 15, 15, 14, 15.2),
            Block.box(15, 2, 0.8000000000000007, 15.200000000000001, 14, 15.2)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public AgraliteCageBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch (state.getValue(STATE)){
            case 0:
                if(state.getValue(POWERED)){
                    return OPEN;
                }
                return NORMAL;
            case 1:
                return BOTTOM;
            case 2:
                if(state.getValue(POWERED)){
                    return OPEN_TOP;
                }
                return TOP;
        }
        return NORMAL;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState stateNew, LevelAccessor accessor, BlockPos pos, BlockPos posNew) {
        if(direction==Direction.UP && state.getValue(STATE) != 2 && stateNew.is(BlockHandler.AGRALITE_CAGE.get())){
            return state.setValue(STATE,1);
        }
        if(direction==Direction.UP && state.getValue(STATE) != 2 && !stateNew.is(BlockHandler.AGRALITE_CAGE.get())){
            return state.setValue(STATE,0);
        }
        if(direction==Direction.DOWN && state.getValue(STATE) == 2 && !stateNew.is(BlockHandler.AGRALITE_CAGE.get())){
            return state.setValue(STATE,0);
        }
        if(direction==Direction.DOWN && state.getValue(STATE) == 0 && stateNew.is(BlockHandler.AGRALITE_CAGE.get())){
            return state.setValue(STATE,2);
        }

        return state;
    }
    

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean p_52781_) {
        if (!level.isClientSide) {
            boolean flag = state.getValue(POWERED);
            if(level.getBlockState(pos2).is(BlockHandler.AGRALITE_CAGE.get())){
                if(level.getBlockState(pos2).getValue(POWERED) == true) {
                    level.setBlock(pos, state.setValue(POWERED, true), 2);
                }
                System.out.println(1);
            }
            if (level.hasNeighborSignal(pos)) {
                if(state.getValue(STATE) != 1)this.playSound((Player)null, level, pos, SoundEvents.IRON_TRAPDOOR_OPEN,flag);
                level.setBlock(pos,state.setValue(POWERED,true),2);
            }
            else{

                level.setBlock(pos,state.setValue(POWERED,false),2);

            }

        }
    }

    protected void playSound(@javax.annotation.Nullable Player p_57528_, Level p_57529_, BlockPos p_57530_, SoundEvent event, boolean flag) {
        p_57529_.playSound(p_57528_, p_57530_, event, SoundSource.BLOCKS, 1.0F, p_57529_.getRandom().nextFloat() * 0.1F + 0.9F);
        p_57529_.gameEvent(p_57528_, flag ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, p_57530_);
    }

    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(!(entity instanceof Player) && !(entity instanceof ItemEntity))entity.setDeltaMovement(0d,0d,0d);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        if(context.getPlayer().isShiftKeyDown()){
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(STATE,0).setValue(POWERED,level.hasNeighborSignal(blockpos));
        }
        if(level.getBlockState(blockpos.below()).is(BlockHandler.AGRALITE_CAGE.get()) && (level.getBlockState(blockpos.below()).getValue(STATE) == 0 || level.getBlockState(blockpos.below()).getValue(STATE) == 1)){
            return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(STATE,2).setValue(POWERED,level.hasNeighborSignal(blockpos));
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(STATE,0).setValue(POWERED,level.hasNeighborSignal(blockpos));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING).add(STATE).add(POWERED);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        if(state.getValue(STATE) == 1){
            level.setBlock(pos.above(),Blocks.AIR.defaultBlockState(),2);
        }
        if(state.getValue(STATE) == 2){
            level.setBlock(pos.below(),Blocks.AIR.defaultBlockState(),2);
        }
    }
}
