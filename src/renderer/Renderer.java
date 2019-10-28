package renderer;

import java.awt.*;
import java.util.List;

import model.*;
import view.*;

public class Renderer {

    private final Raster raster;

    public Renderer(Raster raster) {
        this.raster = raster;

        //Obdelnik(4, 4, 20, 38);
        //line(4, 4, 20, 38);
    }

    public void line(int startX, int startY, int endX, int endY, Color color)
    {
        BasicLine line = new BasicLine(startX, startY, endX, endY, color);
        line.drawLine(this);
    }

    public void lineDDA(int startX, int startY, int endX, int endY)
    {
        DDALine line = new DDALine(startX, startY, endX, endY);
        line.drawLine(this);
    }


    private void Obdelnik(int startX, int startY, int endX, int endY)
    {
        for (int y = startY; y < endY; y++) {
            for (int x = startX; x < endX; x++){
                raster.DrawPixel(x, y, Color.GREEN);
            }
        }
    }
    
    public void clear()
    {
        raster.clear();
        line(0,15,raster.getWidth(),15,Color.BLACK);
        if (inlineTextString != null)
            raster.DrawText(inlineTextString,10,12,Color.BLACK);
    }
    private String inlineTextString;
    public void setInlineTextString ( String text)
    {
        inlineTextString = text;
    }

    public void drawPolygon(List<model.Point> points) 
    {
        for (int i = 0; i < points.size()-1;i++ )
        {
            lineDDA((int)points.get(i).getX(),(int)points.get(i).getY(),(int)points.get(i+1).getX(),(int)points.get(i+1).getY());
        }
        lineDDA((int)points.get(0).getX(),(int)points.get(0).getY(),(int)points.get(points.size()-1).getX(),(int)points.get(points.size()-1).getY());
    }

    public void drawPolygon2(List<model.Point> points) 
    {

    }
    
    public void DrawPixel(double x, double y, Color color)
    {
        raster.DrawPixel(x,y,color);
    }

    
}
