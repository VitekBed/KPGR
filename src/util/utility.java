package util;

import java.util.ArrayList;

public final class utility {
    //https://www.geeksforgeeks.org/how-to-remove-duplicates-from-arraylist-in-java/
    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {
        ArrayList<T> newList = new ArrayList<T>();

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
}
