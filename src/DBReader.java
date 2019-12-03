import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class DBReader {

    Connection conn;
    PreparedStatement stmt;
    ResultSet results;


    //Konstruktor
    DBReader(String databaseURL, String username, String password) throws SQLException{
        conn = getConnection(databaseURL,username,password);
        conn.setAutoCommit(false);
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    public int kontostandTransaktion(int accid) throws SQLException {

        stmt = conn.prepareStatement("SELECT balance FROM tps100.accounts WHERE accid = " + accid);
        results = stmt.executeQuery();
        results.next();

        return results.getInt(1);
    }

    public int einzahlungsTransaktion(int accid,int tellerid, int branchid, int delta) throws SQLException {

        int balance = 0;

        String updateBranches = "UPDATE tps100.branches SET balance = balance + " + delta  + "WHERE branchid = " + branchid + ";";
        String updateTellers = "UPDATE tps100.tellers SET balance = balance + " + delta  + "WHERE tellerid = " + tellerid + ";";
        String updateAccounts = "UPDATE tps100.accounts SET balance = balance + " + delta  + "WHERE accid = " + accid + ";";

        String selectBalance = "SELECT balance FROM tps100.accounts WHERE accid = " + accid;

        String insertIntoHistory = "INSERT INTO tps100.history(accid,tellerid,delta,branchid,accbalance,cmmnt) " +
                                    "VALUES (" + accid + "," + tellerid + "," + delta + "," + branchid + ", ?" +
                                    ",'012345678901234567890123456789');";

        stmt = conn.prepareStatement(updateBranches + updateTellers + updateAccounts);
        stmt.executeUpdate();
        stmt.close();

        stmt = conn.prepareStatement(selectBalance);
        results = stmt.executeQuery();
        results.next();
        balance = results.getInt(1);
        stmt.close();

        stmt = conn.prepareStatement(insertIntoHistory);
        stmt.setInt(1,balance);
        stmt.executeUpdate();
        stmt.close();

        conn.commit();

        return balance;
    }

    public void clearHistory() throws SQLException{

        stmt = conn.prepareStatement("DELETE FROM tps100.history");
        stmt.executeUpdate();

        conn.commit();
    }

    public int analyseTransaktion(int delta) throws SQLException {

        stmt = conn.prepareStatement("SELECT COUNT(*) FROM tps100.history WHERE delta = " + delta);
        results = stmt.executeQuery();

        results.next();

        return results.getInt(1);
    }

}
