package com.wadoo.hyperion.common.blocks;

import net.minecraft.util.StringRepresentable;

public enum ElevatorMoveState implements StringRepresentable {
    UP("up"),
    DOWN("down"),
    STANDBY("standby");

    private final String name;

    private ElevatorMoveState(String p_61759_) {
        this.name = p_61759_;
    }

    public String toString() {
        return this.getSerializedName();
    }

    public String getSerializedName() {
        return this.name;
    }
}