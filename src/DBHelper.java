import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.time.StopWatch;

import static java.sql.DriverManager.getConnection;

public class DBHelper {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet results;

    DBHelper(String databaseURL, String username, String password) throws SQLException {

        conn = getConnection(databaseURL, username, password);
        conn.setAutoCommit(false);

    }


    public ResultSet executeQuery(String sqlStatement) throws SQLException {

        stmt = conn.prepareStatement(sqlStatement);
        results = stmt.executeQuery();

        return results;

    }


    void executeUpdate(String sqlStatement) throws SQLException {

        stmt = conn.prepareStatement(sqlStatement);
        stmt.executeUpdate();
        stmt.close();

    }

    void insertData(int n) throws SQLException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String schemaName = "tps" + n;

        String insertIntoBranches = "INSERT INTO " + schemaName + ".branches (branchid, branchname, balance, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdefghij\');";
        String insertIntoAccounts = "INSERT INTO " + schemaName + ".accounts (accid, name, balance, branchid, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, ?, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdef\');";
        String insertIntoTellers = "INSERT INTO " + schemaName + ".tellers (tellerid, tellername, balance, branchid, address)" +
                "VALUES (?, \'abcdefghijklmnopqrst\', 0, ?, \'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdef\');";

        stmt = conn.prepareStatement(insertIntoBranches);

        for (int i = 1; i <= n; i++) {
            stmt.setInt(1, i);
            stmt.addBatch();
        }

        stmt.executeBatch();

        stmt = conn.prepareStatement(insertIntoAccounts);

        for (int i = 1; i <= n * 100000; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, ThreadLocalRandom.current().nextInt(1, n + 1));
            stmt.addBatch();
        }

        stmt.executeBatch();

        stmt = conn.prepareStatement(insertIntoTellers);

        for (int i = 1; i <= n * 10; i++) {
            stmt.setInt(1, i);
            stmt.setInt(2, ThreadLocalRandom.current().nextInt(1, n + 1));
            stmt.addBatch();
        }

        stmt.executeBatch();
        conn.commit();

        stopWatch.stop();
        long time = stopWatch.getTime();
        System.out.println("time = " + time);
    }

    public void createNewConnection(String databaseURL, String username, String password) throws SQLException {

        this.conn = getConnection(databaseURL, username, password);
    }

    public void closeConnection() throws SQLException {
        this.conn.close();
    }

}
