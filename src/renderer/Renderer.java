//VBE #5

package renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import model.*;
import model.Point;
import model.Polygon;
import transforms.Vec2D;
import view.*;

public class Renderer {

    private final Raster raster;

    public Renderer(Raster raster) {
        this.raster = raster;

        //Obdelnik(4, 4, 20, 38);
        //line(4, 4, 20, 38);
    }

    public void line(int startX, int startY, int endX, int endY, Color color)
    {
        BasicLine line = new BasicLine(startX, startY, endX, endY, color);
        line.drawLine(this);
    }

    public void lineDDA(int startX, int startY, int endX, int endY)
    {
        DDALine line = new DDALine(startX, startY, endX, endY);
        line.drawLine(this);
    }
    public void lineDDA(Point point1, Point point2) {
        lineDDA((int)point1.getX(),(int)point1.getY(),(int)point2.getX(),(int)point2.getY());
    }

    private void Obdelnik(int startX, int startY, int endX, int endY)
    {
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++){
                raster.DrawPixel(x, y, Color.GREEN);
            }
        }
    }
    
    public void clear()
    {
        raster.clear();
        this.line(0,15,raster.getWidth(),15,Color.BLACK);
        if (inlineTextString != null)
            raster.DrawText(inlineTextString,10,12,Color.BLACK);
    }
    private String inlineTextString;
    public void setInlineTextString ( String text)
    {
        inlineTextString = text;
    }

    public void drawPolygon(List<model.Point> points) 
    {
        for (int i = 0; i < points.size()-1;i++ )
        {
            this.lineDDA((int)points.get(i).getX(),(int)points.get(i).getY(),(int)points.get(i+1).getX(),(int)points.get(i+1).getY());
        }
        this.lineDDA((int)points.get(0).getX(),(int)points.get(0).getY(),(int)points.get(points.size()-1).getX(),(int)points.get(points.size()-1).getY());
    }

    public void drawPolygon2(Point center, Point apex, Point point)  //VBE #5
    {
        /*  Všechny body pravidelného n-úhelníku musí ležet na krušnici. Paramterické vyjádření kružnice:
         *  x = x0 + r * cos(phi); y = y0 + r * sin(phi); kde (x0,y0) je střed, r je poloměr a phi úhel mezi body
         *  Já potřebuji zjistit úhel mezi středem a vrcholem a středem a pozicí druhého bodu, určit nejbližší
         *  úhel, který dělí 360° a jeho násobky použít jako parametr phi.
         */

        /*       ________________________
         * r = \/ (x0-x1)^2 + (y0-y1)^2        zjištění poloměru => Pythagorova věta
         */
        double r = Math.sqrt(Math.pow(center.getX()-apex.getX(),2) + Math.pow(center.getY()-apex.getY(),2));

        //výpočet úhlu vzaný původně z Polygon.addPoint
        Vec2D v1 = new Vec2D(apex.getX()-center.getX(),apex.getY()-center.getY());  // v1 = center->apex
        Vec2D v2 = new Vec2D(point.getX()-center.getX(),point.getY()-center.getY()); //v2 = center->point
        double phi0 = Math.acos(v1.dot(v2)/(v1.length()*v2.length()));
        double det = (v1.getX() * v2.getY()) - (v2.getX() * v1.getY()); //potřebujeme abychom věděli ve kterém směru úhel je
        //if (phi0 < Math.toRadians(10)) phi0 = Math.toRadians(10);  //nastavení minimálního úhlu pro polygon
        //najdeme nejbližší počet celých dílů na které by tento úhel kružnici rozdělil
        int v = (int)Math.round((2*Math.PI)/phi0);
        if (v < 3 ) v = 3;      //omezení na minimálně trojúhelník
        if (v > 36) v = 36;     //omezení na maximálně třicetišestiúhelník (úhel 10°)

        double phi = (2*Math.PI)/v;
        List<Point> points = new ArrayList<>();

        for (int i = 0; i < v; i++) {
            // a se skládá z části která dopočítává úhly - "počáteční úhel" udaný prvním bodem;
            double a = (Math.signum(det)*i * phi) - Math.acos(v1.dot(new Vec2D().withX(1).withY(0))/v1.length());
            double x = center.getX() + r * Math.cos(a);
            double y = center.getY() + r * Math.sin(a);
            points.add(new Point(x,y));
        }
        Polygon polygon = new Polygon(points);
        drawPolygon(polygon.getPoints());
        //System.out.println(r + ";" + phi0 + ";" + v + ";" + phi + ";" + points.size());
    }
    
    public void DrawPixel(double x, double y, Color color)
    {
        raster.DrawPixel(x,y,color);
    }



}
