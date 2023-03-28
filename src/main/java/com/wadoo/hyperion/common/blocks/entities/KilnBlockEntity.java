package com.wadoo.hyperion.common.blocks.entities;

import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KilnBlockEntity extends AbstractFurnaceBlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final RawAnimation LOOP = RawAnimation.begin().thenLoop("loop");

    public KilnBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, RecipeType<? extends AbstractCookingRecipe> recipeType) {
        super(BlockEntityHandler.KILN.get(), pos, state, RecipeType.BLASTING);
    }

    public KilnBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityHandler.KILN.get(), pos, state,RecipeType.BLASTING);

    }





    protected Component getDefaultName() {
        return Component.translatable("container.hyperion.kiln");
    }

    protected int getBurnDuration(ItemStack stack) {
        return (int) (super.getBurnDuration(stack) * 1.2);
    }

    protected AbstractContainerMenu createMenu(int p_59293_, Inventory p_59294_) {
        return new FurnaceMenu(p_59293_, p_59294_, this, this.dataAccess);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "loop", 5, state -> state.setAndContinue(LOOP)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
