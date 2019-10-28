package renderer;

import model.Point;
import view.Raster;

import java.awt.*;
import java.io.Console;
import java.util.List;

public class Renderer {

    private final Raster raster;

    public Renderer(Raster raster) {
        this.raster = raster;

        Obdelnik(4, 4, 20, 38);
        line(4, 4, 20, 38);
    }

    public void line(int startX, int startY, int endX, int endY)
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
            raster.DrawPixel(x,y,Color.BLUE.getRGB());
        }

    }

    public void lineDDA(int startX, int startY, int endX, int endY)
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
            raster.DrawPixel(x,y,Color.BLUE.getRGB());
            y = y+k;
        }

    }


    private void Obdelnik(int startX, int startY, int endX, int endY)
    {
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++){
                raster.DrawPixel(x, y, Color.GREEN.getRGB());
            }
        }
    }

    public void clear()
    {
        raster.clear();
    }

    public void drawPolygon(List<Point> points) {
        for (int i = 0; i < points.size()-1;i++ )
        {
            lineDDA((int)points.get(i).getX(),(int)points.get(i).getY(),(int)points.get(i+1).getX(),(int)points.get(i+1).getY());
        }
        lineDDA((int)points.get(0).getX(),(int)points.get(0).getY(),(int)points.get(points.size()-1).getX(),(int)points.get(points.size()-1).getY());
    }

    public void drawPolygon2(List<Point> points) {

    }
}
