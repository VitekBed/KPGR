package model3d;

import transforms.Point3D;

import java.awt.*;

public class Axis extends Solid {
    public Axis(Color color, int dimension, int thickness, int size)
    {
        this.color = color;
        this.thickness = thickness;

        makeIndices(dimension,size,0);
    }
    public Axis(Color color, int dimension, int thickness, int size, int offset1, int offset2)
    {
        this.color = color;
        this.thickness = thickness;

        for (int i = offset1; i <= offset2; i++) {
            makeIndices(dimension,size,i);
        }
    }

    private void makeIndices(int dimension, int size, int offset) {
        int indicesCount = getIndices().size();
        for (int i = 0; i < size; i++) {
            switch (dimension) {
                case 0:
                    vertices.add(new Point3D(i, offset, 0));
                    vertices.add(new Point3D((i+1), offset, 0));
                    //vertices.add(new Point3D(-i, offset, 0));
                    //vertices.add(new Point3D((-i-1), offset, 0));
                    break;
                case 1:
                    vertices.add(new Point3D(offset, i, 0));
                    vertices.add(new Point3D(offset, (i+1), 0));
                    //vertices.add(new Point3D(offset, -i, 0));
                    //vertices.add(new Point3D(offset, (-i-1), 0));
                    break;
                case 2:
                    vertices.add(new Point3D(0, 0, i));
                    vertices.add(new Point3D(0, 0, (i+1)));
                    //vertices.add(new Point3D(0, 0, -i));
                    //vertices.add(new Point3D(0, 0, (-i-1)));
                    break;
                default:
                    throw new IndexOutOfBoundsException("Dimension must be 0 - 2");
            }
            addIndices(indicesCount+2*i, indicesCount+2*i+1);
            //addIndices(indicesCount+4*i+2, indicesCount+4*i+3);
        }
    }
}
