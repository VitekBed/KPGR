package model3d;

import transforms.Cubic;
import transforms.Mat4;
import transforms.Point3D;

import java.awt.*;

public abstract class Curve extends Solid implements ParametricCurve {
    final private Cubic cubic;

    abstract protected Mat4 GetBaseMat();
    private Mat4 baseMat;
    public Curve(Color color, int thickness, Point3D p1, Point3D p2, Point3D p3, Point3D p4, int steps) {
        this.color = color;
        this.thickness = thickness;
        baseMat = GetBaseMat();
        cubic = new Cubic(baseMat,p1,p2,p3,p4);
        int q = 0;
        for (double i = 0; i <= 1.01; i+= 1f/steps  ) {
            vertices.add(GetPoint(i));
            if (q>0)
            {
                addIndices(q-1,q);
            }
            q++;
        }
    }

    @Override
    final public Point3D GetPoint(double param) {
        return cubic.compute(param);
    }
}
