package controller;

import renderer.Renderer;
import view.Raster;
import model.Point;

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
                if (e.isControlDown())
                {
                    if (points.size() == 0)
                    {
                        points.add(new Point(e.getX(),e.getY()));
                    }
                    else
                    {
                        points.add(new Point(e.getX(),e.getY()));
                        renderer.drawPolygon2(points);
                        points.clear();
                    }
                }
                else
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        points.add(new Point(e.getX(),e.getY()));
                    else {
                        renderer.clear();
                        renderer.drawPolygon(points);
                        points.clear();
                    }
                }
            }
        });
    }
}
