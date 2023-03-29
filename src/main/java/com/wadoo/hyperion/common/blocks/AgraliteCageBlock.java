package com.wadoo.hyperion.common.blocks;

import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;

public class AgraliteCageBlock extends DirectionalBlock implements EntityBlock {
    protected static final VoxelShape SHAPE = Block.box(0D, 0.0D, 0D, 16D, 16D, 16D);

    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty FULL = BooleanProperty.create("full");

    public AgraliteCageBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.SOUTH).setValue(FULL, Boolean.valueOf(false)));

    }



    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING).add(FULL);
    }

    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        super.use(state, level, pos, player, hand, result);
            if((player.getItemInHand(hand).is(ItemHandler.CAPSLING_BUCKET.get()) || player.getItemInHand(hand).is(ItemHandler.CAPSLING_SPAWN_EGG.get())) && !state.getValue(FULL)){
                state.setValue(FULL,true);
                return InteractionResult.SUCCESS;
            }
        return InteractionResult.PASS;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState state2, boolean p_60570_) {
        super.onPlace(state, level, pos, state, p_60570_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
            if(state.getValue(FULL)) {
                CapslingEntity capsling = EntityHandler.CAPSLING.get().create(level);
                capsling.setPos(pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f);
                level.addFreshEntity(capsling);
                level.playSound(player, pos, SoundEvents.STRIDER_HAPPY, SoundSource.BLOCKS, 1.7f, 1.4f);
            }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityHandler.AGRALITE_CAGE.get().create(pos,state);    }
}
