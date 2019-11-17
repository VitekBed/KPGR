// #3 M:1-3 //VBE #9 //VBE #12

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

    public Polygon(List<Point> points) {
        this.points = points;
    }

    public Polygon(List<Point> points, Color lineColor, Color color) {
        this.points = points;
        this.lineColor = lineColor;
        this.color = color;
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
            Point prvni = points.get(0);    //VBE #12
            Point druhy = points.get(1);    //VBE #12   (nově testujeme i úhel k prvnímu bodu a úhel u prvního bodu)

            Vec2D v1 = new Vec2D(p2.getX()-p1.getX(),p2.getY()-p1.getY());  // v1 = p1->p2
            Vec2D v2 = new Vec2D(point.getX()-p2.getX(),point.getY()-p2.getY()); //v2 = p2->point
            Vec2D v3 = new Vec2D(prvni.getX()-point.getX(),prvni.getY()-point.getY()); //v = point->první   VBE #12
            Vec2D v4 = new Vec2D(druhy.getX()-prvni.getX(),druhy.getY()-prvni.getY()); //v = první->druhý   VBE #12

            double phi = Math.acos(v1.dot(v2)/(v1.length()*v2.length()));
            if ( Double.isNaN(phi)) return; //VBE #12 jen test, nemělo by nastat, ale teoreticky může nastat, když body jsou totožné
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
                convex = testConvex(v1,v2,v3,v4);   //VBE #12 vyčleněno do samostatné metody
                if (direction == null)  //pokud směr ještě nemám udaný, první trojice bodů mi jej udá
                {
                    direction = determinant(v1,v2) > 0;    //kladný determinant - kladný směr otáčení; záporný determinant - záporný směr otáčení
                }
            }
        }
        points.add(point);
    }
    public boolean testConvex(Point point)  //VBE #12 předělané a vyčleněné testování konvexnosti
    {
        if (!convex) return false;  //pokud není konvexní, nemá smysl testovat
        Point p1 = points.get(points.size()-2);
        Point p2 = points.get(points.size()-1);
        Point prvni = points.get(0);
        Point druhy = points.get(1);
        Vec2D v1 = new Vec2D(p2.getX()-p1.getX(),p2.getY()-p1.getY());  // v1 = p1->p2
        Vec2D v2 = new Vec2D(point.getX()-p2.getX(),point.getY()-p2.getY()); //v2 = p2->point
        Vec2D v3 = new Vec2D(prvni.getX()-point.getX(),prvni.getY()-point.getY()); //v = point->první
        Vec2D v4 = new Vec2D(druhy.getX()-prvni.getX(),druhy.getY()-prvni.getY()); //v = první->druhý
        return testConvex(v1,v2,v3,v4);
    }
    private boolean testConvex(Vec2D v1, Vec2D v2, Vec2D v3, Vec2D v4)
    {
        /* Schválně testujeme 4 vektory. Pro trojúhelník je v1 a v4 totožný. Smysl tohoto testování je zajistit,
         * aby nebylo možné zadávat nekonvexní nebo protínající se útvary. Vycházíme z předpokladu, že libovolný
         * n-úhelník který je konvexní a neprotíná sebe sama, tak i po odebrání libovolného bodu zůstává konvexní.
         * Pokud tedy zadávám bod, který by po spojení s prvním bodem nevytvořil konvexní útvar, pak neexistuje
         * způsob jak přidat libovolný počet bodů tak, aby vznikl uzavřený neprotínající se konvexní útvar.
         *
         * Kontrolujeme, zda jsme dříve útvar neoznačili za nekonvexní, pak přidáním libovolných bodů zůstává
         * vždy nekonvexní. Pokud přidáváme nový bod, tak musí mít stejnou orientaci úhlu jako je předchozí úhel
         * a úhel k prvnímu bodu musí mít také stejnou orientaci a úhel u prvního bodu musí mít také stejnou
         * orientaci.
         */
        if (!convex) return false;  //pokud není konvexní, nemá smysl testovat
        //pokud mám směr ale nesedí, není konvexní
        if (direction != null
                && (direction != determinant(v1, v2) > 0
                    || direction != determinant(v2, v3) > 0
                    || direction != determinant(v3, v4) > 0)) {
            return false;
        }
        return convex;
    }
    private double determinant(Vec2D v1, Vec2D v2)  //VBE #12 vyčleněno do samostatné metody
    {
        /*
        Determinant matice složené ze sloupcových vektorů určuje vzájemnou polohu dvou vektorů.
        Pokud determinant je kladný, jsou od sebe otočeny o úhel v kladném smyslu.
        Toto potřebujeme zjišťovat abychom snadno určili zda máme konvexní nebo nekonvexní útvar.
        Pokud jsou všechny úhly v jednom směru, jedná se o konvexní útvar.
        Determinant nám ale nic neříká o velikosti úhlu, teoreticky by šlo vektory znormovat a
        využít toho, že absolutní hodnotu determinantu lze interpretovat jako obsah rovnoběžníku (#6)
        */
        //neřeším nějakou pomocnou třídu pro matice 2x2, determinant je takto dostatečně jednoduchý
        double det = (v1.getX() * v2.getY()) - (v2.getX() * v1.getY());
        if (det < 0.00001) return 0;    //pro malé hodnoty determinantu je úhel -> 0 a vracím hodnotu 0
        else return det;
    }

    /**
     * @return seznam orientovaných úseček, které tvoří daný objekt
     */
    public List<Line> getLines()
    {
        if (points.size() < 3) return null;
        List<Line> list = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new DDALine(points.get(i),points.get(i+1), lineColor);  //VBE #9
            list.add(line);
        }
        Line line = new DDALine(points.get(points.size()-1),points.get(0), lineColor);  //VBE #9
        list.add(line);
        return list;
    }
}
