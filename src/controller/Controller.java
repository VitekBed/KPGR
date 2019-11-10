//VBE #4

package controller;

import view.Raster;
import model.Point;
import renderer.*;
import enume.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Controller {

    private final Renderer renderer;
    private java.util.List<Point> points;
    private Uloha nastaveni;
    private Algoritmus algoritmus;

    public Controller(Raster raster) {
        this.renderer = new Renderer(raster);

        initListeners(raster);
        points = new ArrayList<Point>();
    }

    private void initListeners(Raster raster) {
        raster.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                switch (nastaveni)
                {
                    case CENTER_LINE:
                        renderer.clear();
                        renderer.lineDDA(400,300,e.getX(),e.getY());
                        break;
                    /*case LINE:
                        renderer.clear();
                        if (points.size() > 0)
                            renderer.lineDDA((int)(points.get(0).getX()),(int)(points.get(0).getY()),e.getX(),e.getY());
                        break;*/
                    default:
                        break;
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (points == null || nastaveni == null) return;
                switch (nastaveni)
                {
                    case LINE:
                        if (points.size()==1) {
                            renderer.clear();
                            renderer.lineDDA(points.get(0), new Point(e.getX(), e.getY()));
                        }
                        break;
                    case POLYGON2:
                        if (points.size() == 2) {
                            renderer.clear();
                            renderer.drawPolygon2(points.get(0), points.get(1), new Point(e.getX(), e.getY()));
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        raster.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switch (nastaveni)
                {
                    case LINE:
                        if (points.size() > 1) {
                            points.clear();
                            renderer.clear();
                        }
                        points.add(new Point(e.getX(),e.getY()));
                        break;
                    case POLYGON:
                        polygon(e);
                        break;
                    case POLYGON2:
                        nuhelnik(e);
                        break;
                    default:
                        break;
                }
            }

            private void nuhelnik(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)    //levý klikání přidává body
                {
                    if (points.size() == 0) renderer.clear();   //vymažu obrazovku pro další kreslení
                    if (points.size() > 2) return;  //pokud už mám dva body další mě nazajímají
                    points.add(new Point(e.getX(), e.getY()));  //přidám bod do points
                }
            }

            private void polygon(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)    //levý klikání přidává body
                {   //VBE #4
                    if (points.size() == 0) renderer.clear();   //vymažu obrazovku pro další kreslení
                    points.add(new Point(e.getX(), e.getY()));  //přidám bod do points
                    if (points.size() > 1) {    //pokud mám alespoň dva body, vykreslím čáru mezi posledníma dvěma
                        renderer.lineDDA(points.get(points.size() - 2), new Point(e.getX(), e.getY()));
                    }
                }
                else {  //nelevým vykreslím polygon
                    renderer.clear();
                    renderer.drawPolygon(points);
                    points.clear();
                }
            }
            
        });
        raster.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar())
                {
                    case '0':
                        renderer.setInlineTextString("Čára ze středu");
                        nastaveni = Uloha.CENTER_LINE;
                        renderer.clear();
                        points.clear();
                        break;
                    case '1':
                        renderer.setInlineTextString("čára mezi body | pravým klikem urči počáteční bod čáry, levým nakresli čáru");
                        nastaveni = Uloha.LINE;
                        renderer.clear();
                        points.clear();
                        break;
                    case '2':   //VBE #4
                        renderer.setInlineTextString("polygon | levým tlačítem přidávej body, pravým uzavři polygon");
                        nastaveni = Uloha.POLYGON;
                        renderer.clear();
                        points.clear();
                        break;
                    case '3':
                        renderer.setInlineTextString("pravidelný n-úhelník | levým kliknutím určete střed a poté jeden z vrcholů, pravým potvrď");
                        nastaveni = Uloha.POLYGON2;
                        renderer.clear();
                        points.clear();
                }
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
            
        }
        );
    }
}
