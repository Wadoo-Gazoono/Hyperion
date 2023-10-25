package com.wadoo.hyperion.common.inventory.menu.agol;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AgolWalkerScreen extends AbstractContainerScreen<AgolWalkerMenu> {
    private static final ResourceLocation LOCATION = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/agol_core.png");
    public AgolWalkerScreen(AgolWalkerMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
    }

    @Override
    public void render(GuiGraphics poseStack, int i, int j, float f) {
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }

    @Override
    protected void renderBg(GuiGraphics poseStack, float f, int i, int j) {
        this.renderBackground(poseStack);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        poseStack.blit(LOCATION, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }
}
