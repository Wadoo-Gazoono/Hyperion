package com.wadoo.hyperion.common.mixin.hyperionMixin;

import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import com.wadoo.hyperion.common.inventory.menu.agol.AgolOpenContainer;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin implements AgolOpenContainer {
    @Shadow
    protected abstract void nextContainerCounter();

    @Shadow protected abstract void initMenu(AbstractContainerMenu abstractContainerMenu);

    @Shadow private int containerCounter;

    @Override
    public void openAgolInventory(AbstractAgolEntity hamster, Container pInventory) {
        System.out.println("hi");
        ServerPlayer $this = (ServerPlayer) (Object) this;
        if ($this.containerMenu != $this.inventoryMenu) {
            $this.closeContainer();
        }
        this.nextContainerCounter();
        $this.connection.m_9829_(new ClientboundHorseScreenOpenPacket(this.containerCounter, pInventory.getContainerSize(), hamster.getId()));
        $this.containerMenu = new AbstractAgolMenu(this.containerCounter, $this.getInventory(), pInventory, hamster);
        this.initMenu($this.containerMenu);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open($this, $this.containerMenu));

    }


}