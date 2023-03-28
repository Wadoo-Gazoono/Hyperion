package com.wadoo.hyperion.common.blocks;

import com.wadoo.hyperion.common.registry.ParticleHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AgraliteLamp extends LanternBlock {
    protected static final VoxelShape AABB = Shapes.or(Block.box(3.0D, 0.0D, 3.0D, 13.0D, 12.0D, 13.0D), Block.box(7.0D, 12.0D, 7.0D, 9.0D, 13.0D, 9.0D));
    protected static final VoxelShape HANGING_AABB = Shapes.or(Block.box(3.0D, 4.0D, 3.0D, 13.0D, 16.0D, 13.0D));

    public AgraliteLamp(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState p_153474_, BlockGetter p_153475_, BlockPos p_153476_, CollisionContext p_153477_) {
        return p_153474_.getValue(HANGING) ? HANGING_AABB : AABB;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource randomSource) {
        //super.animateTick(p_220827_, p_220828_, p_220829_, p_220830_);

        for(int i = 0; i < 3; i++) {
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = 0;
            if(state.getValue(LanternBlock.HANGING)){
                d1 =(double) pos.getY() + 0.5D;
            }
            else{
                d1 =(double) pos.getY() + 0.2D;

            }
            double d2 = (double) pos.getZ() + 0.5D;
            level.addParticle(ParticleHandler.AGRALITE_FLAME.get(), d0, d1, d2, 0.0D, 0.0D, 0.0D);
        }
    }
}
