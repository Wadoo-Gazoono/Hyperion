package com.arathain.vigorem.api.box;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

/**
 * Literally just <a href="https://github.com/ZsoltMolnarrr/BetterCombat/blob/1.19.2/common/src/main/java/net/bettercombat/client/collision/OrientedBoundingBox.java">this code from BetterCombat (pre-license change, thus licensed as CC0)</a>
 * Praise the god code \o/
 * @author ZsoltMolnarrr
 */
public class OrientedBox {

    // TOPOLOGY

    // Y ^       8   +-------+   7     axisY   axisZ
    //   |          /|      /|             | /
    //   |     4   +-------+ | 3           |/
    //   |  Z      | |     | |             +-- axisX
    //   |   /   5 | +-----|-+  6       Center
    //   |  /      |/      |/
    //   | /   1   +-------+   2
    //   |/
    //   +--------------------> X

    // DEFINITIVE PROPERTIES

    // Center position of the cuboid
    public Vec3 center;

    // Extent defines the half size in all directions
    public Vec3 extent;

    // Orthogonal basis vectors define orientation
    public Vec3 axisX, axisY, axisZ;

    // DERIVED PROPERTIES
    public Vec3 scaledAxisX, scaledAxisY, scaledAxisZ;
    public Matrix3f rotation = new Matrix3f();
    public Vec3 vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8;
    public Vec3[] vertices;

    // 1. CONSTRUCT

    public OrientedBox(Vec3 center, double width, double height, double depth, float pitch, float yaw) {
        this.center = center;
        this.extent = new Vec3(width/2.0, height/2.0, depth/2.0);
        this.axisZ = Vec3.directionFromRotation(pitch, yaw).normalize();
        this.axisY = Vec3.directionFromRotation(pitch + 90, yaw).reverse().normalize();
        this.axisX = axisZ.cross(axisY);
    }

    public OrientedBox(Vec3 center, Vec3 size, float pitch, float yaw) {
        this(center,size.x, size.y, size.z, pitch, yaw);
    }
    public OrientedBox(Vec3 center, Vec3 size, float pitch, float yaw, float roll) {
        this.center = center;
        this.extent = new Vec3(size.x/2.0, size.y/2.0, size.z/2.0);
        this.axisZ = Vec3.directionFromRotation(pitch, yaw).zRot(roll * (float) (Math.PI / 180.0)).normalize();
        this.axisY = Vec3.directionFromRotation(pitch + 90, yaw).reverse().zRot(roll * (float) (Math.PI / 180.0)).normalize();
        this.axisX = axisZ.cross(axisY);
    }


    public OrientedBox(AABB box) {
        this.center = new Vec3((box.maxX + box.minX) / 2.0, (box.maxY + box.minY) / 2.0, (box.maxZ + box.minZ) / 2.0);
        this.extent = new Vec3(Math.abs(box.maxX - box.minX) / 2.0, Math.abs(box.maxY - box.minY) / 2.0, Math.abs(box.maxZ - box.minZ) / 2.0);
        this.axisX = new Vec3(1, 0, 0);
        this.axisY = new Vec3(0, 1, 0);
        this.axisZ = new Vec3(0, 0, 1);
    }

    public OrientedBox(OrientedBox obb) {
        this.center = obb.center;
        this.extent = obb.extent;
        this.axisX = obb.axisX;
        this.axisY = obb.axisY;
        this.axisZ = obb.axisZ;
    }

    public OrientedBox copy() {
        return new OrientedBox(this);
    }

    // 2. CONFIGURE

    public OrientedBox offsetX(double offset) {
        this.center = this.center.add(axisX.multiply(offset,offset,offset));
        return this;
    }

    public OrientedBox offsetY(double offset) {
        this.center = this.center.add(axisY.multiply(offset,offset,offset));
        return this;
    }

    public OrientedBox offsetZ(double offset) {
        this.center = this.center.add(axisZ.multiply(offset,offset,offset));
        return this;
    }

    public OrientedBox offset(Vec3 offset) {
        this.center = this.center.add(offset);
        return this;
    }

    public OrientedBox scale(double scale) {
        this.extent = this.extent.multiply(scale,scale,scale);
        return this;
    }

    // 3. UPDATE

