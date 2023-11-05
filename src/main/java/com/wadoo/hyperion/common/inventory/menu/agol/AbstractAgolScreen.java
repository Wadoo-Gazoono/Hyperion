package com.wadoo.hyperion.common.inventory.menu.agol;


import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AbstractAgolScreen extends AbstractContainerScreen<AbstractAgolMenu> {
    private static final ResourceLocation AGOL_INV = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/agol_core.png");
    private final AbstractAgolEntity agol;
    private float xMouse;
    private float yMouse;

    public AbstractAgolScreen(AbstractAgolMenu menu, Inventory inventory, Component entity) {
        super(menu, inventory, entity);
        this.imageHeight = 200;
        this.agol = null;
    }

    public AbstractAgolScreen(AbstractAgolMenu menu, Inventory inventory, AbstractAgolEntity entity) {
        super(menu, inventory, entity.getDisplayName());
        this.imageHeight = 200;
        this.agol = entity;
    }

    protected void renderBg(GuiGraphics p_282553_, float p_282998_, int p_282929_, int p_283133_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        p_282553_.blit(AGOL_INV, i, j, 0, 0, this.imageWidth, this.imageHeight);


        InventoryScreen.renderEntityInInventoryFollowsMouse(p_282553_, i + 51, j + 60, 17, (float)(i + 51) - this.xMouse, (float)(j + 75 - 50) - this.yMouse, this.agol);
    }

    public void render(GuiGraphics p_281697_, int p_282103_, int p_283529_, float p_283079_) {
        this.renderBackground(p_281697_);
        this.xMouse = (float)p_282103_;
        this.yMouse = (float)p_283529_;
        super.render(p_281697_, p_282103_, p_283529_, p_283079_);
        this.renderTooltip(p_281697_, p_282103_, p_283529_);
    }
}