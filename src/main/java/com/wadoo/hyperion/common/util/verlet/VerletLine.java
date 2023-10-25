package com.wadoo.hyperion.common.util.verlet;

import com.wadoo.hyperion.common.util.verlet.*;

public class VerletLine {
    public VerletPoint p1;
    public VerletPoint p2;
    public double length = 1f;
    public VerletLine(VerletPoint p1, VerletPoint p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public VerletLine(VerletPoint p1, VerletPoint p2, double length){
        this.p1 = p1;
        this.p2 = p2;
        this.length = length;
    }
}
