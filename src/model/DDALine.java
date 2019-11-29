//VBE #9 //VBE #20
package model;

import java.awt.Color;

import renderer.*;

public class DDALine extends Line {
    //region Constructor
    @Deprecated(forRemoval = true)  //VBE #9 Deprecated
    public DDALine(int startX, int startY, int endX, int endY) {
        super(startX, startY, endX, endY);
    }

    public DDALine(int startX, int startY, int endX, int endY, Color color) {
        super(startX, startY, endX, endY, color);
    }
    @Deprecated(forRemoval = true)  //VBE #9 Deprecated
    public DDALine(Point point1, Point point2) {
        super(point1, point2);
    }

    public DDALine(Point point1, Point point2, Color color) {
        super(point1, point2, color);
    }

    public DDALine(Line line) {
        super(line);
    }
    //endregion


    @Override
    public float getK() {   //VBE #20
        return k;
    }

    float k = (float)(endY-startY)/(float)(endX-startX);
    float q = (startY - k * startX);

    public void drawLine(Renderer renderer)
    {
        if (Math.abs(k) <= 1) {
            normalizeX();   //prohodí počáteční a koncový bod pokud je to potřeba podle osy X
            float y = (k * startX + q);
            for (int x = startX; x <= endX; x++) {
                renderer.DrawPixel(x, y, color);    //VBE #9
                y = y + k;
            }
        }
        else
        {
            normalizeY();
            float h = 1/k;
            float r = (startX - h * startY);
            float x = ((1/k)*startY +r);
            for (int y = startY; y <= endY; y++)
            {
                renderer.DrawPixel(x, y, color);    //VBE #9
                x = x + h;
            }
        }
    }
}