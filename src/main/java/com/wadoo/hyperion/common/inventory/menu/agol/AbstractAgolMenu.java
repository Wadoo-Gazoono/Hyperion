package com.wadoo.hyperion.common.inventory.menu.agol;

import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.registry.ContainerHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AbstractAgolMenu extends AbstractContainerMenu {
    private Container agolContainer;
    private AbstractAgolEntity agol;

    @Override
    public MenuType<?> getType() {
        return ContainerHandler.AGOL_MENU.get();
    }

    public AbstractAgolMenu(int p_39656_, Inventory inv, Container container, final AbstractAgolEntity entity) {
        super(ContainerHandler.AGOL_MENU.get(), p_39656_);
        this.agolContainer = container;
        this.agol = entity;
        int i = 3;
        container.startOpen(inv.player);
        int j = -18;
        this.addSlot(new Slot(container, 0, 8, 18) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemHandler.AGRALITE_SHEET.get());
            }
            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(container, 1, 8, 36) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(ItemHandler.AGRALITE_SHEET.get());
            }

            public boolean isActive() {
                return true;
            }

            public int getMaxStackSize() {
                return 64;
            }
        });


        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(container, k1 + i1 * 9 + 9, 8 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(container, j1, 8 + j1 * 18, 142));
        }

    }

    public AbstractAgolMenu(int i, Inventory inventory) {
        super(ContainerHandler.AGOL_MENU.get(), i);
    }

    public AbstractAgolMenu(int i, Inventory j, SimpleContainer inventory) {
        super(ContainerHandler.AGOL_MENU.get(), i);
    }

    public boolean stillValid(Player player) {
        return !this.agol.hasInventoryChanged(this.agolContainer) && this.agolContainer.stillValid(player) && this.agol.isAlive() && this.agol.distanceTo(player) < 8.0F;
    }


    public ItemStack quickMoveStack(Player p_39665_, int p_39666_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(p_39666_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.agolContainer.getContainerSize();
            if (p_39666_ < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1)) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (p_39666_ >= j && p_39666_ < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_39666_ >= i && p_39666_ < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player p_39663_) {
        super.removed(p_39663_);
        this.agolContainer.stopOpen(p_39663_);
    }

    public static MenuConstructor getServerContainer(AbstractAgolEntity agol) {
        return (id, playerInv, player) -> new AbstractAgolMenu(id, playerInv, agol.inventory, agol);
    }
}
