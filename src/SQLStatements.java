class SQLStatements {

    private static final String dummy20 = "abcdefghijklmnopqrst";
    private static final String dummy72 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdefghij";

    private static final String createSchema = "DROP SCHEMA IF EXISTS %schemaName% CASCADE;" +
            "CREATE SCHEMA IF NOT EXISTS %schemaName%;" +
            "create table %schemaName%.branches" +
            "( branchid int not null," +
            "branchname char(20) not null," +
            "balance int not null," +
            "address char(72) not null," +
            "primary key (branchid) );" +
            "create table %schemaName%.accounts" +
            "( accid int not null," +
            "name char(20) not null," +
            "balance int not null," +
            "branchid int not null," +
            "address char(68) not null," +
            "primary key (accid)," +
            "foreign key (branchid) references %schemaName%.branches );" +
            "create table %schemaName%.tellers" +
            "( tellerid int not null," +
            "tellername char(20) not null," +
            "balance int not null," +
            "branchid int not null," +
            "address char(68) not null," +
            "primary key (tellerid)," +
            "foreign key (branchid) references %schemaName%.branches );" +
            "create table %schemaName%.history" +
            "( accid int not null," +
            "tellerid int not null," +
            "delta int not null," +
            "branchid int not null," +
            "accbalance int not null," +
            "cmmnt char(30) not null," +
            "foreign key (accid) references %schemaName%.accounts," +
            "foreign key (tellerid) references %schemaName%.tellers," +
            "foreign key (branchid) references %schemaName%.branches );";

    private static final String insertIntoBranches = "INSERT INTO %schemaName%.branches (branchid, branchname, balance, address)" +
            "VALUES (%branchid%, \'" + dummy20 + "\', 0, \'" + dummy72 + "\');";


    static String getCreateSchema(int n){
        String schemaName = "tps" + n;
        return createSchema.replace("%schemaName%",schemaName);
    }

    static String getInsertIntoBranches(int n, int i) {
        String schemaName = "tps" + n;
        String insertStmt = insertIntoBranches.replace("%schemaName%",schemaName);
        insertStmt = insertStmt.replace("%branchid%", Integer.toString(i));
        return insertStmt;
    }

}
