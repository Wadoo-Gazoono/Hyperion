package com.wadoo.hyperion.common.util.verlet;


public class VerletUtil {
    private static final float GRAV = -0.007f;
    private static final float DRAG = 0.9f;
    private static final float STIFFNESS = 10f;

    public static void movePoint(VerletPoint p){
        if (!p.isAnchored) {
            double dx = (p.x - p.lx) * DRAG;
            double dy = (p.y - p.ly) * DRAG;
            double dz = (p.z - p.lz) * DRAG;
            p.lx = p.x;
            p.ly = p.y;
            p.lz = p.z;
            p.x += dx;
            p.y += dy;
            p.z += dz;
            p.y += GRAV;
        }
    }

    public static void constrainToGround(VerletPoint p, double ground){
        System.out.println(p.y);
        if(p.y < -0.3){
            p.y = -0.3;
            //p.ly = -0.5;
        }
        System.out.println(p.y);
    }

    public static void constrainToLine(VerletLine l) {
        VerletPoint p1 = l.p1;
        VerletPoint p2 = l.p2;
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        double dz = p2.z - p1.z;
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
