package com.wadoo.hyperion.common.util;

import net.minecraft.client.animation.Keyframe;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class GrabKeyframe {
    public float xPos;
    public float yPos;
    public float zPos;

    public GrabKeyframe(float xPos, float yPos, float zPos){
        this.xPos = (float) ((float)xPos/16.0);
        this.yPos = (float) ((float)yPos/16.0);
        this.zPos = (float) ((float)zPos/16.0);
    }

    public GrabKeyframe(double xPos, double yPos, double zPos){
        this.xPos = (float) ((float)xPos/16.0);
        this.yPos = (float) ((float)yPos/16.0);
        this.zPos = (float) ((float)zPos/16.0);
    }


    public Vec3 toWorldCoords(LivingEntity entity){
        Vec3 entityPos = entity.position();
        float angle = entity.getYRot();
        return new Vec3(this.xPos,this.yPos,this.zPos).yRot((float)Math.toRadians(-angle)).add(entityPos);


    }

    public Vec3 toWorldCoords(Vec3 positionVec, float yRot){
        Vec3 entityPos = positionVec;
        float angle = yRot;
        return new Vec3(this.xPos,this.yPos,this.zPos).yRot((float)Math.toRadians(-angle)).add(entityPos);
    }
}
