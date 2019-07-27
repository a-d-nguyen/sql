package db;

import java.util.ArrayList;

/**
 * Created by Anthony Nguyen on 3/3/2017.
 */

public class InsertInto {
    Table fromTable;
    ArrayList<String> insertValues;

    public InsertInto(String name, ArrayList<String> values, Database db) {
        if (db.data.containsKey(name)) {
            fromTable = db.data.get(name);
            insertValues = values;
            insertInto(name, values, db);
        } else {
            throw new IllegalArgumentException("Table not in Database");
        }
    }

    static String insertInto(String fromTbl, ArrayList<String> values, Database db) {
        try {
            if (db.data.containsKey(fromTbl)) {
                Table theTable = db.data.get(fromTbl);
            } else {
                return "ERROR: .*";
            }
            Table theTable = db.data.get(fromTbl);
            ArrayList<String> colNames = theTable.columnNames;
            if (values.size() != colNames.size()) {
                return "ERROR: .*";
            }
            ArrayList<String> colTypes = theTable.columnTypes;
            for (int i = 0; i < colNames.size(); i++) {
                Columns temp = (Columns) theTable.cMap.get(colNames.get(i));

                if (colTypes.get(i).equals("int")) {
                    try {
                        if (values.get(i).equals("NOVALUE")) {
                            temp.inputs.add(Integer.parseInt("-1000000"));
                            theTable.cMap.put(colNames.get(i), temp);
                        } else if (values.get(i).indexOf(".") > 0) {
                            return "ERROR: come on man, integers can't have decimals";
                        } else {
                            temp.inputs.add(Integer.parseInt(values.get(i)));
                            theTable.cMap.put(colNames.get(i), temp);
                        }
                    } catch (IndexOutOfBoundsException|NumberFormatException e) {
                        return "ERROR: .*";
                    }


                } else if (colTypes.get(i).equals("float")) {
                    try {
                        if (values.get(i).equals("NOVALUE")) {
                            temp.inputs.add(Integer.parseInt("-1000000.0"));
                            theTable.cMap.put(colNames.get(i), temp);
                        } else {
                            temp.inputs.add(Float.parseFloat(values.get(i)));
                            theTable.cMap.put(colNames.get(i), temp);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        return "ERROR: .*";
                    }


                } else if (colTypes.get(i).equals("string")) {
                    try {
                        temp.inputs.add(values.get(i));
                        theTable.cMap.put(colNames.get(i), temp);
                    } catch (IndexOutOfBoundsException e) {
                        return "ERROR: .*";
                    }
                } else {
                    return "ERROR: .*";
                }
            }
            return "";
        } catch (IndexOutOfBoundsException | NumberFormatException e) {
            return "ERROR: .*";

        }
    }


}
