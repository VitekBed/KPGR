package controller;

import model3d.*;
import renderer.GPURenderer;
import renderer.Renderer3D;
import transforms.*;
import view.Raster;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Controller3D
{
    private final GPURenderer renderer;
    private Camera camera;
    private final double pi = Math.PI;
    private final ArrayList<Solid> solids;
    private final double speed = 0.1;
    private Mat4 model, view, projection;
    private boolean perpective = true;

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
                                    30);      //nezvzdálenější zobrazená vzdálenost*/
        //projection = new Mat4OrthoRH(30,20,0.1,20);

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
        //základní mřížka
        solids.add(new Axis(Color.RED,0,1,10,0,10));
        solids.add(new Axis(Color.GREEN,1,1,10,0,10));
        solids.add(new Axis(Color.BLUE,2,1,10));


        Solid spiral0 = new Spiral(Color.ORANGE,3,0,100,.1);
        Solid spiralCube = new Cube(Color.YELLOW,1);
        spiral0.setTransformation(new Mat4Scale(.1,1,1).mul(new Mat4Transl(0,1,1)));
        spiralCube.setTransformation(new Mat4Scale(5,1,1).mul(new Mat4Transl(5,1,1)));
        solids.add(spiral0);
        solids.add(spiralCube);
        Solid spiral1 = new Spiral(Color.ORANGE,3,0,100,1);
        Solid spiralCube1 = new Cube(Color.YELLOW,1);
        spiral1.setTransformation(new Mat4Scale(.1,1,1).mul(new Mat4Transl(0,4,1)));
        spiralCube1.setTransformation(new Mat4Scale(5,1,1).mul(new Mat4Transl(5,4,1)));
        solids.add(spiral1);
        solids.add(spiralCube1);
        Solid spiral2= new Spiral(Color.GREEN,3,0,50,.3);
        Solid spiralCube2 = new Cube(Color.YELLOW,1);
        spiral2.setTransformation(new Mat4Scale(.2,1,1).mul(new Mat4RotZ(.3)).mul(new Mat4Transl(0,.75,3)));
        spiralCube2.setTransformation(new Mat4Transl(1,0,0).mul(new Mat4Scale(5,1,1)).mul(new Mat4RotZ(.3)).mul(new Mat4Transl(0,.75,3)));
        solids.add(spiral2);
        solids.add(spiralCube2);


        Solid hranol = new Cube(Color.BLACK,3);
        hranol.setTransformation(new Mat4Scale(.5,1,1.5).mul(new Mat4RotZ(.2)).mul(new Mat4Transl(2,8,1.5)));
        Solid hranol2 = new Cube(Color.BLACK,3);
        hranol2.setTransformation(new Mat4Scale(.5,1,1.5).mul(new Mat4RotZ(-.2)).mul(new Mat4Transl(4,8,1.5)));
        Solid jehlan = new Jehlan(Color.PINK,3,6);
        jehlan.setTransformation(new Mat4Scale(2,1.5,1.5).mul(new Mat4RotY(-Math.PI/2f)).mul(new Mat4Transl(3,8,3)));
        solids.add(hranol);
        solids.add(hranol2);
        solids.add(jehlan);


        Point3D p1 = new Point3D(-1,-1,-1); Point3D p2 = new Point3D(-1,1,1);
        Point3D p3 = new Point3D(1,1,1);    Point3D p4 = new Point3D(1,-1,1);
        Solid curve0 = new BezierCurve(new Color(0,75,0), 3, p1,p2,p3,p4,20);
        Solid curveBox0 = new Cube(new Color(0,125,0), 1);
        Group group0 = new Group(curve0,curveBox0);

        Solid curve1 = new CoonsCurve(new Color(75,160,0), 3, p1,p2,p3,p4,20);
        curve1.setTransformation(new Mat4Scale(1/6f,1/6f,1/6f));
        Solid triangle1a = new NAngle(Color.BLACK,1,p1,p2,p3);
        Solid triangle1b = new NAngle(Color.BLACK,1,p4,p2,p3);
        Solid curveBox1 = new Cube(new Color(0,125,0), 1);
        Group group1 = new Group(triangle1a,triangle1b,curve1,curveBox1);

        Point3D q1 = new Point3D(-1,-1,-1); Point3D q2 = new Point3D(1,-1,1);
        Point3D q3 = new Point3D(0,6,6);    Point3D q4 = new Point3D(0,-6,0);
        Solid curve2 = new FergusonCurve(new Color(75,75,0), 3, q1,q2,q3,q4,20);
        Solid curveBox2 = new Cube(new Color(0,125,0), 1);
        Group group2 = new Group(curve2,curveBox2);

        group1.setTransformation(new Mat4Transl(1,0,0));
        group2.setTransformation(new Mat4Transl(2,0,0));

        Group group = new Group(group0,group1,group2);
        group.setTransformation(new Mat4RotZ(.6).mul(new Mat4Transl(9,9,1)));
        solids.addAll(group0.getSolidList());
        solids.addAll(group1.getSolidList());
        solids.addAll(group2.getSolidList());

        //pravidlo - osy se vykreslují fixními barvamy RGB->XYZ
        //osy kreslíme až jako poslední, protože zatím není žešená žádná průhlednost ani viditelnost a čáry se vzájemně překreslují
        solids.add(new Axis(Color.RED,0,3,1));
        solids.add(new Axis(Color.GREEN,1,3,1));
        solids.add(new Axis(Color.BLUE,2,3,1));
    }

    private void initListeners(Raster raster) {
        raster.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode())
                {
                    case 65: //a
                        camera = camera.left(speed);
                        display();
                        break;
                    case 68: //d
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
                    case 81: //q
                    case 37: //left
                        camera = camera.addAzimuth(speed/7f);
                        display();
                        break;
                    case 76: //l
                    case 69: //e
                    case 39: //right
                        camera = camera.addAzimuth(-speed/7f);
                        display();
                        break;
                    case 38: //up
                    case 73: //i
                        camera = camera.addZenith(speed/7f);
                        display();
                        break;
                    case 75: //k
                    case 40: //down
                        camera = camera.addZenith(-speed/7f);
                        display();
                        break;
                    case 16: //shift
                        camera = camera.up(speed);
                        display();
                        break;
                    case 17: //ctrl
                        camera = camera.down(speed);
                        display();
                        break;
                    case 32:
                        perpective = !perpective;
                        if (perpective)
                        {
                            projection = new Mat4PerspRH(pi/3,  //zorný úhel
                                    600/800f,  //poměr výška/šířka okna;
                                    0.1,      //nejkratší zdobrazená vzdálenost
                                    30);      //nezvzdálenější zobrazená vzdálenost*/
                        }
                        else
                        {
                            projection = new Mat4OrthoRH(30,20,0.1,20);
                        }
                        display();
                        break;
                }
            }
        });
        final MouseCache mc = new MouseCache();
        raster.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {
                    case MouseEvent.BUTTON1:
                        mc.setLeft(true);
                        mc.setPosition(e.getPoint());
                        break;
                    case MouseEvent.BUTTON3:
                        mc.setRight(true);
                        mc.setPosition(e.getPoint());
                        break;
                }
                //System.out.println("PRESSED");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mc.setLeft(false);
                mc.setRight(false);
                //System.out.println("RELEASE");
            }
        });
        raster.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {

                //System.out.println("MOVE");
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mc.isLeft()) {
                    double diffX = e.getX() - mc.getPosition().getX();
                    double diffY = e.getY() - mc.getPosition().getY();
                    camera = camera.addAzimuth((-diffX / 6.28) / 100f);
                    camera = camera.addZenith((diffY / 6.28) / 100f);
                    display();
                    mc.setPosition(e.getPoint());
                }
                if (mc.isRight())
                {
                    double diffX = e.getX() - mc.getPosition().getX();
                    double diffY = e.getY() - mc.getPosition().getY();
                    camera = camera.left(diffX/100f);
                    camera = camera.up(diffY/100f);
                    display();
                    mc.setPosition(e.getPoint());
                }
                //System.out.println("DRAGGED");
            }
        });
        raster.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                camera = camera.forward(-e.getPreciseWheelRotation());
                display();
            }
        });
    }


    private class MouseCache {
        boolean left = false;
        boolean right = false;
        java.awt.Point position;

        public MouseCache() {
        }

        public boolean isLeft() {
            return left;
        }

        public void setLeft(boolean left) {
            this.left = left;
        }

        public boolean isRight() {
            return right;
        }

        public void setRight(boolean right) {
            this.right = right;
        }

        public Point getPosition() {
            return position;
        }

        public void setPosition(Point position) {
            this.position = position;
        }
    }
}
