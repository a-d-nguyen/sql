package db;

/*Created by Anthony Nguyen on 3/3/2017.*/



import java.util.ArrayList;

public class Select {
    Database newDB;
    Table newTable;
    ArrayList<String> icolJoin;
    ArrayList<String> itblstoJoin;
    ArrayList<String> doWhere;

    //supposed to be all of the strings past select
    public Select(Database NDB, ArrayList<String> colJoin, ArrayList<String> tblstoJoin, ArrayList<String> whereClause) {
        newDB = NDB;
        this.icolJoin = colJoin;
        this.itblstoJoin = tblstoJoin;
        this.doWhere = whereClause;
    }

    /*String work (Database NDB, ArrayList<String> whatCols, ArrayList<String> whatTbls){

        //check column types
        //check that tables contain columns
        if(work_tbl.size() == 1){
            //removes rather than keeps the columns **FIX
            newTable = NDB.data.get(work_tbl.get(0));
            for(int i = 0; i < work_col.size(); i++){
                newTable.cMap.remove(work_col.get(i));
            }
            printTable(newTable);
        } else if (work_tbl.size() == 2) {
            Join(work_col, NDB.data.get(work_tbl.get(0)), NDB.data.get(work_tbl.get(1)), NDB);
        } else if(work_tbl.size() > 2) {

        } else {
            System.err.printf("done fucked up");
        }
        return "";
    }

    String simpleSelect (Database db, ArrayList<String> selCols, String tblName){
        if (db.data.containsKey(tblName)) {
            Table temp = db.data.get(tblName);
            if (selCols.get(0).equals("*")) {
                if (selCols.size() != 1) {
                    return "ERROR: .*";
                }
                newTable = temp;
                return printTable(newTable);
            }
            for (int i = 0; i < selCols.size(); i++){
                if (!temp.cMap.containsKey(selCols.get(i))) {
                    return "ERROR: .*";
                } else if (selCols.get(i).equals("as")) {
                    String newColName = selCols.get(i + 1);

                } else {
                    newTable.cMap.put(selCols.get(i), temp.cMap.get(selCols.get(i)));
                }
            }

        } else {
            return "ERROR: .*";
        }

    }

//    Table Join (ArrayList<String> col_toJoin, Table tbl1, Table tbl2, Database uDB) {
//        try {
//            boolean common = false;
//
//            for(int i = 0; i < tbl1.columnNames.size(); i++){
//                if (tbl2.columnNames.contains(tbl1.columnNames.get(i))) {
//                    common = true;
//                }
//            }
//
//            if (!common){
//                cartesian_Join(tbl1, tbl2, uDB);
//            }
//
//
//            return tbl1;
//        } catch(IllegalArgumentException e) {
//            return "ERROR: .*";
//        }
//    }

    String cartesian_Join (Table tbl1, Table tbl2, Database cDB) {
        //check if columns are same size
        try {
            if (tbl1.getColsize() != tbl2.getColsize()) {
                throw new IllegalArgumentException("I'm going to cry.");
            }


            return printTable(newTable);
        } catch (IllegalArgumentException e) {
            return "ERROR: .*";
        }
    }

//    String asFunction() {
//        //join the tables
//        //use c_table to create a new table w/ name x
//    }*/

    static String printTable (Table gg) {
        String repr;
        try {
            int x = gg.columnNames.size();

            repr = gg.columnNames.get(0) + " " + gg.columnTypes.get(0) + ",";
            for (int i = 1; i < x - 1; i++) {
                repr += gg.columnNames.get(i) + " " + gg.columnTypes.get(i) + ",";
            }
            repr += (gg.columnNames.get(x - 1) + " " + gg.columnTypes.get(x - 1) + "\n");

            Columns find_size = (Columns) gg.cMap.get(gg.columnNames.get(0));
            int size = find_size.inputs.size();

            for (int i = 0; i < size; i++) {
                for (int q = 0; q < x - 1; q++) {
                    repr += gg.getValue((String) gg.columnNames.get(q), i) + ",";
                }
                repr += gg.getValue((String) gg.columnNames.get(x - 1), i) + "\n";
            }
            System.out.print(repr);
            return repr;
        } catch (IllegalArgumentException e){
            return "ERROR: .*";
        }
    }
}
