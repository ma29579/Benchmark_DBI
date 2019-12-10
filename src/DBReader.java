import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DBReader {

    Connection conn;
    PreparedStatement stmt;
    ResultSet results;


    //Konstruktor
    DBReader(String databaseURL, String username, String password) throws SQLException {
        conn = getConnection(databaseURL, username, password);
        conn.setAutoCommit(true);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public int kontostandTransaktion(int accid) throws SQLException {

        int balance;

        stmt = conn.prepareStatement("SELECT accounts.balance FROM tps100.accounts WHERE accounts.accid = ?;");
        stmt.setInt(1, accid);
        results = stmt.executeQuery();
        results.next();
        balance = results.getInt(1);

        results.close();
        stmt.close();

        return balance;
    }

    public int einzahlungsTransaktion(int accid, int tellerid, int branchid, int delta) throws SQLException {

        int balance;

        String callFunction = "SELECT tps100.einzahlungstransaktion(?,?,?,?,?);";

        stmt = conn.prepareStatement(callFunction);
        stmt.setInt(1, accid);
        stmt.setInt(2, tellerid);
        stmt.setInt(3, branchid);
        stmt.setInt(4, delta);
        stmt.setString(5,"012345678901234567890123456789");
        results = stmt.executeQuery();

        results.next();
        balance = results.getInt(1);

        stmt.close();
        results.close();

        return balance;
    }

    public int analyseTransaktion(int delta) throws SQLException {

        int historyNumber;

        stmt = conn.prepareStatement("SELECT COUNT(*) FROM tps100.history WHERE delta = ?;");
        stmt.setInt(1, delta);
        results = stmt.executeQuery();

        results.next();
        historyNumber = results.getInt(1);

        results.close();
        stmt.close();

        return historyNumber;
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

}
