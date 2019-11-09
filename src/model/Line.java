package model;

import java.awt.Color;
import renderer.*;

public abstract class Line
{
    protected int startX;
    protected int startY;
    protected int endX;
    protected int endY;
    protected Color color;

    public Line(int startX, int startY, int endX, int endY, Color color) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
    }
    public Line(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = Color.BLACK;
    }

    public Line(Point point1, Point point2) {
        this(point1,point2,Color.BLACK);
    }

    public Line(Point point1, Point point2, Color color) {
        this((int)point1.getX(), (int)point1.getY(),(int)point2.getX(),(int)point2.getY(),color);
    }

    public final int getStartX()
    {
        return startX;
    }
    public final int getStartY()
    {
        return startY;
    }
    public final int getEndX()
    {
        return endX;
    }
    public final int getEndY()
    {
        return endY;
    }
    public final Color getColor()
    {
        return color;
    }
    public abstract void drawLine(Renderer renderer);
}