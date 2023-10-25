package com.wadoo.hyperion.common.mixin.hyperionMixin;

import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import com.wadoo.hyperion.common.inventory.menu.agol.AgolOpenContainer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements AgolOpenContainer {

    @Shadow
    protected abstract void nextContainerCounter();

    @Shadow
    protected abstract void initMenu(AbstractContainerMenu abstractContainerMenu);

    @Shadow
    private int containerCounter;

    @Override
    public void openAgolInventory(AgolWalkerEntity agol, Container container) {

    }
}
