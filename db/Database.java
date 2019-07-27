package db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Database {
    HashMap<String, Table> data;
    String repr;

    public Database() {
        data = new HashMap<String, Table>();
    }

    String ctable(String name, ArrayList<String> columns, ArrayList<String> type) {
        if (data.containsKey(name)) {
            return "ERROR: .*";
        } else if (columns.isEmpty()) {
            return "ERROR: .*";
        } else {
            Table theTable = new Table(name, columns, type);
            data.put(name, theTable);
        }

        if (data.containsKey(name)) {
            return "";
        } else {
//            System.err.printf("Failed to add Table");
            return "ERROR: .*";
        }
    }

    String droptheTable(String tableName) {
        if (data.containsKey(tableName)) {
            data.remove(tableName);
            if (data.containsKey(tableName)) {
//                System.err.printf("Failure to drop table");
                return "ERROR: .*";
            }
            return "";
        }
        System.err.printf("Table is not in Database");
        return "ERROR: .*";
    }


    public String transact(String query) {
        try {
            int index = 0, i = 0;
            while (i < query.length()) {
                if (query.charAt(i) == ' ') {
                    index++;
                } else if (query.charAt(i) != ' ') {
                    break;
                }
                i++;
            }
            query = query.substring(index, query.length());
            eval(query);
            return repr;
        } catch (IOException e) {
//            System.err.printf("You fucked up");
            return "ERROR: .*";
        }
    }

    static final String REST = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD = Pattern.compile("load " + REST),
            STORE_CMD = Pattern.compile("store " + REST),
            DROP_CMD = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    final Pattern CREATE_NEW = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*"
            + "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+"
                    + "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+"
                    + "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+"
                    + "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL = Pattern.compile("(\\S+)\\s+as select\\s+"
                    + SELECT_CLS.pattern()),
            INSERT_CLS = Pattern.compile("(\\S+)\\s+values\\s+(.+?"
                    + "\\s*(?:,\\s*.+?\\s*)*)"),
            ALL = Pattern.compile("something related to *");

    public void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println("Expected a single query argument");
            return;
        }
        eval(args[0]);
    }

    public void eval(String query) throws IOException {
        try {
            Matcher m;
            if ((m = CREATE_CMD.matcher(query)).matches()) {
                createTable(m.group(1));
                //   System.out.print(m.group(1));
            } else if ((m = LOAD_CMD.matcher(query)).matches()) {
                loadTable(m.group(1));
            } else if ((m = STORE_CMD.matcher(query)).matches()) {
                storeTable(m.group(1));
            } else if ((m = DROP_CMD.matcher(query)).matches()) {
                dropTable(m.group(1));
            } else if ((m = INSERT_CMD.matcher(query)).matches()) {
                insertRow(m.group(1));
            } else if ((m = PRINT_CMD.matcher(query)).matches()) {
                printTable(m.group(1).trim());
            } else if ((m = SELECT_CMD.matcher(query)).matches()) {
                Matcher l = SELECT_CLS.matcher(m.group(1));
                if (!l.matches()) {
                    System.err.printf("Malformed select: %s\n", m.group(1));
                }
                ArrayList<String> columnNamesToUse = new ArrayList<>();
                ArrayList<String> fromWhatTables = new ArrayList<>();
                ArrayList<String> whereClauses = new ArrayList<>();
                String[] firstSplit = l.group(1).trim().split("\\s*,\\s*");
                for (String s : firstSplit) {
                    if (s.contains(" as ")) {
                        String[] a = asSelect(s);
                        for (String b : a) {
                            columnNamesToUse.add(b.trim());
                        }
                    } else {
                        columnNamesToUse.add(s.trim());
                    }
                }
                String[] secondSplit = l.group(2).split("\\s*,\\s*");
                for (String c : secondSplit) {
                    fromWhatTables.add(c.trim());
                }
                if (l.group(3) != null) {
                    String[] thirdSplit = l.group(3).split("\\s*and\\s*");
                    for (String s : thirdSplit) {
                        String[] a = clauseSelect(s);
                        for (String b : a) {
                            whereClauses.add(b);
                        }
                    }
                }
                repr = select(this, columnNamesToUse, fromWhatTables, whereClauses);
            } else {
                System.err.printf("Malformed query: %s\n", query);
            }
        } catch (IllegalStateException e) {
            System.err.printf("ERROR: Malfored Command!");
        }
    }

    private void createTable(String expr) throws IOException {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            System.out.print(m.group(1));
            System.out.println(m.group(2).split("[,\\s]+"));
            createNewTable(m.group(1), m.group(2).split("[,\\s]+"));

//        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
//            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            repr = "ERROR: .*";
//                System.err.print("Malformed create: %s\n" + expr);
        }
    }


    private void createNewTable(String name, String[] cols) {
        //* reads through the column-info in the query and
        boolean what = true;
        //* creates an ArrayList of column names
        ArrayList<String> columnNames = new ArrayList<String>();
        for (int i = 0; i < cols.length; i = i + 2) {
            columnNames.add(cols[i]);
        }

        //* creates an ArrayList of column types
        ArrayList<String> columnTypes = new ArrayList<String>();
        for (int i = 1; i < cols.length; i = i + 2) {
            columnTypes.add(cols[i]);
        }


        for (int i = 0; i < columnTypes.size(); i++) {
            if (!columnTypes.get(i).equals("int")
                    && !columnTypes.get(i).equals("float")
                    && !columnTypes.get(i).equals("string")) {
                repr = "ERROR: .*";
                what = false;
            }
        }

        //* CREATES THE FUCKING TABLE
        if (what) {
            repr = ctable(name, columnNames, columnTypes);
        }
    }

    private void loadTable(String name) throws IOException {
        System.out.printf("You are trying to load the table named %s\n", name);
        repr = FileData.work(name, this);
    }

    private void storeTable(String name) throws IOException {
        if (this.data.containsKey(name)) {
            System.out.printf("You are trying to store the table named %s\n", name);
            repr = Store.makeFile(name, this);
        } else {
            repr = "ERROR: .*";
        }
    }

    private void dropTable(String name) throws IOException {
        System.out.printf("You are trying to drop the table named %s\n", name);
        repr = droptheTable(name);
    }

    private void insertRow(String expr) throws IOException {

        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            repr = "ERROR: .*";
        }

        createNewInsert(m.group(1), m.group(2).split(COMMA));
    }

    private void createNewInsert(String name, String[] values) throws IOException {
        //* reads through the values-info in the query and

        //* creates an ArrayList of column names
        ArrayList<String> insertNames = new ArrayList<String>();
        for (int i = 0; i < values.length; i++) {
            insertNames.add(values[i]);
        }
        repr = InsertInto.insertInto(name, insertNames, this);
    }

    private void printTable(String name) throws IOException {
        System.out.printf("You are trying to print the table named %s\n", name);
        if (this.data.containsKey(name)) {
            repr = Print.returnState(name, this);
        } else {
            repr = "ERROR: .*";
        }
    }

    public String select(Database x, ArrayList<String> columnNamesToUse,
                         ArrayList<String> fromWhatTables, ArrayList<String> whereClauses) {

//        ArrayList<Table> fromTables = new ArrayList<>();
//        for (int i = 0; i < fromWhatTables.size(); i++) {
//            fromTables.add(this.data.get(fromWhatTables.get(i)));
//        }
//        Selector pleaseSelect = new Selector(columnNamesToUse, fromTables, whereClauses);
//        //return pleaseSelect.simpleSelect();
//        if (!pleaseSelect.exists) {
//            return "ERROR: columns requested don't exist in tables provided";
//        }
//        if (pleaseSelect.fromWhatUse.size() == 1) {
//            return Select.printTable(pleaseSelect.simpleSelect());
//        }
//        if (pleaseSelect.inCommon) {
//            return pleaseSelect.join();
//        }
//        if (!pleaseSelect.inCommon) {
//            return pleaseSelect.cartesian();
//        } else {
//            return "ERROR: idk it just didn't work man";
//        }
        return "";
    }

    public String[] clauseSelect(String line) {
        Pattern p = Pattern.compile("([A-Za-z]\\w*)\\s*(<=|>=|>|<|==|!=)\\s*(\'*\\w+\'*)");
        Matcher m;
        if ((m = p.matcher(line)).matches()) {
            String[] s = {m.group(1), m.group(2), m.group(3)};
            return s;
        } else {
            return null;
        }
    }

    public String[] asSelect(String line) {
        Pattern p = Pattern.compile
                ("([A-Za-z]\\w*)\\s*(\\*|\\/|\\+|\\-)\\s*(\\w+)\\s*[\"as\"]\\w+\\s*(\\w+)");
        Matcher m;
        if ((m = p.matcher(line)).matches()) {
            String[] s = {m.group(1), m.group(2), m.group(3), m.group(4)};
            return s;
        } else {
            return null;
        }
    }
}

