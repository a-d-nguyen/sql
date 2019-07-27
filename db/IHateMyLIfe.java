package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Anthony Nguyen on 3/5/2017.
 */
public class IHateMyLIfe {
    public static void main(String[] args) throws IOException {
        Database db = new Database();
        FileData usuck = new FileData("teams", db);
//        System.out.print(db.Data.containsKey("teams"));
        Store testStore = new Store("teams", db);
        InsertInto testInsert = new InsertInto("teams", new ArrayList<String>(Arrays.asList("test", "test1", "test2", "nin", "test3", "test4")), db);
        Print testPrint = new Print("teams", db);

    }
}
