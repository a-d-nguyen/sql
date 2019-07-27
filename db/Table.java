package db;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anthony Nguyen on 2/28/2017.
 */

public class Table<T> {
    String title;
    ArrayList<String> columnNames;
    ArrayList<String> columnTypes;
    HashMap<String, Columns<T>> cMap;

    public Table(String name, ArrayList<String> columns, ArrayList<String> types) {
        this.title = name;
        this.columnNames = columns;
        this.columnTypes = types;
        this.cMap = new HashMap<String, Columns<T>>();
        for (int i = 0; i < this.columnNames.size(); i++) {
            String temp = this.columnNames.get(i);
            cMap.put(temp, new Columns<T>(this.columnTypes.get(i)));
        }
    }

    Object getValue(String colName, int position) {
        return this.cMap.get(colName).getItem(position);
    }

    int getColsize() {
        return cMap.get(columnNames.get(0)).inputs.size();
    }
}
