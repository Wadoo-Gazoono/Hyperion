package com.arathain.vigorem.api.anim;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;

import javax.swing.text.html.parser.Entity;

public interface ColliderAnimation {
    void damage(Entity entity);

    void render(PoseStack matrices, MultiBufferSource vertexConsumers, LocalPlayer player, Camera camera);
}