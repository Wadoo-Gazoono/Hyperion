package com.arathain.vigorem.api.anim;

import com.arathain.vigorem.api.Keyframe;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;

public abstract class ExtendableAnimation extends Animation {
    public final Map<String, List<Keyframe>> endKeyframes;
    private final int endLength;
    protected int stage;
    public static final Vector3f ZERO = new Vector3f(0,0,0);
    public ExtendableAnimation(int initLength, Map<String, List<Keyframe>> initKeyframes, int endLength, Map<String, List<Keyframe>> endKeyframes) {
        super(initLength, initKeyframes);
        this.endKeyframes = endKeyframes;
        this.endLength = endLength;
    }
    public boolean shouldRemove() {
        return frame >= endLength && stage == 2;
    }

    public void tick() {
        if(stage == 2 || stage == 0) {
            super.tick();
        }
        if(frame >= getLength() && stage == 0) {
            stage = 1;
        }
        if(stage == 1) {
            codeTick();
        }
    }

    @Override
    public Vector3f getRot(String query, float tickDelta) {
        if(!keyframes.containsKey(query)) {
            return ZERO;
        }
        if(stage == 1) {
            return this.getCodeRot(query, tickDelta);
        } else if(stage == 2) {
            Keyframe lastFrame = null;
            Keyframe nextFrame = null;
            boolean bl = false;
            for(Keyframe frame : endKeyframes.get(query)) {
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
        return super.getRot(query, tickDelta);
    }
    protected abstract Vector3f getCodeRot(String query, float tickDelta);

    @Override
    public Vector3f getPivot(String query, float tickDelta) {
        if(!keyframes.containsKey(query)) {
            return ZERO;
        }
        if(stage == 1) {
            return this.getCodePivot(query, tickDelta);
        } else if(stage == 2) {
            Keyframe lastFrame = null;
            Keyframe nextFrame = null;
            boolean bl = false;
            for(Keyframe frame : endKeyframes.get(query)) {
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
        return super.getPivot(query, tickDelta);
    }

    protected abstract Vector3f getCodePivot(String query, float tickDelta);

    public Vector3f getOffsetSuper(String query, float tickDelta) {
        return super.getOffset(query, tickDelta);
    }
    public Vector3f getPivotSuper(String query, float tickDelta) {
        return super.getPivot(query, tickDelta);
    }
    public Vector3f getRotSuper(String query, float tickDelta) {
        return super.getRot(query, tickDelta);
    }

    @Override
    public Vec3 getCameraOffset(float yaw, float tickDelta) {
        Vector3f bodyRot = this.getRot("body", tickDelta);
        Vector3f headRot = this.getRot("head", tickDelta);
        return new Vec3(0, -1, 0).add(new Vec3(0, (12d / 16d), 0).yRot(-bodyRot.y).xRot(-bodyRot.x).zRot(bodyRot.z)).add(new Vec3(0, 4/16f, 0).yRot(-headRot.y).xRot(-headRot.x).zRot(-headRot.z)).yRot((float) (yaw * -Math.PI / 180f));
    }

    @Override
    public Vector3f getOffset(String query, float tickDelta) {
        if(!endKeyframes.containsKey(query)) {
            return ZERO;
        }
        if(stage == 1) {
            return this.getCodeOffset(query, tickDelta);
        } else if(stage == 2) {
            Keyframe lastFrame = null;
            Keyframe nextFrame = null;
            boolean bl = false;
            for(Keyframe frame : endKeyframes.get(query)) {
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
        return super.getOffset(query, tickDelta);
    }

    protected abstract Vector3f getCodeOffset(String query, float tickDelta);

    protected void setModelAnglesSuper(PlayerModel<AbstractClientPlayer> model, Player player, float tickDelta) {
        super.setModelAngles(model, player, tickDelta);
    }

    @Override
    public void readNbt(CompoundTag nbt) {
        super.readNbt(nbt);
        this.stage = nbt.getInt("stage");
    }

    @Override
    public void writeNbt(CompoundTag nbt) {
        super.writeNbt(nbt);
        nbt.putInt("stage", this.stage);
    }

    @Override
    public void setModelAngles(PlayerModel<AbstractClientPlayer> model, Player player, float tickDelta) {
        switch(this.stage) {
            case 0 -> super.setModelAngles(model, player, tickDelta);
            case 1 -> this.setCodeModelAngles(model, player, tickDelta);
            case 2 -> {
                for(String part : endKeyframes.keySet()) {
                    Keyframe lastFrame = null;
                    Keyframe nextFrame = null;
                    boolean bl = false;
                    for(Keyframe frame : endKeyframes.get(part)) {
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
                        case "right_arm" -> {
                            setPartAngles(model.rightArm, lastFrame, nextFrame, tickDelta, bl);
                        }
                        case "left_arm" -> {
                            setPartAngles(model.leftArm, lastFrame, nextFrame, tickDelta, bl);
                        }
                        case "left_leg" -> {
                            setPartAngles(model.leftLeg, lastFrame, nextFrame, tickDelta, bl);
                        }
                        case "right_leg" -> {
                            setPartAngles(model.rightLeg, lastFrame, nextFrame, tickDelta, bl);
                        }
                        default -> {}
                    }
                }
            }
        }
    }
    public void update() {
        this.stage = 2;
        this.frame = 0;
    }
    protected abstract void setCodeModelAngles(PlayerModel<AbstractClientPlayer> model, Player player, float tickDelta);

    protected abstract void codeTick();
    public boolean shouldEnd(Player player) {
        return stage == 1;
    }
}