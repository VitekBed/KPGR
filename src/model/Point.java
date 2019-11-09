package model;

public class Point {
    private double x,y;

    public Point(double x, double y) {
        this.x=x;
        this.y=y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point Translace(Point vector)
    {
        double x = this.x + vector.x;
        double y = this.y + vector.y;
        return new Point(x,y);
    }

    public Point Rotace(float angel)
    {
        double x = this.x * Math.cos(angel) - this.y * Math.sin(angel);
        double y = this.x * Math.sin(angel) + this.y * Math.cos(angel);
        return new Point(x,y);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point )) return false;
        Point point = (Point)obj;
        if (point.getX() == this.getX() && point.getY() == this.getY()) return true;
        return false;
    }
}
