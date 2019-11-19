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

    }

    void insertData(int n) throws SQLException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String schemaName = "tps" + n;

        String dummy20 = "abcdefghijklmnopqrst";
        String dummy68 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdef";
        String dummy72 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVQXYZ0123456789abcdefghij";

        String insertIntoBranches = "INSERT INTO %schemaName%.branches (branchid, branchname, balance, address)" +
                "VALUES (%branchid%, \'" + dummy20 + "\', 0, \'" + dummy72 + "\');";
        String insertIntoAccounts = "INSERT INTO %schemaName%.accounts (accid, name, balance, branchid, address)" +
                "VALUES (%accid%, \'" + dummy20 + "\', 0, %branchid%, \'" + dummy68 + "\');";
        String insertIntoTellers = "INSERT INTO %schemaName%.tellers (tellerid, tellername, balance, branchid, address)" +
                "VALUES (%tellerid%, \'" + dummy20 + "\', 0, %branchid%, \'" + dummy68 + "\');";

        for (int i = 1; i <= n; i++) {
            String insertStmt = insertIntoBranches.replace("%schemaName%", schemaName);
            insertStmt = insertStmt.replace("%branchid%", Integer.toString(i));
            stmt = conn.prepareStatement(insertStmt);
            stmt.executeUpdate();
        }

        for (int i = 1; i <= n * 100000; i++) {
            String insertStmt = insertIntoAccounts.replace("%schemaName%", schemaName);
            insertStmt = insertStmt.replace("%accid%", Integer.toString(i));
            insertStmt = insertStmt.replace("%branchid%", Integer.toString(ThreadLocalRandom.current().nextInt(1, n + 1)));
            stmt = conn.prepareStatement(insertStmt);
            stmt.executeUpdate();
        }

        for (int i = 1; i <= n * 10; i++) {
            String insertStmt = insertIntoTellers.replace("%schemaName%", schemaName);
            insertStmt = insertStmt.replace("%tellerid%", Integer.toString(i));
            insertStmt = insertStmt.replace("%branchid%", Integer.toString(ThreadLocalRandom.current().nextInt(1, n + 1)));
            stmt = conn.prepareStatement(insertStmt);
            stmt.executeUpdate();
        }
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
