package test;

import model.DDALine;
import model.Line;
import model.Point;
import model.Primka;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimkaTest {
    @Test
    void constructor() {
        Point point1 = new Point(1,4);
        Point point2 = new Point(8,7);
        Primka primka = new Primka(point1,point2);
        assertEquals(7-4,primka.getNormal().getX());
        assertEquals(-(8-1),primka.getNormal().getY());

        point1 = new Point(-1,-4);
        point2 = new Point(-8,7);
        primka = new Primka(point1,point2);
        assertEquals(7-(-4),primka.getNormal().getX());
        assertEquals(-(-8-(-1)),primka.getNormal().getY());

        point1 = new Point(-1,-4);
        point2 = new Point(-8,7);
        Primka primka1 = new Primka(point1,point2);
        Primka primka2 = new Primka(point2,point1);
        assertNotEquals(primka1.getNormal(),primka2.getNormal());
        assertEquals(primka1.getNormal(),primka2.getNormal().opposite());
        assertEquals(primka1.getC(),-primka2.getC());
    }

    @Test
    void point() {
        Point point1 = new Point(-1,-4);
        Point point2 = new Point(-8,7);
        Primka primka1 = new Primka(point1,point2);
        Primka primka2 = new Primka(point2,point1);
        assertTrue(primka1.point(point1));
        assertTrue(primka1.point(point2));
        assertTrue(primka2.point(point1));
        assertTrue(primka2.point(point2));
    }

    @Test
    void constructorException() {
        Point point1 = new Point(1,4);
        Point point2 = new Point(1,4);
        assertThrows(IllegalArgumentException.class, () -> new Primka(point1,point2));
    }

    @Test
    void prusecikPrimka() {
        Point point1 = new Point(0,4);
        Point point2 = new Point(8,8);
        Primka primka1 = new Primka(point1,point2);
        Primka primka1a = new Primka(point2,point1);
        Point point3 = new Point(0,6);
        Point point4 = new Point(4,0);
        Primka primka2 = new Primka(point3,point4);
        Primka primka2a = new Primka(point4,point3);
        assertEquals(new Point(1,4.5),primka1.prusecik(primka2));
        assertEquals(new Point(1,4.5),primka2.prusecik(primka1));

        assertEquals(new Point(1,4.5),primka1.prusecik(primka2a));
        assertEquals(new Point(1,4.5),primka2.prusecik(primka1a));

        assertEquals(new Point(1,4.5),primka1a.prusecik(primka2));
        assertEquals(new Point(1,4.5),primka2a.prusecik(primka1));

        assertEquals(new Point(1,4.5),primka1a.prusecik(primka2a));
        assertEquals(new Point(1,4.5),primka2a.prusecik(primka1a));
    }

    @Test
    void prusecikPrimkaRovnobezky() {
        Point point1 = new Point(0,4);
        Point point2 = new Point(8,8);
        Primka primka1 = new Primka(point1,point2);
        Primka primka1a = new Primka(point2,point1);
        Point point3 = new Point(0,6);
        Point point4 = new Point(8,10);
        Primka primka2 = new Primka(point3,point4);
        Primka primka2a = new Primka(point4,point3);
        assertNull(primka1.prusecik(primka2));
        assertNull(primka2.prusecik(primka1));

        assertNull(primka1.prusecik(primka2a));
        assertNull(primka2.prusecik(primka1a));
        assertNull(primka1a.prusecik(primka2));
        assertNull(primka2a.prusecik(primka1));
        assertNull(primka1a.prusecik(primka2a));
        assertNull(primka2a.prusecik(primka1a));
    }

    @Test
    void prusecikPrimkaRovnobezkyShodne() {
        Point point1 = new Point(0,4);
        Point point2 = new Point(8,8);
        Primka primka = new Primka(point1,point2);
        assertNull(primka.prusecik(primka));
    }

    @Test
    void prusecikLine() {
        Point point1 = new Point(0,0);
        Point point2 = new Point(8,8);
        Point point3 = new Point(0,4);
        Point point4 = new Point(8,4);
        Primka primka1 = new Primka(point1,point2);
        Primka primka2 = new Primka(point3,point4);
        Line line1 = new DDALine(point3,point4,null);

        assertEquals(new Point(4,4),primka1.prusecik(line1));
        assertNull(primka2.prusecik(line1));

        Primka primka = new Primka(point1,point3);
        Line line2 = new DDALine(point2,point4,null);
        Line line3 = new DDALine(new Point(-2,6),new Point(1,6),null);
        Line line4 = new DDALine(new Point(-2,6),new Point(0,6),null);
        Line line5 = new DDALine(new Point(-2,6),new Point(-0.9,6),null);
        Line line6 = new DDALine(new Point(-2,6),new Point(-1,6),null);
        assertNull(primka.prusecik(line2));
        assertEquals(new Point(0,6),primka.prusecik(line3));
        assertEquals(new Point(0,6),primka.prusecik(line4));
        assertEquals(new Point(0,6),primka.prusecik(line5));
        assertEquals(null,primka.prusecik(line6));
    }

    @Test
    void polorovina() {
        Point point1 = new Point(0,0);
        Point point2 = new Point(1,1);
        Primka primka1 = new Primka(point1,point2);  //osa 1.-3. kvadrantu
        Primka primka2 = new Primka(point2,point1);  //osa 1.-3. kvadrantu

        Point pointA = new Point(-1,3); //2. kvadrant
        Point pointB = new Point(2,-3); //4. kvadrant
        Point pointC = new Point(6,-8); //4. kvadrant
        assertNotEquals(primka1.polorovina(pointA),primka1.polorovina(pointB));
        assertEquals(primka1.polorovina(pointA),primka1.polorovina(pointA));
        assertEquals(primka1.polorovina(pointB),primka1.polorovina(pointC));
        assertFalse(primka1.polorovina(pointA));
        assertTrue(primka1.polorovina(pointB));
        assertTrue(primka2.polorovina(pointA));
        assertFalse(primka2.polorovina(pointB));
    }
}