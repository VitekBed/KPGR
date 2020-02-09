package model3d;

import transforms.Point3D;

import java.awt.*;

public class Spiral extends Solid implements ParametricCurve {
    public Spiral(Color color, int thickness, double minParam, double maxParam, double step)
    {
        this.color = color;
        this.thickness = thickness;

        int q = 0;
        for (double param = minParam; param <= maxParam; param += step) {
            vertices.add(GetPoint(param));
            if (q>0)
            {
                addIndices(q-1,q);
            }
            q++;
        }


    }

    @Override
    public Point3D GetPoint(double param) {
        double x = param;
        double y = Math.sin(param);
        double z = Math.cos(param);
        return new Point3D(x,y,z);
    }
}
