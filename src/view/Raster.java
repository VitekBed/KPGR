//VBE #9 //VBE #10 //VBE #15
package view;

import javax.swing.*;
import java.awt.*;
//import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Raster extends JPanel {

    private final BufferedImage img; // objekt pro zápis pixelů
    private final BufferedImage img_r; // VBE #15 objekt pro uchování commitnuté informace
    private final Graphics g; // objekt nad kterým jsou k dispozici grafické funkce
    private static final int FPS = 1000 / 30;
    private final Color backgroundColor = Color.GRAY;

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Raster() {
        setPreferredSize(new Dimension(800, 600));
        // inicializace image, nastavení rozměrů (nastavení typu - pro nás nedůležité)
        img = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
        img_r = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);    //VBE #15
        g = img.getGraphics();
        setLoop();
        clear();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
        // pro zájemce - co dělá observer - https://stackoverflow.com/a/1684476
    }

    private void setLoop() {
        // časovač, který 30 krát za vteřinu obnoví obsah plátna aktuálním img
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, FPS);
    }

    public void clear() {
        g.setColor(backgroundColor);
        g.fillRect(0, 0, 800, 600);
        commit();   //VBE #15
    }

    public void DrawPixel(double x, double y, Color color)
    {
        DrawPixel((int)Math.floor(x),(int)Math.floor(y),color);
    }
    public void DrawPixel(int x, int y, Color color)
    {
        if (x>0 &&x<img.getWidth() && y>0 && y<img.getHeight())
        img.setRGB(x,y,color.getRGB());
    }
    public void DrawText(String text, int x, int y, Color color)
    {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.drawString(text, x, y);
        g.setColor(oldColor);
    }
    public void drawLine(double x, double y, double x1, double y1, Color color) {
        g.setColor(color);
        g.drawLine((int)Math.round(x),(int)Math.round(y),(int)Math.round(x1),(int)Math.round(y1));
    }
    public void drawLine(double x, double y, double x1, double y1, Color color, int thickness) {
        ((Graphics2D)g).setStroke(new BasicStroke(thickness));
        drawLine(x, y, x1, y1, color);
        ((Graphics2D)g).setStroke(new BasicStroke(1));
    }
    public void drawRectangle(int x, int y, int x1, int y1, Color color) {  //VBE #9

        g.setColor(color);
        g.fillRect(x,y,x1-x,y1-y);

    }
    public void drawRectangle(int x, int y, int w, int h, Color color, boolean b) { //VBE #9
        if (b) {
            drawRectangle(x,y,x+w,y+h,color);
        }
        else {
            drawRectangle(x,y,w,h,color);
        }
    }
    public int getColor(int x, int y){
        return img.getRGB(x,y);
    }

    public void rollback()  //VBE #15
    {
        img.setData(img_r.getData());
    }

    public void commit()    //VBE #15
    {
        img_r.setData(img.getData());
    }
}
