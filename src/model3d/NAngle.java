package model3d;

import transforms.Point3D;

import java.awt.*;

public class NAngle extends Solid {
    public NAngle(Color color, int thickness, Point3D... points)
    {
        this.color = color;
        this.thickness = thickness;
        int q = 0;
        for (int i = 0; i < points.length; i++) {
            vertices.add(points[i]);
            if (q>0)
            {
                addIndices(q-1,q);
            }
            q++;
        }
        addIndices(q-1,0);
    }
}
