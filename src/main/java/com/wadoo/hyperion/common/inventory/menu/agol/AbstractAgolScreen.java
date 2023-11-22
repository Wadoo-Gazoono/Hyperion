package com.wadoo.hyperion.common.inventory.menu.agol;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.network.NetworkHandler;
import com.wadoo.hyperion.common.network.OpenAgolScreenClientPacket;
import com.wadoo.hyperion.common.network.OpenAgolScreenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.BeaconScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.joml.Quaternionf;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class AbstractAgolScreen extends AbstractContainerScreen<AbstractAgolMenu> {
    private AbstractAgolEntity horse;
    private AbstractAgolMenu menu;
    private Inventory inventory;
    private final List<AbstractAgolScreen.AgolScreenButton> buttons = Lists.newArrayList();

    private double rotationY = 0;
    private double rotateY = 0;

    public AbstractAgolScreen(AbstractAgolMenu pMenu, Inventory pPlayerInventory, AbstractAgolEntity pHorse) {
        super(pMenu, pPlayerInventory, pHorse.getDisplayName());
        this.horse = pHorse;
        this.menu = pMenu;
        this.inventory = pPlayerInventory;
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
        pGuiGraphics.drawCenteredString(this.font, this.horse.getDisplayName(), this.imageWidth/2 - 4, this.inventoryLabelY - 4, 16777215);
    }

    protected void renderBg(GuiGraphics poseStack, float partialTicks, int x, int y) {
        rotationY += rotateY;
        ResourceLocation AGOL_INV = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/" + this.horse.getAgolName() +".png");
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, AGOL_INV);
        int i = (this.width - this.imageWidth) / 2 - 4;
        int j = (this.height - this.imageHeight) / 2;
        poseStack.blit(AGOL_INV, i, j, 0, 0, this.imageWidth, this.imageHeight);
        poseStack.blit(AGOL_INV, i + 20, j - 24, 176, 0, 28,27);
        poseStack.blit(AGOL_INV, i + 50, j - 24, 176, 0, 28,24);

        Quaternionf quaternionf = (new Quaternionf().rotateX((float) Math.toRadians(180f)));
        Quaternionf quaternionf1 = (new Quaternionf().rotateX((float) Math.toRadians(3f)).rotateY((float) Math.toRadians(-45f)).rotateY((float) ((float) rotationY)));
        quaternionf.mul(quaternionf1);
        poseStack.pose().pushPose();
        poseStack.pose().scale(0.8f,0.8f,0.8f);
        InventoryScreen.renderEntityInInventory(poseStack, i + 146, j + 77, 15, quaternionf, quaternionf1, this.horse );
        poseStack.pose().scale(1f,1f,1f);
        poseStack.pose().popPose();
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
        protected boolean isValidClickButton(int pButton) {
            return super.isValidClickButton(pButton);
        }

        @Override
        public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            if (isHovered){ real_uvY = uvY + 18;}
            else{real_uvY = uvY;}
            ResourceLocation AGOL_INV = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/agol_base.png");

            pGuiGraphics.blit(AGOL_INV, this.getX(), this.getY(), uvX, real_uvY, sizeX,sizeY);
        }


        public void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
            this.defaultButtonNarrationText(pNarrationElementOutput);
        }

        @Override
        public void onPress(){}

        public AbstractAgolScreen openInventory(Player player, AbstractAgolEntity entity){
            SimpleContainer inventory = new SimpleContainer(12);
            Inventory playerInventory = player.getInventory();
            AbstractAgolMenu reindeerContainer = new AbstractAgolMenu(5, playerInventory, inventory, entity);
            player.containerMenu = reindeerContainer;
            AbstractAgolScreen reindeerScreen = new AbstractAgolScreen(reindeerContainer, playerInventory, entity);
            return reindeerScreen;
        }


    }


    private class AgolUpButton extends AgolScreenButton{
        protected Player player = AbstractAgolScreen.this.minecraft.player;
        protected AgolUpButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY,181, 27);
        }

        @Override
        protected boolean isValidClickButton(int pButton) {
            return AbstractAgolScreen.this.horse.getAboveEntity() != null;
        }

        @Override
        public void onPress() {
            super.onPress();
            if (AbstractAgolScreen.this.horse.isVehicle()){
                NetworkHandler.sendToServer(new OpenAgolScreenClientPacket(AbstractAgolScreen.this.horse.getId(), (byte) 1));
            }
        }
    }

    private class AgolLeftButton extends AgolScreenButton{
        protected Player player = AbstractAgolScreen.this.minecraft.player;
        protected AgolLeftButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 176, 32);
        }


        @Override
        protected boolean isValidClickButton(int pButton) {
            return AbstractAgolScreen.this.horse.getLeftEntity() != null;
        }
        @Override
        public void onPress() {
            super.onPress();
            //if (AbstractAgolScreen.this.horse.isPassenger()){

            NetworkHandler.sendToServer(new OpenAgolScreenClientPacket(AbstractAgolScreen.this.horse.getId(), (byte) 3));
        }
    }

    private class AgolRightButton extends AgolScreenButton{
        protected Player player = AbstractAgolScreen.this.minecraft.player;
        protected AgolRightButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 189, 32);
        }


        @Override
        protected boolean isValidClickButton(int pButton) {
            return AbstractAgolScreen.this.horse.getRightEntity() != null;
        }

        @Override
        public void onPress() {
            super.onPress();
            NetworkHandler.sendToServer(new OpenAgolScreenClientPacket(AbstractAgolScreen.this.horse.getId(), (byte) 4));
        }
    }

    private class AgolDownButton extends AgolScreenButton{
        protected Player player = AbstractAgolScreen.this.minecraft.player;
        protected AgolDownButton(int pX, int pY, int sX, int sY) {
            super(pX, pY, sX, sY, 181, 40);
        }


        @Override
        protected boolean isValidClickButton(int pButton) {
            return AbstractAgolScreen.this.horse.getBelowEntity() != null;
        }

        @Override
        public void onPress() {
            super.onPress();
            if (AbstractAgolScreen.this.horse.isPassenger()){
                NetworkHandler.sendToServer(new OpenAgolScreenClientPacket(AbstractAgolScreen.this.horse.getId(), (byte) 2));
            }
        }
    }


}