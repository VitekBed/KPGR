package model;

import java.awt.Color;

import renderer.*;

public class DDALine extends Line {
    public DDALine(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
    }
    public DDALine(int startX, int startY, int endX, int endY, Color color) {
        super(startX, startY, endX, endY, color);
    }

    
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
        float y = (k*startX+q);
        for (int x = startX; x<=endX; x++)
        {
            renderer.DrawPixel(x,y,Color.BLUE);
            y = y+k;
        }
    }
}