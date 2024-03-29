package com.wadoo.hyperion.common.inventory.menu.agol;

import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorT;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.items.ModuleItem;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AbstractAgolMenu extends AbstractContainerMenu {
    private Container horseContainer;
    private AbstractAgolEntity horse;
    public Player player;
    public AbstractAgolMenu(int id, Inventory inventory, Container container, final AbstractAgolEntity agol) {
        super(null, id);
        this.horseContainer = container;
        this.horse = agol;
        container.startOpen(inventory.player);
        this.addSlot(new Slot(container, 0, 20, 21) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.APPLE) && !this.hasItem();
            }
            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(container, 1, 20, 41) {
            public boolean mayPlace(ItemStack stack) {
                return stack.getItem() instanceof ModuleItem && !this.hasItem();
            }

            public boolean isActive() {
                return true;
            }

            public int getMaxStackSize() {
                return 1;
            }
        });
        if (agol instanceof AgolConnectorT){
            this.addSlot(new Slot(container, 2, 121, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
            this.addSlot(new Slot(container, 3, 143, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
        }else if (agol instanceof AgolHead) {

        }
        else {
            this.addSlot(new Slot(container, 2, 132, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
        }

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(inventory, k1 + i1 * 9 + 9, 4 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(inventory, j1, 4 + j1 * 18, 142));
        }

    }

    public AbstractAgolMenu(int id, Inventory inventory, Container container, final AbstractAgolEntity agol, Player player) {
        super(null, id);
        this.horseContainer = container;
        this.horse = agol;
        this.player = player;
        container.startOpen(inventory.player);
        this.addSlot(new Slot(container, 0, 20, 21) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.APPLE) && !this.hasItem();
            }
            public boolean isActive() {
                return true;
            }
        });
        this.addSlot(new Slot(container, 1, 20, 41) {
            public boolean mayPlace(ItemStack stack) {
                return true;
            }

            public boolean isActive() {
                return true;
            }

            public int getMaxStackSize() {
                return 1;
            }
        });
        if (agol instanceof AgolConnectorT){
            this.addSlot(new Slot(container, 2, 121, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
            this.addSlot(new Slot(container, 3, 143, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
        } else if (agol instanceof AgolHead) {

        } else {
            this.addSlot(new Slot(container, 2, 132, 31) {
                public boolean mayPlace(ItemStack stack) {
                    return stack.getItem() instanceof ModuleItem && !this.hasItem();

                }

                public boolean isActive() {
                    return true;
                }

                public int getMaxStackSize() {
                    return 1;
                }
            });
        }

        for(int i1 = 0; i1 < 3; ++i1) {
            for(int k1 = 0; k1 < 9; ++k1) {
                this.addSlot(new Slot(inventory, k1 + i1 * 9 + 9, 4 + k1 * 18, 102 + i1 * 18 + -18));
            }
        }

        for(int j1 = 0; j1 < 9; ++j1) {
            this.addSlot(new Slot(inventory, j1, 4 + j1 * 18, 142));
        }

    }

    public void setEntity(AbstractAgolEntity entity){
        this.horse = entity;
    }

    public void setContainer(Container container){
        this.horseContainer = container;
    }


    public boolean stillValid(Player player) {
        return !this.horse.hasInventoryChanged(this.horseContainer) && this.horseContainer.stillValid(player) && this.horse.isAlive() && this.horse.distanceTo(player) < 8.0F;
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = this.horseContainer.getContainerSize();
            if (index < i) {
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
                if (index >= j && index < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.horseContainer.stopOpen(player);
    }
}