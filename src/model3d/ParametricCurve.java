package model3d;

import transforms.Point3D;

public interface ParametricCurve {
    public Point3D GetPoint(double param);
}
