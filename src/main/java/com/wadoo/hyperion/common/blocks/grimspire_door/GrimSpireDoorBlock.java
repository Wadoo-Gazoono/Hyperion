package com.wadoo.hyperion.common.blocks.grimspire_door;

import com.wadoo.hyperion.common.registry.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GrimSpireDoorBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final IntegerProperty X_POS = IntegerProperty.create("x_pos",0,8);
    public static final IntegerProperty Y_POS = IntegerProperty.create("y_pos",0,8);
    public static final EnumProperty<GSDoorPart> PART = EnumProperty.create("part", GSDoorPart.class);

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);

    public GrimSpireDoorBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(PART, GSDoorPart.START)
                .setValue(X_POS, Integer.valueOf(5))
                .setValue(Y_POS, Integer.valueOf(1)));
    }


    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState facingState, LevelAccessor accessor, BlockPos pos, BlockPos facingPos) {
        return super.updateShape(state, direction, facingState, accessor, pos, facingPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        BlockPos blockpos = context.getClickedPos();

        if(context.getLevel().getBlockState(blockpos.below()).is(BlockHandler.GRIMSPIRE_DOOR.get()) ||
           context.getLevel().getBlockState(blockpos.above()).is(BlockHandler.GRIMSPIRE_DOOR.get()) ||
           context.getLevel().getBlockState(blockpos.west()).is(BlockHandler.GRIMSPIRE_DOOR.get()) ||
           context.getLevel().getBlockState(blockpos.east()).is(BlockHandler.GRIMSPIRE_DOOR.get()) ||
           context.getLevel().getBlockState(blockpos.south()).is(BlockHandler.GRIMSPIRE_DOOR.get())||
           context.getLevel().getBlockState(blockpos.north()).is(BlockHandler.GRIMSPIRE_DOOR.get())){
            return null;
        }
        Level level = context.getLevel();
        BlockPos startPosition = blockpos.relative(direction.getClockWise(), 3).above(7);
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                if(!level.getBlockState(startPosition.relative(direction.getClockWise().getOpposite(), i).below(j - 1)).is(Blocks.AIR)){
                    return null;
                }
            }
        }
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(FACING, PART,X_POS,Y_POS);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
        super.setPlacedBy(level, pos, state, entity, stack);
        if(!level.isClientSide()) {
            BlockPos startPosition = pos.relative(state.getValue(FACING).getClockWise(), 3).above(7);
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    level.setBlock(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j - 1), BlockHandler.GRIMSPIRE_DOOR.get().defaultBlockState().setValue(PART,GSDoorPart.DOOR).setValue(FACING, state.getValue(FACING)).setValue(X_POS, i).setValue(Y_POS, j), 3);
                    if((i ==4 || i == 5) && (j == 7 || j == 8)){
                        level.setBlock(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j - 1), BlockHandler.GRIMSPIRE_DOOR.get().defaultBlockState().setValue(PART,GSDoorPart.KEY).setValue(FACING, state.getValue(FACING)).setValue(X_POS, i).setValue(Y_POS, j), 3);
                    }
                }
            }
        }
    }


    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        BlockPos startPosition = pos.relative(state.getValue(FACING).getClockWise(),state.getValue(X_POS).intValue() - 1);
        if(state.getValue(PART) != GSDoorPart.KEY || !player.getItemInHand(hand).is(Items.ENCHANTED_GOLDEN_APPLE)){
            return InteractionResult.PASS;
        }
        while (level.getBlockState(startPosition.above()).is(BlockHandler.GRIMSPIRE_DOOR.get())){
            startPosition = startPosition.above();
        }
        for(int i =0; i < 8;i++){
            for(int j = 0;j <8;j++){
                if(level.getBlockState(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j)).is(BlockHandler.GRIMSPIRE_DOOR.get())) {
                    level.setBlock(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
        level.setBlock(startPosition.relative(state.getValue(FACING).getCounterClockWise(),2).below(7), BlockHandler.GRIMSPIRE_DOOR_ENTITY.get().defaultBlockState().setValue(FACING,state.getValue(FACING)), 3);
        return super.use(state, level, pos, player, hand, result);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        super.playerWillDestroy(level, pos, state, player);
        BlockPos startPosition = pos.relative(state.getValue(FACING).getClockWise(),state.getValue(X_POS).intValue() - 1);
        while (level.getBlockState(startPosition.above()).is(BlockHandler.GRIMSPIRE_DOOR.get())){
            startPosition = startPosition.above();
        }
        for(int i =0; i < 8;i++){
            for(int j = 0;j <8;j++){
                if(level.isClientSide()){
                    for(int k = 0; k < level.random.nextInt(15)+5; k++){
                        Vec3 particlePos = startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j - 1).getCenter().add(level.random.nextFloat()-0.5f, level.random.nextFloat()-0.5f, level.random.nextFloat()-0.5f);
                        level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, state), particlePos.x, particlePos.y + 0.2f,particlePos.z, 0, 0.08d,0);                }
                }

                if(level.getBlockState(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j)).is(BlockHandler.GRIMSPIRE_DOOR.get())) {
                    level.setBlock(startPosition.relative(state.getValue(FACING).getClockWise().getOpposite(), i).below(j), Blocks.AIR.defaultBlockState(), 3);
                }
            }
        }
    }


}
