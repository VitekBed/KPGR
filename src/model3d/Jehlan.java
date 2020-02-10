package model3d;

import transforms.Point3D;

import java.awt.*;

public class Jehlan extends Solid {
    public Jehlan(Color color, int thickness, int count)
    {
        this.color = color;
        this.thickness = thickness;

        int q = 0;
        double step = (Math.PI * 2)/(double)count;
        for (int i = 0; i < count; i++) {
            vertices.add(GetCirclePoint(i*step));
            if (q>0)
            {
                addIndices(q-1,q);
            }
            q++;
        }
        addIndices(q-1,0);
        vertices.add(new Point3D(1,0,0));
        for (int i = 0; i < q; i++) {
            addIndices(i,q);
        }
    }

    private Point3D GetCirclePoint(double param) {
        double y = Math.sin(param);
        double z = Math.cos(param);
        return new Point3D(0,y,z);
    }
}
