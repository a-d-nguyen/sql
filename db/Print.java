package db;

/**
 * Created by Anthony Nguyen on 3/3/2017.
 */
public class Print {

    public Print(String tblName, Database db) {
        returnState(tblName, db);
    }

    static String returnState(String tblName, Database db) {

        try {
            String repr;
            Table newT = db.data.get(tblName);
            int x = newT.columnNames.size();
            if (x == 1) {
                repr = newT.columnNames.get(0) + " " + newT.columnTypes.get(0) + "\n";
            } else {
                repr = newT.columnNames.get(0) + " " + newT.columnTypes.get(0) + ",";
                for (int i = 1; i < x - 1; i++) {
                    repr += newT.columnNames.get(i) + " " + newT.columnTypes.get(i) + ",";
                }
                repr += (newT.columnNames.get(x - 1) + " " + newT.columnTypes.get(x - 1) + "\n");
            }
            Columns findSize = (Columns) newT.cMap.get(newT.columnNames.get(0));
            int size = findSize.inputs.size();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < x - 1; j++) {
                    String temp = (String) newT.columnNames.get(j);
                    if (newT.getValue(temp, i).equals(-1000000)
                            | newT.getValue(temp, i).equals(-1000000.0)) {
                        repr += "NOVALUE" + ",";
                    } else {
                        repr += newT.getValue((String) newT.columnNames.get(j), i) + ",";
                    }
                }
                repr += newT.getValue((String) newT.columnNames.get(x - 1), i) + "\n";
            }
//            System.out.print(repr);
            return repr;
        } catch (IllegalArgumentException e) {
            System.err.printf("Cannot print table.");
            return "ERROR: Cannot print table.";
        }
    }
}
