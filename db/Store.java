package db;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Anthony Nguyen on 3/3/2017.
 */


public class Store {
    public Store(String tableToStore, Database db) throws IOException {
        makeFile(tableToStore, db);
    }

    static String makeFile(String tableToStore, Database db) throws IOException {
        try {
            File file = new File(tableToStore + ".tbl");
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            Table newT = db.data.get(tableToStore); //Table to store from Database

            //Writes the first line (columns and types)
            int x = newT.columnNames.size();
            for (int i = 0; i < x - 1; i++) {
                printWriter.print(newT.columnNames.get(i) + " " + newT.columnTypes.get(i) + ",");
            }
            printWriter.println(newT.columnNames.get(x - 1) + " " + newT.columnTypes.get(x - 1));

            Columns findSize = (Columns) newT.cMap.get(newT.columnNames.get(0));
            int size = findSize.inputs.size();

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < x - 1; j++) {
                    String temp = (String) newT.columnNames.get(j);
                    if (newT.getValue(temp, i).equals(-1000000)
                            | newT.getValue(temp, i).equals(-1000000.0)) {
                        printWriter.print("NOVALUE" + ",");
                    } else {
                        printWriter.print(newT.getValue((String) newT.columnNames.get(j), i) + ",");
                    }
                }
                if (newT.getValue((String) newT.columnNames.get(x - 1), i).equals(-1000000)
                        | newT.getValue((String) newT.columnNames.get(x - 1), i).equals(-1000000.0)) {
                    printWriter.print("NOVALUE");
                } else {
                    printWriter.println(newT.getValue((String) newT.columnNames.get(x - 1), i));
                }
            }


            fileWriter.flush();
            fileWriter.close();
            return "";
        } catch (IOException e) {
            e.printStackTrace();
//            System.err.printf("Cannot store table");
            return "ERROR: .*";
        }
    }
}
