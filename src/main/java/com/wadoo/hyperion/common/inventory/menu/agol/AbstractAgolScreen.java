package com.wadoo.hyperion.common.inventory.menu.agol;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AbstractAgolScreen extends AbstractContainerScreen<AbstractAgolMenu> {
    private static final ResourceLocation AGOL_INV = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/agol_base.png");
    private AbstractAgolEntity horse;
    private final List<AbstractAgolScreen.AgolScreenButton> buttons = Lists.newArrayList();

    private float xMouse;
    private float yMouse;
    private double rotationY = 0;
    private double rotateY = 0;

    public AbstractAgolScreen(AbstractAgolMenu pMenu, Inventory pPlayerInventory, AbstractAgolEntity pHorse) {
        super(pMenu, pPlayerInventory, pHorse.getDisplayName());
        this.horse = pHorse;
        this.imageWidth = 176;
        this.imageHeight = 166;

    }

    @Override
    protected void init() {
        super.init();
        buttons.clear();
        this.addButton(new AgolUpButton(this.leftPos + 80, this.topPos + 15, 8, 5));
        this.addButton(new AgolRightButton(this.leftPos + 102, this.topPos + 35, 5, 8));
        this.addButton(new AgolDownButton(this.leftPos + 80, this.topPos + 57, 8, 5));
        this.addButton(new AgolLeftButton(this.leftPos + 60, this.topPos + 35, 5, 8));


    }

    private <T extends AbstractWidget & AgolButton> void addButton(T button) {
        this.addRenderableWidget(button);
        this.buttons.add((AgolScreenButton) button);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.horse.getDisplayName(), this.inventoryLabelX, this.inventoryLabelY - 4, 16777215, false);
    }

    protected void renderBg(GuiGraphics poseStack, float partialTicks, int x, int y) {
        rotationY += rotateY;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AGOL_INV);
        int i = (this.width - this.imageWidth) / 2 - 4;
        int j = (this.height - this.imageHeight) / 2;
        poseStack.blit(AGOL_INV, i, j, 0, 0, this.imageWidth, this.imageHeight);
        Quaternionf quaternionf = (new Quaternionf().rotateX((float) Math.toRadians(180f)));
        Quaternionf quaternionf1 = (new Quaternionf().rotateX((float) Math.toRadians(35f)).rotateY((float) Math.toRadians(-45f)).rotateY((float) ((float) rotationY)));
        quaternionf.mul(quaternionf1);
        InventoryScreen.renderEntityInInventory(poseStack, i + 88, j + 45, 15, quaternionf, quaternionf1, this.horse );

    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        rotateY = pDragX / 60f;
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double pMouseX, double pMouseY, int pButton) {
        rotateY = 0;
        return super.mouseReleased(pMouseX, pMouseY, pButton);
    }

    public void render(GuiGraphics poseStack, int x, int y, float partialTicks) {
        this.renderBackground(poseStack);
        this.xMouse = (float)x;
        this.yMouse = (float)y;
        super.render(poseStack, x, y, partialTicks);
    }

    @OnlyIn(Dist.CLIENT)
    interface AgolButton {
    }

    @OnlyIn(Dist.CLIENT)
    abstract static class AgolScreenButton extends AbstractButton implements AgolButton {
        private boolean selected;
        private int sizeX;
        private int sizeY;
        private int uvX;
        private int uvY;
        private int real_uvY;
        protected AgolScreenButton(int pX, int pY, int sX,int sY, int uX, int uY) {
            super(pX, pY, sX, sY, CommonComponents.EMPTY);
            sizeX = sX;
            sizeY = sY;
            this.uvX = uX;
            this.uvY = uY;
            this.real_uvY = uvY;
        }

        protected AgolScreenButton(int pX, int pY, int sX, int sY, Component pMessage) {
            super(pX, pY, sX, sY, pMessage);
            sizeX = sX;
            sizeY = sY;
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            if (isHovered){ real_uvY = uvY + 18;}
            else{real_uvY = uvY;}
            pGuiGraphics.blit(AGOL_INV, this.getX(), this.getY(), uvX, real_uvY, sizeX,sizeY);
        }


        public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }

        @Override
        public void onPress(){}
    }

    private class AgolUpButton extends AgolScreenButton{

        protected AgolUpButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY,181, 27);
        }

        @Override
        public void onPress() {
            super.onPress();
            System.out.println("up");
            if (horse.getFirstPassenger() != null && horse.getFirstPassenger() instanceof AbstractAgolEntity) {
                horse = (AbstractAgolEntity) horse.getFirstPassenger();
                menu.setEntity((AbstractAgolEntity) horse.getFirstPassenger());
                menu.setContainer(((AbstractAgolEntity) horse.getFirstPassenger()).getInventory());
            }
        }
    }

    private class AgolLeftButton extends AgolScreenButton{

        protected AgolLeftButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 176, 32);
        }

        @Override
        public void onPress() {
            super.onPress();
            System.out.println("left");
        }
    }

    private class AgolRightButton extends AgolScreenButton{

        protected AgolRightButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 189, 32);
        }

        @Override
        public void onPress() {
            super.onPress();
            System.out.println("right");
        }
    }

    private class AgolDownButton extends AgolScreenButton{

        protected AgolDownButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 181, 40);
        }

        @Override
        public void onPress() {
            super.onPress();
            System.out.println("down");
        }
    }


}