package controller;

import model3d.Cube;
import model3d.Solid;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Raster;

import java.util.ArrayList;

public class Controller3D
{
    private final GPURenderer renderer;
    private final Camera camera;
    private final double pi = Math.PI;
    private final ArrayList<Solid> solids;
    private final Solid[] axis;
    private Mat4 model, view, projection;

    public Controller3D(Raster raster) {
        this.renderer = new Renderer3D(raster);

        model = new Mat4Identity(); //základní model bez úprav

        camera = new Camera()
                .withPosition(new Vec3D(0,-12,0))    //kamera odkud se díváme (pozice)
                .withAzimuth(Math.toRadians(90))  //rotace v ose y
                .withZenith(Math.toRadians(20));  //rotace v ose x (limitovaný obvykle na -pi/2 to +pi/2)
        view = camera.getViewMatrix();
        projection = new Mat4PerspRH(pi/3,  //zorný úhel
                                    600/800f,  //poměr výška/šířka okna;
                                    0.1,      //nejkratší zdobrazená vzdálenost
                                    20);      //nezvzdálenější zobrazená vzdálenost

        solids = new ArrayList<Solid>();
        solids.add(new Cube());

        axis = new Solid[3];    //pravidlo - osy se vykreslují fixními barvamy RGB->XYZ

        display();
    }
    private void display()
    {
        renderer.clear();
        renderer.setView(camera.getViewMatrix());   //nastavím zobrazovací matici
        renderer.setProjection(projection);         //nastavím projekční matici

        renderer.setModel(new Mat4Identity());
        //renderer.draw(axis);        //pro osy nechceme aplikovat žádné modelovací matice, posunout linky nám neposune počátek, osy by nebyly osami, postrádaly by smysl

        //vykreslení ostatních těles
        renderer.setModel(model);
        renderer.draw(solids.toArray(new Solid[0]));
    }
}
