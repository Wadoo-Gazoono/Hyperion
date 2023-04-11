package com.arathain.vigorem.api.anim;

import com.arathain.vigorem.anim.OffsetModelPart;
import com.arathain.vigorem.api.Keyframe;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public class MirrorableAnimation extends Animation {
    protected final boolean mirrored;
    public MirrorableAnimation(int length, Map<String, List<Keyframe>> keyframes, boolean mir) {
        super(length, keyframes);
        this.mirrored = mir;
    }
    @Override
    public void setModelAngles(PlayerModel<AbstractClientPlayer> model, Player player, float tickDelta) {
        for(String part : keyframes.keySet()) {
            Keyframe lastFrame = null;
            Keyframe nextFrame = null;
            boolean bl = false;
            for(Keyframe frame : keyframes.get(part)) {
                if(frame.frame == (this.frame + tickDelta)) {
                    lastFrame = frame;
                    nextFrame = frame;
                    bl = true;
                }
                if(lastFrame == null && frame.frame < (this.frame + tickDelta)) {
                    lastFrame = frame;
                } else {
                    if(lastFrame != null && frame.frame > lastFrame.frame && frame.frame < (this.frame + tickDelta)) {
                        lastFrame = frame;
                    }
                }
                if(nextFrame == null && frame.frame > (this.frame + tickDelta)) {
                    nextFrame = frame;
                } else {
                    if(nextFrame != null && frame.frame < nextFrame.frame && frame.frame > (this.frame + tickDelta)) {
                        nextFrame = frame;
                    }
                }
            }
            assert lastFrame != null;
            if(nextFrame == null) {
                nextFrame = lastFrame;
            }
            switch(part) {
                case "head" -> {
                    setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
                }
                case "body" -> {
                    setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
                }
                case "left_arm" -> {
                    setPartAngles(mirrored ? model.rightArm : model.leftArm, lastFrame, nextFrame, tickDelta, bl);
                }
                case "right_arm" -> {
                    setPartAngles(mirrored ? model.leftArm : model.rightArm, lastFrame, nextFrame, tickDelta, bl);
                }
                case "right_leg" -> {
                    setPartAngles(mirrored ? model.leftLeg : model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
                }
                case "left_leg" -> {
                    setPartAngles(mirrored ? model.rightLeg : model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
                }
                default -> {}
            }
        }
    }
    public Vector3f getRot(String query, float tickDelta) {
        if(mirrored) {
            switch (query) {
                case "left_arm" -> query = "right_arm";
                case "right_arm" -> query = "left_arm";
                case "right_leg" -> query = "left_leg";
                case "left_leg" -> query = "right_leg";
                case "left_hand" -> query = "right_hand";
                case "right_hand" -> query = "left_hand";
                default -> {}
            }
        }
        return super.getRot(query, tickDelta);
    }
    public Vector3f getPivot(String query, float tickDelta) {
        if(mirrored) {
            switch (query) {
                case "left_arm" -> query = "right_arm";
                case "right_arm" -> query = "left_arm";
                case "right_leg" -> query = "left_leg";
                case "left_leg" -> query = "right_leg";
                case "left_hand" -> query = "right_hand";
                case "right_hand" -> query = "left_hand";
                default -> {}
            }
        }
        return super.getPivot(query, tickDelta);
    }
    public Vector3f getOffset(String query, float tickDelta) {
        if(mirrored) {
            switch (query) {
                case "left_arm" -> query = "right_arm";
                case "right_arm" -> query = "left_arm";
                case "right_leg" -> query = "left_leg";
                case "left_leg" -> query = "right_leg";
                case "left_hand" -> query = "right_hand";
                case "right_hand" -> query = "left_hand";
                default -> {}
            }
        }
        return super.getOffset(query, tickDelta);
    }

    @Override
    protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        if(this.mirrored) {
            prev = new Keyframe(prev.easing, prev.translation, new Vector3f(prev.rotation.x, -prev.rotation.y, -prev.rotation.z), prev.scale, prev.offset, prev.frame, prev.override);
            next = new Keyframe(next.easing, next.translation, new Vector3f(next.rotation.x, -next.rotation.y, -next.rotation.z), next.scale, next.offset, next.frame, next.override);
        }
        if(same) {
            part.setRotation(prev.rotation.x + (!prev.override ? part.xRot : 0),prev.rotation.y + (!prev.override ? part.yRot : 0), prev.rotation.z + (!prev.override ? part.zRot : 0));
            part.setPos(part.x + prev.translation.x, part.y + prev.translation.y, part.z + prev.translation.z);
            part.xScale = 1 + prev.scale.x;
            part.yScale = 1 + prev.scale.y;
            part.zScale = 1 + prev.scale.z;
            ((OffsetModelPart)(Object)part).setOffset(prev.offset.x, prev.offset.y, prev.offset.z);
        } else {
            float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
            part.setRotation(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x + (!prev.override ? part.xRot : 0), next.rotation.x + (!next.override ? part.xRot : 0)), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y + (!prev.override ? part.yRot : 0), next.rotation.y + (!next.override ? part.yRot : 0)), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z + (!prev.override ? part.zRot : 0), next.rotation.z + (!next.override ? part.zRot : 0)));
            part.setPos(part.x + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), part.y + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), part.z + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z));
            part.xScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.x, next.scale.x);
            part.yScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.y, next.scale.y);
            part.zScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.z, next.scale.z);
            ((OffsetModelPart)(Object)part).setOffset(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
        }
        if(this.mirrored) {
            ((OffsetModelPart)(Object)part).setOffset(-((OffsetModelPart)(Object)part).getOffsetX(), ((OffsetModelPart)(Object)part).getOffsetY(), -((OffsetModelPart)(Object)part).getOffsetZ());
        }
    }
    protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        Vector3f soup = super.getRot(prev, next, tickDelta, same);
        if(mirrored) {
            soup = new Vector3f(soup.x, -soup.y, -soup.z);
        }
        return soup;
    }
    protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        Vector3f soup = super.getPivot(prev, next, tickDelta, same);
        if(mirrored) {
            soup = new Vector3f(-soup.x, soup.y, -soup.z);
        }
        return soup;
    }
    protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        Vector3f soup = super.getOffset(prev, next, tickDelta, same);
        if(mirrored) {
            soup = new Vector3f(-soup.x, soup.y, -soup.z);
        }
        return soup;
    }
}