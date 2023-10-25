package com.wadoo.hyperion.common.util.verlet;

public class VerletPoint {
    public double x;
    public double y;
    public double z;
    public double lx;
    public double ly;
    public double lz;
    public boolean isAnchored = false;
    public VerletPoint(double x, double y, double z, boolean anchored){
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = x;
        this.ly = y;
        this.lz = z;
        this.isAnchored = anchored;
    }
    public VerletPoint(double x, double y, double z, double lx, double ly, double lz, boolean anchored){
        this.x = x;
        this.y = y;
        this.z = z;
        this.lx = lx;
        this.ly = ly;
        this.lz = lz;
        this.isAnchored = anchored;
    }
}
