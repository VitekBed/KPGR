//VBE #12 //VBE #17 //VBE #20
package model;

import java.awt.Color;
import java.security.InvalidParameterException;

import renderer.*;

public abstract class Line
{
    int startX;
    int startY;
    int endX;
    int endY;
    Color color;

    public Line(int startX, int startY, int endX, int endY, Color color) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
    }

    /**
     * @deprecated Use Line(int startX, int startY, int endX, int endY, Color color) instead
     */
    @Deprecated(forRemoval = true)
    public Line(int startX, int startY, int endX, int endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = Color.BLACK;
    }

    /**
     * @deprecated Use Line(Point point1, Point point2, Color color) instead
     */
    @Deprecated(forRemoval = true)
    public Line(Point point1, Point point2) {
        this(point1,point2,Color.BLACK);
    }

    public Line(Point point1, Point point2, Color color) {
        this((int)point1.getX(), (int)point1.getY(),(int)point2.getX(),(int)point2.getY(),color);
    }
    public Line(Line line)
    {
        this.startX = line.startX;
        this.startY = line.startY;
        this.endX = line.endX;
        this.endY = line.endY;
        this.color = line.color;
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
    public final Point getStartPoint() {return new Point(startX,startY);}   //VBE #12
    public final Point getEndPoint() {return new Point(endX,endY);}         //VBE #12
    public final Color getColor()
    {
        return color;
    }
    public abstract void drawLine(Renderer renderer);

    public void normalizeY() {
        if (startY > endY)
        {
            int q = startX;
            startX = endX;
            endX = q;
            q = startY;
            startY = endY;
            endY = q;
        }
    }

    public void normalizeX() {
        if (startX > endX)
        {
            int q = startX;
            startX = endX;
            endX = q;
            q = startY;
            startY = endY;
            endY = q;
        }
    }

    public boolean isNormalizedY(){ //VBE #17
        return (startY < endY);
    }

    public void reduceEndY() throws InvalidParameterException { //VBE #17
        if (isNormalizedY()) {
            endY--;
        } else {
            normalizeY();
            if (isNormalizedY()) {
                endY--;
            } else {
                throw new InvalidParameterException("Line is horizontal, cannot reduce!");
            }
        }
    }

    public abstract float getK();   //VBE #20
}