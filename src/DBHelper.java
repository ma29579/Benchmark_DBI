import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.DriverManager.getConnection;

public class DBHelper {

    private Connection conn;
    private PreparedStatement stmt;
    private ResultSet results;

    DBHelper(String databaseURL, String username, String password) throws SQLException {

        conn = getConnection(databaseURL, username, password);

    }

    public void createSchema() {

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
        for (int i = 1; i <= n; i++) {
            stmt = conn.prepareStatement(SQLStatements.getInsertIntoBranches(n, i));
            stmt.executeUpdate();
        }

        for (int i = 1; i <= n * 100; i++) {
            stmt = conn.prepareStatement(SQLStatements.getInsertIntoAccounts(n, i));
            stmt.executeUpdate();
        }

        for (int i = 1; i <= n * 10; i++) {
            stmt = conn.prepareStatement(SQLStatements.getInsertIntoTellers(n, i));
            stmt.executeUpdate();
        }
    }

    public void createNewConnection(String databaseURL, String username, String password) throws SQLException {

        this.conn = getConnection(databaseURL, username, password);
    }

    public void closeConnection() throws SQLException {
        this.conn.close();
    }

}