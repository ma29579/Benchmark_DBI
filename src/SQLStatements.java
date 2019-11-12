public class SQLStatements {

    public static final String createSchema = "DROP SCHEMA IF EXISTS %schemaName% CASCADE;" +
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

    public static String getCreateSchema(int n){
        String schemaName = "tps" + n;
        return createSchema.replace("%schemaName%",schemaName);
    }

}
