package model3d;

import transforms.Mat4;
import transforms.Point3D;

import java.awt.*;

public class FergusonCurve extends Curve {
    @Override
    protected Mat4 GetBaseMat() {
        return new Mat4(new double[][]{{2,-2,1,1},{-3,3,-2,-1},{0,0,1,0},{1,0,0,0}});
    }

    public FergusonCurve(Color color, int thickness, Point3D p1, Point3D p2, Point3D p3, Point3D p4, int steps) {
        super(color, thickness, p1, p2, p3, p4, steps);
    }
}
