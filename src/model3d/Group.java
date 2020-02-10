package model3d;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Group extends Solid {
    private List<Solid> solidList;
    public Group()
    {
        this.solidList = new ArrayList<>();
    }
    public Group(List<Solid> solidList)
    {
        this.solidList = new ArrayList<>(solidList);
    }
    public Group(Solid... solids)
    {
        this.solidList = new ArrayList();
        this.solidList.addAll(Arrays.asList(solids));
    }
    public List<Solid> getSolidList() {
        return solidList;
    }

    void setSolidList(List<Solid> solidList) {
        this.solidList = new ArrayList<>(solidList);
    }
    public void addSolid(Solid solid)
    {
        this.solidList.add(solid);
    }

    @Override
    public List<Point3D> getVertices() {
        List<Point3D> returnList = new ArrayList<>();
        for (Solid solid : solidList) {
            returnList.addAll(solid.getVertices());
        }
        return  returnList;
    }

    @Override
    public List<Integer> getIndices() {
        List<Integer> returnList = new ArrayList<>();
        for (Solid solid : solidList)
        {
            returnList.addAll(solid.getIndices());
        }
        return returnList;
    }

    @Override
    public Mat4 getTransformation() {
        return super.getTransformation();
    }

    @Override
    public void setTransformation(Mat4 transformation) {
        this.transformation = (this.transformation.mul(transformation));
        for (Solid solid : solidList)
        {
            solid.setTransformation(solid.getTransformation().mul(transformation));
        }
    }
}
