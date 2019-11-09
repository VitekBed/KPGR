package model;

import java.awt.Color;

import renderer.Renderer;

public class BasicLine extends Line {
    public BasicLine(int startX, int startY, int endX, int endY, Color color) {
        super(startX, startY, endX, endY, color);
    }

    public BasicLine(Point point1, Point point2) {
        super(point1, point2);
    }

    public BasicLine(Point point1, Point point2, Color color) {
        super(point1, point2, color);
    }

    public BasicLine(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
    }
    @Override
    public void drawLine(Renderer renderer)
    {
        if (startX > endX)
        {
            int q = startX;
            startX = endX;
            endX = q;
            q = startY;
            startY = endY;
            endY = q;
        }

        float k = (float)(endY-startY)/(float)(endX-startX);
        float q = (startY - k*startX);

        for (int x = startX; x<=endX; x++)
        {//y=kx+q
            float y = (k*x+q);
            renderer.DrawPixel(x,y,color);
        }
    }
}