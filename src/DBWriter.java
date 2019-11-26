import org.apache.commons.lang3.time.StopWatch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import static java.sql.DriverManager.getConnection;

/**
 * Realisiert den Verbindungsauf- und abbau zur Benchbark-Datenbank und initialisiert diese.
 */
class DBWriter {

    private Connection conn;
    private PreparedStatement stmt;

    /**
     * Erstellt ein neues DBHelper-Objekt. Baut eine Verbindung zur angegebenen Datenbank auf. AutoCommit wird auf false gesetzt.
     * @param databaseURL Adresse des Datenbank-Servers
     * @param username Benutzername für die Datenbankverbindung
     * @param password Passwort für die Datenbankverbindung
     * @throws SQLException Wenn keine Verbindung zur Datenbank aufgebaut werden kann
     */
    DBWriter(String databaseURL, String username, String password) throws SQLException {
        conn = getConnection(databaseURL, username, password);
        conn.setAutoCommit(false);
    }

    /**
     * Initialisiert das relationale Datenbankschema entsprechend der Vorgabe aus der Aufgabe
     * @param n Skalierungsfaktor für das Schema
     * @throws SQLException wenn es im SQL-Statement einen Syntaxfehler gibt
     */
    void createBenchmarkDatabase(int n) throws SQLException {

        String schemaName = "tps" + n;

        String createSchema = "DROP SCHEMA IF EXISTS " + schemaName + " CASCADE;" +
                "CREATE SCHEMA IF NOT EXISTS  " + schemaName + " ;" +
                "create table  " + schemaName + " .branches" +
                "( branchid int not null," +
                "branchname char(20) not null," +
                "balance int not null," +
                "address char(72) not null," +
                "primary key (branchid) );" +
                "create table  " + schemaName + " .accounts" +
                "( accid int not null," +
                "name char(20) not null," +
                "balance int not null," +
                "branchid int not null," +
                "address char(68) not null," +
                "primary key (accid)," +
                "foreign key (branchid) references  " + schemaName + " .branches );" +
                "create table  " + schemaName + " .tellers" +
                "( tellerid int not null," +
                "tellername char(20) not null," +
                "balance int not null," +
                "branchid int not null," +
                "address char(68) not null," +
                "primary key (tellerid)," +
                "foreign key (branchid) references  " + schemaName + " .branches );" +
                "create table  " + schemaName + " .history" +
                "( accid int not null," +
                "tellerid int not null," +
                "delta int not null," +
                "branchid int not null," +
                "accbalance int not null," +
                "cmmnt char(30) not null," +
                "foreign key (accid) references  " + schemaName + " .accounts," +
                "foreign key (tellerid) references  " + schemaName + " .tellers," +
                "foreign key (branchid) references  " + schemaName + " .branches );";

        stmt = conn.prepareStatement(createSchema);
        stmt.executeUpdate();
        stmt.close();
    }

    /**
     * Befüllt das Datenbankschema mit der in der Aufgabenstellung angegebenen Anzahl an Tupeln. Die benötigte Zeit in ms wird auf der Konsole ausgegeben.
     * @param n Skalierungsfaktor für das Schema
     * @throws SQLException wenn es im SQL-Statement einen Syntaxfehler gibt
     */
    void insertData(int n) throws SQLException {

        // Instanziierung der StopWatch und Starten der Zeitmessung
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String schemaName = "tps" + n;

        String insertIntoBranches = "INSERT INTO " + schemaName + ".branches (branchid, branchname, balance, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdefghij\');";
        String insertIntoAccounts = "INSERT INTO " + schemaName + ".accounts (accid, name, balance, branchid, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, ?, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdef\');";
        String insertIntoTellers = "INSERT INTO " + schemaName + ".tellers (tellerid, tellername, balance, branchid, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, ?, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdef\');";

        // Einfügen in die Relation "branches"
        stmt = conn.prepareStatement(insertIntoBranches);
        for (int i = 1; i <= n; i++) {
            stmt.setInt(1, i);
            stmt.addBatch();
        }
        stmt.executeBatch();
        stmt.close();

        // Einfügen in die Relation "accounts"
        stmt = conn.prepareStatement(insertIntoAccounts);
        for (int i = 1; i <= n * 100000; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, ThreadLocalRandom.current().nextInt(1, n + 1));
            stmt.addBatch();
            if (i % 100000 == 0) {
                stmt.executeBatch();
            }
        }
        stmt.executeBatch();
        stmt.close();

        // Einfügen in die Relation "tellers"
        stmt = conn.prepareStatement(insertIntoTellers);
        for (int i = 1; i <= n * 10; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, ThreadLocalRandom.current().nextInt(1, n + 1));
            stmt.addBatch();
        }
        stmt.executeBatch();
        stmt.close();

        // Beenden der Transaktion
        conn.commit();

        // Beenden der Zeitmessung und Ausgabe der gemessenen Zeit auf der Konsole
        stopWatch.stop();
        long time = stopWatch.getTime();
        System.out.println("Benötigte Zeit für das Befüllen der Datenbank: " + (double) time / 1000.0 + " Sekunden");
    }

    /**
     * Beendet die Verbindung zur Datenbank
     * @throws SQLException wenn die Verbindung zur Datenbank nicht beendet werden kann / bereits beendet wurde
     */
    void closeConnection() throws SQLException {
        this.conn.close();
    }

}
