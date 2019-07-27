package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Anthony Nguyen on 3/5/2017.
 */
public class FileData {
    private String text;
    private Database base;
    static int size;

    public FileData(String filepath, Database db) throws IOException {
        this.text = filepath;
        this.base = db;
        work(this.text, this.base);
    }

    static String work(String filePath, Database db) throws IOException {
        String tName = filePath;
        filePath = filePath + ".tbl";
        ArrayList<String> cols = new ArrayList<String>(); //column names
        ArrayList<String> ty = new ArrayList<String>(); //column types
        HashMap<String, ArrayList<String>> info = new HashMap<String, ArrayList<String>>();

        try {
            Load file = new Load(filePath);
            String[] aryLines = file.openFile();

            String firstLine = (String) aryLines[0];
            String[] temp = firstLine.split(",");
            for (int u = 0; u < temp.length; u++) {
                String[] temp2 = temp[u].split("\\s+");
                cols.add(temp2[0]);
                ty.add(temp2[1]);
            }

            for (int q = 0; q < cols.size(); q++) {
                info.put(cols.get(q), new ArrayList<String>());
            }

            for (int i = 1; i < aryLines.length; i++) {
                String temp3 = aryLines[i];
                String[] temp4 = temp3.split(",");
                for (int o = 0; o < temp4.length; o++) {
                    ArrayList<String> temp5 = info.get(cols.get(o));
                    temp5.add(temp4[o]);
                    info.put(cols.get(o), temp5);
                    size = temp5.size();
                }
            }

            db.ctable(tName, cols, ty);
            for (int i = 0; i < size; i++) {
                ArrayList<String> row = new ArrayList<String>();
                for (int j = 0; j < info.size(); j++) {
                    row.add(info.get(cols.get(j)).get(i));
                }
                InsertInto testInsertInto = new InsertInto(tName, row, db);
            }

            return "";
        } catch (IOException e) {
            return "ERROR: .*";
        }
    }
}
