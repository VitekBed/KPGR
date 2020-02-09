package controller;

import model3d.Axis;
import model3d.Cube;
import model3d.Solid;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Raster;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Controller3D
{
    private final GPURenderer renderer;
    private Camera camera;
    private final double pi = Math.PI;
    private final ArrayList<Solid> solids;
    private final double speed = 0.1;
    private Mat4 model, view, projection;

    public Controller3D(Raster raster) {
        this.renderer = new Renderer3D(raster);

        model = new Mat4Identity(); //základní model bez úprav

        camera = new Camera()
                .withPosition(new Vec3D(10,-6,8))    //kamera odkud se díváme (pozice)
                .withAzimuth(Math.toRadians(120))  //rotace v ose y
                .withZenith(Math.toRadians(-40));  //rotace v ose x (limitovaný obvykle na -pi/2 to +pi/2)
        view = camera.getViewMatrix();
        projection = new Mat4PerspRH(pi/3,  //zorný úhel
                                    600/800f,  //poměr výška/šířka okna;
                                    0.1,      //nejkratší zdobrazená vzdálenost
                                    30);      //nezvzdálenější zobrazená vzdálenost

        solids = new ArrayList<Solid>();
        makeScene();

        initListeners(raster);
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

    private void makeScene()
    {
        //pravidlo - osy se vykreslují fixními barvamy RGB->XYZ
        solids.add(new Axis(Color.RED,0,3,1));
        solids.add(new Axis(Color.RED,0,1,10,0,10));
        solids.add(new Axis(Color.GREEN,1,3,1));
        solids.add(new Axis(Color.GREEN,1,1,10,0,10));
        solids.add(new Axis(Color.BLUE,2,3,1));
        solids.add(new Axis(Color.BLUE,2,1,10));

        Cube cube0 = new Cube(Color.BLACK,3);
        cube0.setTransformation(new Mat4RotX(.3).mul(new Mat4RotY(.3)));
        solids.add(cube0);
    }

    private void initListeners(Raster raster) {
        raster.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode())
                {
                    case 65: //a
                    case 37: //left
                        camera = camera.left(speed);
                        display();
                        break;
                    case 68: //d
                    case 39: //right
                        camera = camera.right(speed);
                        display();
                        break;
                    case 87: //w
                        camera = camera.forward(speed);
                        display();
                        break;
                    case 83: //s
                        camera = camera.backward(speed);
                        display();
                        break;
                    case 74: //j
                        camera = camera.addAzimuth(speed/7f);
                        display();
                        break;
                    case 76: //l
                        camera = camera.addAzimuth(-speed/7f);
                        display();
                        break;
                    case 73: //i
                        camera = camera.addZenith(speed/7f);
                        display();
                        break;
                    case 75: //k
                        camera = camera.addZenith(-speed/7f);
                        display();
                        break;
                    case 38: //up
                        camera = camera.up(speed);
                        display();
                        break;
                    case 40: //down
                        camera = camera.down(speed);
                        display();
                        break;
                }

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });
    }


}