    public OrientedBox updateVertex() {
        rotation.set(0,0, (float) axisX.x);
        rotation.set(0,1, (float) axisX.y);
        rotation.set(0,2, (float) axisX.z);
        rotation.set(1,0, (float) axisY.x);
        rotation.set(1,1, (float) axisY.y);
        rotation.set(1,2, (float) axisY.z);
        rotation.set(2,0, (float) axisZ.x);
        rotation.set(2,1, (float) axisZ.y);
        rotation.set(2,2, (float) axisZ.z);

        scaledAxisX = axisX.multiply(extent);
        scaledAxisY = axisY.multiply(extent);
        scaledAxisZ = axisZ.multiply(extent);

        vertex1 = center.subtract(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex2 = center.subtract(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex3 = center.subtract(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex4 = center.subtract(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);
        vertex5 = center.add(scaledAxisZ).subtract(scaledAxisX).subtract(scaledAxisY);
        vertex6 = center.add(scaledAxisZ).add(scaledAxisX).subtract(scaledAxisY);
        vertex7 = center.add(scaledAxisZ).add(scaledAxisX).add(scaledAxisY);
        vertex8 = center.add(scaledAxisZ).subtract(scaledAxisX).add(scaledAxisY);

        vertices = new Vec3[]{  vertex1, vertex2, vertex3, vertex4, vertex5, vertex6, vertex7, vertex8  };

        return this;
    }

    // 4. CHECK INTERSECTIONS

    public boolean contains(Vec3 point) {

        Vector3f distance = new Vector3f((Vector3fc) point.subtract(center));

        //TODO check what Vec3f.transform is
        distance.add((Vector3fc) rotation);
        return Math.abs(distance.x()) < extent.x &&
                Math.abs(distance.y()) < extent.y &&
                Math.abs(distance.z()) < extent.z;
    }

    public boolean intersects(AABB boundingBox) {
        OrientedBox otherOBB = new OrientedBox(boundingBox).updateVertex();
        return intersects(this, otherOBB);
    }

    public boolean intersects(OrientedBox otherBox) {
        return intersects(this, otherBox);
    }

    /**
     * Calculates if there is intersection between given OBBs.
     * Separating Axes Theorem implementation.
     */
    public static boolean intersects(OrientedBox a, OrientedBox b)  {
        if (separated(a.vertices, b.vertices, a.scaledAxisX) || separated(a.vertices, b.vertices, a.scaledAxisY) || separated(a.vertices, b.vertices, a.scaledAxisZ))
            return false;

        if (separated(a.vertices, b.vertices, b.scaledAxisX) || separated(a.vertices, b.vertices, b.scaledAxisY) || separated(a.vertices, b.vertices, b.scaledAxisZ))
            return false;

        if (separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisX)) || separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisY)) || separated(a.vertices, b.vertices, a.scaledAxisX.cross(b.scaledAxisZ)))
            return false;

        if (separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisX)) || separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisY)) || separated(a.vertices, b.vertices, a.scaledAxisY.cross(b.scaledAxisZ)))
            return false;

        return !separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisX)) && !separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisY)) && !separated(a.vertices, b.vertices, a.scaledAxisZ.cross(b.scaledAxisY));
    }

    private static boolean separated(Vec3[] vertsA, Vec3[] vertsB, Vec3 axis)  {
        // Handles the crossProduct product = {0,0,0} case
        if (axis.equals(Vec3.ZERO))
            return false;

        double aMin = Double.POSITIVE_INFINITY;
        double aMax = Double.NEGATIVE_INFINITY;
        double bMin = Double.POSITIVE_INFINITY;
        double bMax = Double.NEGATIVE_INFINITY;

        // Define two intervals, a and b. Calculate their min and max values
        for (int i = 0; i < 8; i++)
        {
            double aDist = vertsA[i].dot(axis);
            aMin = Math.min(aDist, aMin);
            aMax = Math.max(aDist, aMax);
            double bDist = vertsB[i].dot(axis);
            bMin = Math.min(bDist, bMin);
            bMax = Math.max(bDist, bMax);
        }

        // One-dimensional intersection test between a and b
        double longSpan = Math.max(aMax, bMax) - Math.min(aMin, bMin);
        double sumSpan = aMax - aMin + bMax - bMin;
        return longSpan >= sumSpan; // > to treat touching as intersection
    }
}