package com.wadoo.hyperion.client.renderers.entity.effect;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.effect.ChainTestModel;
import com.wadoo.hyperion.common.entities.effects.ChainTestEntity;
import com.wadoo.hyperion.common.registry.ParticleHandler;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

//Credit to https://stackoverflow.com/questions/42609279/how-to-simulate-chain-physics-game-design
public class ChainTestRenderer extends GeoEntityRenderer<ChainTestEntity> {
    private Point point0 = new Point(0f, 0f, 0f);
    private Point point1 = new Point(0f, 1f, 0f);
    private Point point2 = new Point(0f, 2f, 0f);
    private Point point3 = new Point(0f, 3f, 0f);
    private Point[] points = {point0, point1, point2,point3};
    private Line line0 = new Line(point0, point1);
    private Line line1 = new Line(point1, point2);
    private Line line2 = new Line(point2, point3);

    private Line[] lines = {line0, line1,line2};

    private final float bone_length = 1f;
    private final float GRAV = -0.007f;
    private final float DRAG = 0.9f;
    private final float STIFFNESS = 10f;

    public ChainTestRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChainTestModel());
    }

    @Override
    public void preRender(PoseStack poseStack, ChainTestEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        for (Point p : points) {
            //Reset points
            if (animatable.tickCount%100 == 0){
                point0.x = (float) 0;
                point0.lx = -1.f;
                point1.lx = -1.f;

                point2.lx = -1.f;
                point3.lx = -1.f;

                point0.y = (float) 0;
                point0.z = (float) 0;
                point1.x = (float) 0;
                point1.y = (float) 1f;
                point1.z = (float) 0;
                point2.x = (float) 0;
                point2.y = (float) 2f;
                point2.z = (float) 0;
                point3.x = (float) 0;
                point3.y = (float) 3f;
                point3.z = (float) 0;
            }
            movePoint(p);
        }
        for (int i = 0; i < STIFFNESS; i++) {
            for (Line l : lines) {
                constraints(l);
            }
        }
        for (Point p: points){
            if (animatable.level().isClientSide){
                //animatable.level().addParticle(ParticleTypes.ENCHANTED_HIT,p.x,p.y,p.z,0d,0d,0d);
            }
        }
    }

    @Override
    public void renderRecursively(PoseStack poseStack, ChainTestEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);

        if (bone.getName().equals("bone1")) {
            float z = (float) Math.atan2(point1.y,point1.x);
            float x = (float) Math.atan2(point1.y,point1.z);

            //bone.setRotY((float) -(x - Math.PI/2));
            bone.setRotZ((float) -(z - Math.PI/2));
        }
        if (bone.getName().equals("bone2")) {
            float z = (float) Math.atan2(point2.y - point1.y,point2.x - point1.x);
            float x = (float) Math.atan2(point2.y - point1.y,point2.z - point1.z);

            //bone.setRotY((float) +(x + Math.PI/2));
            bone.setRotZ((float) -(z + Math.PI/2));
        }
        if (bone.getName().equals("bone3")) {
            float z = (float) Math.atan2(point3.y - point2.y,point3.x - point2.x);
            float x = (float) Math.atan2(point3.y - point2.y,point3.z - point2.z);

            //bone.setRotY((float) +(x + Math.PI/2));
            bone.setRotZ((float) -(z + Math.PI/2));
        }
    }

    public void movePoint(Point p) {
        if (p != point0) {
            float dx = (p.x - p.lx) * DRAG;
            float dy = (p.y - p.ly) * DRAG;
            float dz = (p.z - p.lz) * DRAG;
            p.lx = p.x;
            p.ly = p.y;
            p.lz = p.z;
            p.x += dx;
            p.y += dy;
            p.z += dz;
            p.y += GRAV;
        }
    }

    public void constraints(Line l) {
        Point p1 = l.p1;
        Point p2 = l.p2;
        float dx = p2.x - p1.x;
        float dy = p2.y - p1.y;
        float dz = p2.z - p1.z;
        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        double fraction = ((l.length - dist) / dist);
        dx *= fraction;
        dy *= fraction;
        dz *= fraction;
        p2.x += dx;
        p2.y += dy;
        p2.z += dz;
    }
}

class Point {
    public float x = 0f;
    public float y = 0f;
    public float z = 0f;
    public float lx = 0f;
    public float ly = 0f;
    public float lz = 0f;

    public Point(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = x - 0.2f;
        this.ly = y;
        this.lz = z;
    }


}

class Line {
    public Point p1;
    public Point p2;
    public float length = 1;

    public Line(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }
}