import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DBReader {

    Connection conn;
    PreparedStatement stmt;
    ResultSet results;


    //Konstruktor
    DBReader(String databaseURL, String username, String password) throws SQLException {
        conn = getConnection(databaseURL, username, password);
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    public int kontostandTransaktion(int accid) throws SQLException {

        stmt = conn.prepareStatement("SELECT tps100.kontostandtransaktion(?);");
        stmt.setInt(1, accid);
        results = stmt.executeQuery();
        results.next();

        return results.getInt(1);
    }

    public int einzahlungsTransaktion(int accid, int tellerid, int branchid, int delta) throws SQLException {

        int balance;

        String callFunction = "SELECT tps100.einzahlungstransaktion(?,?,?,?);";

        stmt = conn.prepareStatement(callFunction);
        stmt.setInt(1, accid);
        stmt.setInt(2, tellerid);
        stmt.setInt(3, branchid);
        stmt.setInt(4, delta);
        results = stmt.executeQuery();

        results.next();
        balance = results.getInt(1);

        stmt.close();
        conn.commit();

        return balance;
    }

    public int analyseTransaktion(int delta) throws SQLException {

        stmt = conn.prepareStatement("SELECT tps100.analysetransaktion(?);");
        stmt.setInt(1, delta);
        results = stmt.executeQuery();

        results.next();

        return results.getInt(1);
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }

}
