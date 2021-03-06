//VBE #5 //VBE #9 //VBE #10 //VBE #15 //VBE #16 //VBE #12 //VBE #17

package renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.*;
import model.Point;
import model.Polygon;
import transforms.Vec2D;
import util.utility;
import view.*;

public class Renderer {

    private final Raster raster;
    private Color color = Color.RED;    //VBE #9 aktuální nastavená barva pro kreslení

    public Renderer(Raster raster) {
        this.raster = raster;

        //Obdelnik(4, 4, 20, 38);
        //line(4, 4, 20, 38);
    }

    public void line(int startX, int startY, int endX, int endY)    //VBE #9 odebrán Color color
    {
        BasicLine line = new BasicLine(startX, startY, endX, endY, this.color); //VBE #9 color->this.color
        line.drawLine(this);
    }

    public void lineDDA(int startX, int startY, int endX, int endY)
    {
        DDALine line = new DDALine(startX, startY, endX, endY, this.color); //VBE #9 constructor bez barvy je depreciated
        line.drawLine(this);
    }
    public void lineDDA(Point point1, Point point2) {
        lineDDA((int)point1.getX(),(int)point1.getY(),(int)point2.getX(),(int)point2.getY());
    }

    public void imgClean(){ imgClean(false);}   //VBE #15
    public void imgClean(boolean onlyBar)   //VBE #15
    {
        //přebírá kompletně clear() k 191114.1930, commit a40c4f9

        if (onlyBar) raster.drawRectangle(0,0, raster.getWidth(),15, raster.getBackgroundColor());
        else raster.clear();

        Color m = color;    //VBE #9 přidáno ukládání barvy, abych mohl v rámci clear nakreslit co potřebuji. this.line už nebere barvu.
        color = Color.BLACK;
        this.line(0,15,raster.getWidth(),15);
        if (inlineTextString != null)
            raster.DrawText(inlineTextString,10,12,Color.BLACK);
        color = m;
        showColor();   //VBE #9 zobrazím čtverečky s barvou

        imgCommit();
    }
    public void imgRollback(){raster.rollback();}   //VBE #15
    public void imgCommit(){raster.commit();}       //VBE #15

    /***
     * Nahrazeno metodami imgClean(), imgRollback() a imgCommit() VBE #15
     */
    @Deprecated(forRemoval = true)  //VBE #15
    public void clear()
    {
        raster.clear();
        Color m = color;    //VBE #9 přidáno ukládání barvy, abych mohl v rámci clear nakreslit co potřebuji. this.line už nebere barvu.
        color = Color.BLACK;
        this.line(0,15,raster.getWidth(),15);
        if (inlineTextString != null)
            raster.DrawText(inlineTextString,10,12,Color.BLACK);
        color = m;
        showColor(color);   //VBE #9 zobrazím čtverečky s barvou
    }
    private String inlineTextString;
    public void setInlineTextString ( String text)
    {
        inlineTextString = text;
        imgClean(true); //VBE #15
    }

