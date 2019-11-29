//VBE #17 //VBE #20
package util;

import model.*;

import java.util.ArrayList;
import java.util.List;

public final class utility {
    //https://www.geeksforgeeks.org/how-to-remove-duplicates-from-arraylist-in-java/
    public static <T> List<T> removeDuplicates(List<T> list)
    {
        List<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }

    public static List<Line> prepareScanLine(List<Line> lines) {    //VBE #17
        List<Line> out = new ArrayList<>();
        Primka horizontal = new Primka(new Point(0,0),new Point(1,0));
        for (Line line : lines) {
            Primka pLine = new Primka(line);
            /*if (!horizontal.rovnobezka(pLine)) {  //REM VBE #20
                line.reduceEndY();
                out.add(line);
            }*/
            if (Math.round(line.getK()*1000)/1000f != 0)    //VBE #20 pokud k nulová, pak se jedná o horizontálu
            {
                line.reduceEndY();
                out.add(line);
            }
        }
        return out;
    }

    public static int getPointListMaxY(List<Point> points) {    //VBE #17
        if (points.size() == 0) return 0;
        int max = (int)points.get(0).getY();
        for (Point point : points) {
            if (point.getY() > max) max = (int)point.getY();
        }
        return max;
    }
    public static int getPointListMinY(List<Point> points) {    //VBE #17
        if (points.size() == 0) return 0;
        int min = (int)points.get(0).getY();
        for (Point point : points) {
            if (point.getY() < min) min = (int)point.getY();
        }
        return min;
    }
}
