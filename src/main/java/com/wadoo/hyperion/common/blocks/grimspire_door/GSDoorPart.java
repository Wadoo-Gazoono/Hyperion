package com.wadoo.hyperion.common.blocks.grimspire_door;

import net.minecraft.util.StringRepresentable;

public enum GSDoorPart implements StringRepresentable {
    KEY("key"),
    DOOR("door"),
    START("start");

    private final String name;

    private GSDoorPart(String p_61339_) {
        this.name = p_61339_;
    }

    public String toString() {
        return this.name;
    }

    public String getSerializedName() {
        return this.name;
    }
}
