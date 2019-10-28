package controller;

import view.Raster;
import model.Point;
import renderer.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller {

    private final Renderer renderer;
    private java.util.List<Point> points;

    public Controller(Raster raster) {
        this.renderer = new Renderer(raster);

        initListeners(raster);
        points = new ArrayList<Point>();
    }

    private void initListeners(Raster raster) {
        raster.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                renderer.clear();
                renderer.lineDDA(400,300,e.getX(),e.getY());
            }
        });
        raster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isControlDown())  //pokud držím CTRL kreslím úsečku
                {
                    if (points.size() == 0) //první kliknutí
                    {
                        points.add(new Point(e.getX(),e.getY()));   
                    }
                    else    //druhý kliknutí
                    {
                        points.add(new Point(e.getX(),e.getY()));
                        renderer.drawPolygon2(points);
                        points.clear();
                    }
                }
                else    //pokud CTRL nedržím
                {
                    if (e.getButton() == MouseEvent.BUTTON1)    //levý klikání přidává body
                        points.add(new Point(e.getX(),e.getY()));
                    else {  //nelevým vykreslím polygon
                        renderer.clear();
                        renderer.drawPolygon(points);
                        points.clear();
                    }
                }
            }
        });
    }
}
