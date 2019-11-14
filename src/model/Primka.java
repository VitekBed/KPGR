//VBE #12
package model;

import transforms.Vec2D;

public class Primka {
    private Vec2D normal;
    private Point bod;
    private double c;

    public Primka(Point point1, Point point2) {
        if (point1.equals(point2)) throw new IllegalArgumentException("Points cannot be equals");
        this.bod = point1;
        double x = point2.getX()-point1.getX();
        double y = point2.getY()-point1.getY();
        setNormal(new Vec2D(y,-x));
    }

    @Override
    public String toString() {
        return "Primka{" +
                normal.getX()+" x + " +
                normal.getY()+" y + " +
                c + " = 0}";
    }
    //region Setters, Getters
    public void setNormal(Vec2D normal) {
        this.normal = normal;
        this.c = -(normal.getX()*bod.getX() + normal.getY()*bod.getY());
    }

    public void setBod(Point bod) {
        this.bod = bod;
        this.c = -(normal.getX()*bod.getX() + normal.getY()*bod.getY());
    }

    public Vec2D getNormal() {
        return normal;
    }

    public Point getBod() {
        return bod;
    }

    public double getC() {
        return c;
    }
    //endregion

    /***
     * Vrací průsečík přímky s čárou, Pozor: čára má vždy koncové souřadnice s ořezanými desetinnými místy!
     * @param line
     * @return bod který je průsečíkem čáry s přímkou
     */
    public Point prusecik(Line line)
    {
        Line normLine = new DDALine(line);
        Point point = prusecik(new Primka(new Point(line.getStartX(),line.getStartY()),new Point(line.getEndX(),line.getEndY())));
        if (point == null) return null;
        normLine.normalizeX();
        if (point.getX() < normLine.getStartX() || point.getX() > normLine.getEndX()) return null;
        normLine.normalizeY();
        if (point.getY() < normLine.getStartY() || point.getY() > normLine.getEndY()) return null;
        return point;
    }
    public Point prusecik(Primka primka)
    {
        if (this.normal.normalized().equals(primka.normal.normalized()) || this.normal.normalized().equals(primka.normal.opposite().normalized())) return null; //rovnoběžky
        double a0 = this.normal.getX();
        double b0 = this.normal.getY();
        double c0 = this.c;
        double a1 = primka.normal.getX();
        double b1 = primka.normal.getY();
        double c1 = primka.c;
        if (a0 != 0 && a1 != 0)
        {
            double y = ((a0*c1) - (a1*c0)) / ((a1*b0) - (a0*b1));
            double x = -(b0*y + c0) / a0;
            return new Point(x,y);
        }
        else if (a0 != 0 && a1 == 0)    //a1 == 0 v tuto chvíli vždy musí platit když a0 != 0
        {
            double y = - (c1 / b1); // pokud a1 == 0 pak b1 je vždy != 0 protože by se jinak nejednalo o rovnici přímky, taková přímka nejde zadat
            double x = -(b0*y + c0) / a0;
            return new Point(x,y);
        }
        else if (a0 == 0 && a1 != 0)
        {
            double y = - (c0 / b0);
            double x = -(b1*y + c1) / a1;
            return new Point(x,y);
        }
        else return null;   //to by znamenalo a0 == 0 && a1 == 0, což ale znamená dvě rovnoběžky s osou x, sem by program nikdy neměl dojít
    }

    /***
     * Určuje ve které polorovině se nachází bod vůči orientované přímce
     * @param point kontrolovaný bod
     * @return true = pravá polorovina, false = levá polorovina
     */
    public boolean polorovina(Point point)
    {
        return normal.getX() * point.getX() + normal.getY() * point.getY() + c > 0;
    }

    public boolean point(Point point) {
        return normal.getX() * point.getX() + normal.getY() * point.getY() + c == 0;
    }
}
