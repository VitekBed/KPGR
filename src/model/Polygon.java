// #3 M:1-3

package model;

import transforms.Vec2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points = new ArrayList<>();
    private Color lineColor;
    private Color color;
    private boolean convex = true;
    private Boolean direction;  //true - kladný směr otáčení; false - záporný směr otáčení; null - ještě neznámý
    private final double delta = Math.PI/180;   //minimální rozdíl úhlů musí být 1°


    //region Constructors, Setters, Getters
    public Polygon() {
        this(Color.BLACK,Color.ORANGE);
    }

    public Polygon(Color lineColor, Color color) {
        this.lineColor = lineColor;
        this.color = color;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Point> getPoints() {
        return points;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getConvex() {
        return convex;
    }

    public Boolean getDirection() {
        return direction;
    }
    //endregion

    public void addPoint(Point point)
    {
        if (points.size() == 0)
        {
            points.add(point);  //pokud se jedná o první bod, pak jej nekontrolujeme
            return;
        }
        if (points.get(points.size()-1).equals(point)) return; //pro případ dvojkliku na jednom místě
        if (points.size() >= 2) //pro případ že body leží na přímce řeším dva krajní případy - phi=0 a phi=PI
        {
            Point p1 = points.get(points.size()-2);
            Point p2 = points.get(points.size()-1);

            Vec2D v1 = new Vec2D(p2.getX()-p1.getX(),p2.getY()-p1.getY());  // v1 = p1->p2
            Vec2D v2 = new Vec2D(point.getX()-p2.getX(),point.getY()-p2.getY()); //v2 = p2->point

            double phi = Math.acos(v1.dot(v2)/(v1.length()*v2.length()));
            System.out.println(phi);
            if (phi < delta)    //úhel je nulový
            {
                points.set(points.size()-1,point);  //nahradím poslední bod novým a dál se ničím nezabývám
                return;
            }
            if (phi > Math.PI - delta)  //úhel je PI - tedy leží na opačné polopřímce
            {
                return; //bod zahodím - takový nechci a dál nic neřeším
            }
            //Pokud je úhel "použitelný" a nejsem už nyní nekonvexní, pokračuji určením směru
            if (convex) {
                /*
                Determinant matice složené ze sloupcových vektorů určuje vzájemnou polohu dvou vektorů.
                Pokud determinant je kladný, jsou od sebe otočeny o úhel v kladném smyslu.
                Toto potřebujeme zjišťovat abychom snadno určili zda máme konvexní nebo nekonvexní útvar.
                Pokud jsou všechny úhly v jednom směru, jedná se o konvexní útvar.
                Determinant nám ale nic neříká o velikosti úhlu, teoreticky by šlo vektory znormovat a
                využít toho, že absolutní hodnotu determinantu lze interpretovat jako obsah rovnoběžníku (#6)
                */

                //neřeším nějakou pomocnou třídu pro matice 2x2, determinant je takto dostatečně jednoduchý
                //neřeším případy kdy det -> 0, protože malé úhly už jsou ošetřeny
                double det = (v1.getX() * v2.getY()) - (v2.getX() * v1.getY());
                if (direction != null && direction.booleanValue() != det > 0)   //pokud mám směr ale nesedí, není konvexní
                {
                    convex = false;
                }
                if (direction == null)  //pokud směr ještě nemám udaný, první trojice bodů mi jej udá
                {
                    direction = det > 0;    //kladný determinant - kladný směr otáčení; záporný determinant - záporný směr otáčení
                }
            }
        }
        points.add(point);
    }

    /**
     * @return seznam orientovaných úseček, které tvoří daný objekt
     */
    public List<Line> getLines()
    {
        if (points.size() < 3) return null;
        List<Line> list = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new DDALine(points.get(i),points.get(i+1));
            list.add(line);
        }
        Line line = new DDALine(points.get(points.size()-1),points.get(0));
        list.add(line);
        return list;
    }
}