    //region metody polygon
    private void drawPolygon(List<Point> points)    //VBE #16 public -> private
    {
        for (int i = 0; i < points.size()-1;i++ )
        {
            this.lineDDA((int)points.get(i).getX(),(int)points.get(i).getY(),(int)points.get(i+1).getX(),(int)points.get(i+1).getY());
        }
        this.lineDDA((int)points.get(0).getX(),(int)points.get(0).getY(),(int)points.get(points.size()-1).getX(),(int)points.get(points.size()-1).getY());
    }
    public void drawPolygon(Polygon polygon)    //VBE #16 "správnější" metoda pro kreslení polygonu
    {
        drawPolygon(polygon.getPoints());
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
    //endregion

    public void DrawPixel(double x, double y)   //VBE #9 metoda pro kreslení pixelu v barvě nastavené na rendereu
    {
        this.DrawPixel(x,y,this.color);
    }
    public void DrawPixel(double x, double y, Color color)
    {
        raster.DrawPixel(x,y,color);
    }

    public void showColor() //VBE #15
    {
        raster.drawRectangle(770,2,10,10,getColor(),true);
    }
    public void showColor(Color settingColor) { //VBE #9
        raster.drawRectangle(760,2,10,10,settingColor,true);
        raster.drawRectangle(770,2,10,10,getColor(),true);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    //region metody fill VBE #10
    public void fill(Point point, Color color){
        if (point.getX() < 0 || point.getX() > raster.getWidth()-1) return; //základní kontrola mezí
        if (point.getY() < 0 || point.getY() > raster.getHeight()-1) return;

        if (raster.getColor((int)point.getX(),(int)point.getY()) == color.getRGB()) //obarvujeme pouze pokud bod má barvu na kterou jsme klikli
        {
            /* Obarvovací metody tvoří čáry, nejprve nakreslíme celou čáru a pote ji od posledního bodu
             * k prvnímu voláme rekurzivně znovu tuto metodu. Tímto postupem již nebude znovu prováděno
             * zpracování stejného směru, protože tam již narazíme na hranici. Počet vnoření, které je
             * nutno držet v zásobníku je tak oproti pamatování si každého bodu značně zkráceno.
             */
            fillUp(new DDALine(point,new Point((int)point.getX(), 0),this.color),color);
            fillDown(new DDALine(point,new Point((int)point.getX(), raster.getHeight()),this.color),color);
            fillRight(new DDALine(point,new Point(raster.getWidth(),(int)point.getY()),this.color),color);
            fillLeft(new DDALine(point,new Point(0,(int)point.getY()),this.color),color);
            /* Protože v každe z předchozích metod poslední bod odbarvujeme na původní barvu,
             * zde znovu provedeme obarvení. Tento bod je již ve všech směrech zpracován.
             */
            raster.DrawPixel((int)point.getX(),(int)point.getY(),this.color);
            /* Vlastně každý bod rozsvítím, zhasnu a znovu rozsvítím, protože při prvním nakreslení linky
             * dochází k obarvení všech bodů. Ze všech těchto bodů poté v opačném pořadí provádím odbarvení
             * a fill. Je to z důvodu, že ve fill kontroluji barvu
             */
        }
    }

    private void fillUp(Line line, Color bgColor) {
        int x = line.getStartX();
        int y = line.getStartY();
        // provedeme kontrolu na hranici (barvou kontroluji jestli hned v dalším pixelu není hrana
        if (y-1 < 1 || raster.getColor(x,y-1) != bgColor.getRGB()) return;
        while (y > line.getEndY() && raster.getColor(x,y) == bgColor.getRGB())
        {
            raster.DrawPixel(x,y--,this.color); //obarvíme všechny pixely dokun nenarazíme na hranu
        }
        while (y++ < line.getStartY())  //vezmeme "úsečku" a odzadu ji zkracujeme
        {
            raster.DrawPixel(x,y,bgColor);  //odbarvíme bod aby prošel kontrolou ve fill
            fill(new Point(x,y),bgColor);   //provedeme rekurzivní obarvení ve všech směrech
        }
    }
    private void fillDown(Line line, Color bgColor) {
        int x = line.getStartX();
        int y = line.getStartY();
        if (y+1 > raster.getHeight()-1 || raster.getColor(x,y+1) != bgColor.getRGB()) return;
        while (y < line.getEndY() && raster.getColor(x,y) == bgColor.getRGB())
        {
            raster.DrawPixel(x,y++,this.color);
        }
        while (y-- > line.getStartY())
        {
            raster.DrawPixel(x,y,bgColor);
            fill(new Point(x,y),bgColor);
        }
    }
    private void fillRight(Line line, Color bgColor) {
        int x = line.getStartX();
        int y = line.getStartY();
        if (x+1 > raster.getWidth()-1 || raster.getColor(x+1,y) != bgColor.getRGB()) return;
        while (x < line.getEndX() && raster.getColor(x,y) == bgColor.getRGB())
        {
            raster.DrawPixel(x++,y,this.color);
        }
        while (x-- > line.getStartX())
        {
            raster.DrawPixel(x,y,bgColor);
            fill(new Point(x,y),bgColor);
        }
    }
    private void fillLeft(Line line, Color bgColor) {
        int x = line.getStartX();
        int y = line.getStartY();
        if (x-1 < 1 || raster.getColor(x-1,y) != bgColor.getRGB()) return;
        while (x > line.getEndX() && raster.getColor(x,y) == bgColor.getRGB())
        {
            raster.DrawPixel(x--,y,this.color);
        }
        while (x++ < line.getStartX())
        {
            raster.DrawPixel(x,y,bgColor);
            fill(new Point(x,y),bgColor);
        }
    }
    //endregion

    public Polygon orez(Polygon polygon, Polygon orez)  //VBE #12
    {
        if (!orez.getConvex()) return null;
        for (int i = 0; i < orez.getLines().size(); i++) {
            List<Point> points = new ArrayList<>();     //VBE #17 ArrayList -> List
            Primka primka = new Primka(orez.getLines().get(i));
            for (int j = 0; j < polygon.getLines().size(); j++) {
                Line line = polygon.getLines().get(j);
                Point prusecik = primka.prusecik(line);
                boolean a = primka.polorovina(line.getStartPoint());
                boolean b = primka.polorovina(line.getEndPoint());
                Boolean d = !orez.getDirection();
                System.out.println("Uvnitř: " + (a == d) + (a == b));
                if (a == d && b != d)   //pokud začátek čáry je uvnitř a konec ne
                {
                    points.add(line.getStartPoint());
                    points.add(prusecik);
                }
                if (a != d && b == d)   //pokud začátek čáry není uvnitř a konec je
                {
                    points.add(prusecik);
                    points.add(line.getEndPoint());
                }
                if (a == d && b == d)   //pokud začátek i konec čáry je uvnitř
                {
                    points.add(line.getStartPoint());
                    points.add(line.getEndPoint());
                }
            }
            points = util.utility.removeDuplicates(points); //odstraním duplicity (points se porovnávají podle equals
            polygon = new Polygon(points,polygon.getLineColor(),polygon.getColor());    //vyrobím nový polygon už částečně oříznutý
            System.out.println("---");
            //imgClean();           //REM VBE #17
            //drawPolygon(polygon); //REM VBE #17
        }
        return polygon;
    }

    public void scanLine(Polygon polygon) { //VBE #17
        List<Line> lines = new ArrayList<>(polygon.getLines());
        //lines.removeHorizontals();       //odstraníme horizontály
        //lines.lineListOrientation();     //seřadíme vektory ve směru shora dolu, abychom mohli zkrátit o poslední y bod
        //lines.lineListReduction();       //zkrátíme konce všech čar o jeden pixel
        lines = utility.prepareScanLine(lines); //provede předchozí tři kroky ale jen přes jednu iteraci
        int maxY = utility.getPointListMaxY(polygon.getPoints());
        int minY = utility.getPointListMinY(polygon.getPoints());

        for (int i = minY; i < maxY; i++) {
            Primka hLine = new Primka(new Point(0,i),new Point(raster.getWidth(),i));   //horizontální čára
            List<Point> points = new ArrayList<>();
            for (Line line : lines) {
                Point point = hLine.prusecik(line);
                if (point != null) points.add(point);   //pro každou horizontální čáru najdu všechny průsečíky
            }
            points.sort(new Comparator<Point>() {
                @Override
                public int compare(Point p1, Point p2) {
                    return (int) p1.getX() - (int) p2.getX();
                }
            });
            if (points.size()%2 >0)
            {
                System.out.println("problemek");
            }
            for (int j = 0; j < points.size(); j=j+2) {
                lineDDA(points.get(j),points.get(j+1));     //pro každou dvojici průsečíků nakreslím čáru
            }
        }
    }
}
