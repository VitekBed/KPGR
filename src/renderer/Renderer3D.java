package renderer;

import model3d.Solid;
import transforms.*;
import view.Raster;

import java.awt.*;
import java.util.List;
import java.util.Optional;

public class Renderer3D implements GPURenderer {
    private final Raster raster;
    private Mat4 model;
    private Mat4 view;
    private Mat4 projection;
    private Mat4 complete;

    public Renderer3D(Raster raster) {
        this.raster = raster;
        this.model = new Mat4Identity();
        this.view = new Mat4Identity();
        this.projection = new Mat4Identity();
    }

    @Override
    public void clear() {
        raster.clear();
    }

    @Override
    public void draw(Solid... solids) {
        complete = model.mul(view).mul(projection);
        for (Solid solid : solids) {
            List<Integer> indices = solid.getIndices();
            List<Point3D> vertices = solid.getVertices();
            for (int i = 0; i < indices.size(); i+=2)
            {
                Point3D a = vertices.get(indices.get(i));
                Point3D b = vertices.get(indices.get(1 + i));
                transformLine(a,b,Color.ORANGE);
            }
        }
    }
    private void transformLine(Point3D a, Point3D b)
    {
        transformLine(a,b,Color.BLACK);
    }
    private void transformLine(Point3D a, Point3D b, Color color)   //pozor, tato metoda nejen transformuje, ale i vykresluje!
    {
        a = a.mul(complete);    //a = a.mul(model).mul(view).mul(projection);
        b = b.mul(complete);
        if (clip(a) || clip(b)) return;

        Optional<Vec3D> dehomogA = a.dehomog(); //dehomogenizujeme
        Optional<Vec3D> dehomogB = b.dehomog();

        if (!dehomogA.isPresent() || !dehomogB.isPresent()) return; //pokud problém tak zahodíme (w == 0)

        Vec3D v1 = dehomogA.get();
        Vec3D v2 = dehomogB.get();
        //po dehomogenizaci musí být vše v rozsahu x,y in <-1,1>; w in <0,1>

        v1 = tranformToWindow(v1);
        v2 = tranformToWindow(v2);

        raster.drawLine(v1.getX(),v1.getY(),v2.getX(),v2.getY(), color);
    }

    private boolean clip(Point3D p) {
        if (p.getW() < p.getX() || p.getX() < -p.getW()) return true;
        if (p.getW() < p.getY() || p.getY() < -p.getW()) return true;
        if (p.getW() < p.getZ() || p.getZ() < 0) return true;   //w si lze představit jako skutečná vzdálenost od pozorovatele

        //celé bychom mohli řešit až po dehomogenizaci, ale to bychom nejprve dělili w (dehomogenizace) a potom teprve porovnávali
        return false;
    }

    private Vec3D tranformToWindow(Vec3D vec)
    {
        return vec
                .mul(new Vec3D(1,-1,1)) //otočení osy Y aby to sedělo do zobrazovacího okna     x,y in <-1,1>
                .add(new Vec3D(1,1,0))  //abychom dostali počátek do levého horního rohu
                .mul(new Vec3D(400,300,1));  //400 = 800/2 šířka vykreslovacího okna, po předchozích transformacích je rozsah hodnot x,y in <0,2>
    }

    @Override
    public void setModel(Mat4 model) {
        this.model = model;
    }

    @Override
    public void setView(Mat4 view) {
        this.view = view;
    }

    @Override
    public void setProjection(Mat4 projection) {
        this.projection = projection;
    }
}
