package com.arathain.vigorem.api.anim;

import com.arathain.vigorem.anim.OffsetModelPart;
import com.arathain.vigorem.api.Keyframe;
import com.wadoo.hyperion.Hyperion;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public abstract class Animation {
    public final Map<String, List<Keyframe>> keyframes;
    public static final Vector3f ZERO = new Vector3f(0,0,0);
    private final int length;

    public int frame = 0;

    public Animation(int length, Map<String, List<Keyframe>> keyframes) {
        this.length = length;
        this.keyframes = keyframes;
    }

    public boolean shouldTransformHead() {
        return true;
    };

    public boolean shouldRemove() {
        return frame >= length;
    }
    public void serverTick(Player player) {}
    public void clientTick(Player player) {}

    public void tick() {
        this.frame++;
    }

    public int getLength() {
        return length;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public boolean canInterrupt() {
        return false;
    }
    public boolean canCancel(Animation queued) {
        return false;
    }
    public ResourceLocation getId() {
        return new ResourceLocation(Hyperion.MODID, "balls");
    }

    public float getMovementMultiplier() {
        return 1;
    }
    public boolean isAffectingGravity() {
        return false;
    }
    public boolean isBlockingMovement() {
        return false;
    }
    public boolean lockHeldItem(){
        return false;
    }

    public Vector3f getRot(String query, float tickDelta) {
        if(!keyframes.containsKey(query)) {
            return ZERO;
        }
        Keyframe lastFrame = null;
        Keyframe nextFrame = null;
        boolean bl = false;
        for(Keyframe frame : keyframes.get(query)) {
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
        return getRot(lastFrame, nextFrame, tickDelta, bl);
    }
    public Vector3f getOffset(String query, float tickDelta) {
        if(!keyframes.containsKey(query)) {
            return ZERO;
        }
        Keyframe lastFrame = null;
        Keyframe nextFrame = null;
        boolean bl = false;
        for(Keyframe frame : keyframes.get(query)) {
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
        return getOffset(lastFrame, nextFrame, tickDelta, bl);
    }
    public Vector3f getPivot(String query, float tickDelta) {
        if(!keyframes.containsKey(query)) {
            return ZERO;
        }
        Keyframe lastFrame = null;
        Keyframe nextFrame = null;
        boolean bl = false;
        for(Keyframe frame : keyframes.get(query)) {
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
        return getPivot(lastFrame, nextFrame, tickDelta, bl);
    }
    public void writeNbt(CompoundTag nbt) {
        nbt.putInt("time", this.frame);
    }
    public void readNbt(CompoundTag nbt) {
        this.frame = nbt.getInt("time");
    }

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
                case "head" -> setPartAngles(model.head, lastFrame, nextFrame, tickDelta, bl);
                case "body" -> setPartAngles(model.body, lastFrame, nextFrame, tickDelta, bl);
                case "right_arm" -> setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta, bl);
                case "left_arm" -> setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta, bl);
                case "left_leg" -> setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
                case "right_leg" -> setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
                default -> {}
            }
        }
    }
    protected Vector3f getRot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        if(same) {
            return new Vector3f(prev.rotation.x,prev.rotation.y, prev.rotation.z);
        } else {
            float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
            return new Vector3f(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x, next.rotation.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y, next.rotation.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z, next.rotation.z));
        }
    }
    protected Vector3f getPivot(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        if(same) {
            return new Vector3f(prev.translation.x, prev.translation.y, prev.translation.z);
        } else {
            float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
            return new Vector3f(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z));
        }
    }
    protected Vector3f getOffset(Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        if(same) {
            return new Vector3f(prev.offset.x, prev.offset.y, prev.offset.z);
        } else {
            float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
            return new Vector3f(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
        }
    }
    protected void setPartAngles(ModelPart part, Keyframe prev, Keyframe next, float tickDelta, boolean same) {
        if(same) {
            part.setRotation(prev.rotation.x + (!prev.override ? part.xRot : 0),prev.rotation.y + (!prev.override ? part.yRot : 0), prev.rotation.z + (!prev.override ? part.zRot : 0));
            part.offsetPos(new Vector3f(prev.translation.x, prev.translation.y, prev.translation.z));
            part.xScale = 1 + prev.scale.x;
            part.yScale = 1 + prev.scale.y;
            part.zScale = 1 + prev.scale.z;
            ((OffsetModelPart)(Object)part).setOffset(prev.offset.x, prev.offset.y, prev.offset.z);
        } else {
            float percentage = (this.frame + tickDelta - prev.frame) / ((float) next.frame - prev.frame);
            part.setRotation(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.x + (!prev.override ? part.xRot : 0), next.rotation.x + (!next.override ? part.xRot : 0)), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.y + (!prev.override ? part.yRot : 0), next.rotation.y + (!next.override ? part.yRot : 0)), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.rotation.z + (!prev.override ? part.zRot : 0), next.rotation.z + (!next.override ? part.zRot : 0)));
            part.offsetPos(new Vector3f(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.x, next.translation.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.y, next.translation.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.translation.z, next.translation.z)));
            part.xScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.x, next.scale.x);
            part.yScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.y, next.scale.y);
            part.zScale = 1 + Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.scale.z, next.scale.z);
            ((OffsetModelPart)(Object)part).setOffset(Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.x, next.offset.x), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.y, next.offset.y), Mth.lerp(prev.easing.ease(percentage, 0, 1, 1), prev.offset.z, next.offset.z));
        }
    }

    public Vec3 getCameraOffset(float yaw, float tickDelta) {
        Vector3f bodyRot = this.getRot("body", tickDelta);
        Vector3f headRot = this.getRot("head", tickDelta);
        headRot.add(bodyRot);
        return new Vec3(0, -1, 0).add(new Vec3(0, (12d / 16d), 0).yRot(-bodyRot.y).xRot(-bodyRot.x).zRot(bodyRot.z)).add(new Vec3(0, 4/16f, 0).yRot(-headRot.y).xRot(-headRot.x).zRot(-headRot.z)).yRot((float) (yaw * -Math.PI / 180f));
    }
}