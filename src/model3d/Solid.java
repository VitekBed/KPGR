package model3d;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Solid {

    final List<Point3D> vertices = new ArrayList<>();
    final List<Integer> indices = new ArrayList<>();
    Color color;
    int thickness = 1;
    Mat4 transformation = new Mat4Identity();

    final void addIndices(Integer... toAdd)
    {
        indices.addAll(Arrays.asList(toAdd));
    }

    public List<Point3D> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public Color getColor() {
        return color;
    }

    public int getThickness() {return thickness;}

    public Mat4 getTransformation() {
        return transformation;
    }

    public void setTransformation(Mat4 transformation) {
        this.transformation = transformation;
    }
}
