package com.wadoo.hyperion.common.inventory.menu.agol;

import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class AgolWalkerMenu extends AbstractContainerMenu {
    private final Container container;

    public AgolWalkerMenu(int i, Inventory inventory, Container container, final AgolWalkerEntity entity) {
        super(null, i);
        this.container = container;
        container.startOpen(inventory.player);
        this.addSlot(new Slot(this.container, 0, 8, 18) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemHandler.AGRALITE_SHEET.get());
            }

            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(this.container, 1, 9, 18) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemHandler.AGRALITE_SHEET.get());
            }

            public boolean isActive() {
                return true;
            }
        });
    }


    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.container.stopOpen(player);
    }
}
