package db;


import java.util.ArrayList;

/**
 * Created by Anthony Nguyen on 2/28/2017.
 */

public class Columns<T> {
    ArrayList inputs;

    public Columns(String type) {
        if (type.equalsIgnoreCase("int")) {
            try {
                ArrayList<Integer> iT = new ArrayList<Integer>();
                inputs = iT;
            } catch (NullPointerException e) {
                System.err.printf("hello?");
            }
        } else if (type.equalsIgnoreCase("float")) {
            try {
                ArrayList<Float> fT = new ArrayList<Float>();
                inputs = fT;
            } catch (NullPointerException e) {
                System.err.printf("hello?");
            }
        } else if (type.equalsIgnoreCase("string")) {
            try {
                ArrayList<String> sT = new ArrayList<String>();
                inputs = sT;
            } catch (NullPointerException e) {
                System.err.printf("hello?");
            }
        } else {
            System.err.printf("Invalid Data Type");
        }
    }

    Object getItem(int i) {
        return inputs.get(i);
    }
}
