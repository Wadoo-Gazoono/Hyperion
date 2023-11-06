package com.wadoo.hyperion.common.inventory.menu.agol;

import com.mojang.blaze3d.systems.RenderSystem;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.renderers.block.AgraliteCageRenderer;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

@OnlyIn(Dist.CLIENT)
public class AbstractAgolScreen extends AbstractContainerScreen<AbstractAgolMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation(Hyperion.MODID, "textures/gui/container/agol/agol_base.png");
    private final AbstractAgolEntity horse;
    private float xMouse;
    private float yMouse;

    public AbstractAgolScreen(AbstractAgolMenu pMenu, Inventory pPlayerInventory, AbstractAgolEntity pHorse) {
        super(pMenu, pPlayerInventory, pHorse.getDisplayName());
        this.horse = pHorse;
        this.imageWidth = 256;
        this.imageHeight = 185;
    }

    protected void renderBg(GuiGraphics poseStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, HORSE_INVENTORY_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        poseStack.blit(HORSE_INVENTORY_LOCATION, i, j, 0, 0, this.imageWidth, this.imageHeight);
        Quaternionf quaternionf = (new Quaternionf().rotateX((float) Math.toRadians(180f)));
        Quaternionf quaternionf1 = (new Quaternionf().rotateX((float) Math.toRadians(35.0f)));

        quaternionf.mul(quaternionf1);
        InventoryScreen.renderEntityInInventory(poseStack, i + 97, j + 80, 15, quaternionf, quaternionf1, this.horse );
    }

    public void render(GuiGraphics poseStack, int x, int y, float partialTicks) {
        this.renderBackground(poseStack);
        this.xMouse = (float)x;
        this.yMouse = (float)y;
        super.render(poseStack, x, y, partialTicks);
        int text_offset = 14;
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        poseStack.drawString(this.font, String.valueOf(this.horse.getHealth()), i + 30, j + 20, 16777215, false);
        poseStack.drawString(this.font, String.valueOf(this.horse.getArmorValue()), i + 30, j + 20 + (text_offset * 1), 16777215, false);
        poseStack.drawString(this.font, String.valueOf(this.horse.getAttributeValue(Attributes.ATTACK_DAMAGE)), i + 30, j + 20 + (text_offset * 2), 16777215, false);
        poseStack.drawString(this.font, String.valueOf(this.horse.getHealth()), i + 30, j + 20 + (text_offset * 3), 16777215, false);

        this.renderTooltip(poseStack, x, y);
    }

}