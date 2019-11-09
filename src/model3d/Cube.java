package model3d;

import transforms.Point3D;

import java.awt.*;

public class Cube extends Solid {
    public Cube(Color color) {
        this.color = color;
        // nejprve vytvořím body které reprezentují kryc
        // -v- spodní podstava
        vertices.add(new Point3D(-1,-1,-1));
        vertices.add(new Point3D(1,-1,-1));
        vertices.add(new Point3D(1,-1,1));
        vertices.add(new Point3D(-1,-1,1));
        // -^- spodní podstava
        // -v- dolní podstava
        vertices.add(new Point3D(-1,1,-1));
        vertices.add(new Point3D(1,1,-1));
        vertices.add(new Point3D(1,1,1));
        vertices.add(new Point3D(-1,1,1));
        // -^- dolní podstava

        addIndices(0,1,1,2,2,3,3,0); //spodní podstava
        addIndices(4,5,5,6,6,7,7,4); //horní podstava
        addIndices(0,4,1,5,2,6,3,7); //vertikály
    }

    public Cube() {
        this(Color.RED);    //voláme honstruktor this(Color color)
    }
}
