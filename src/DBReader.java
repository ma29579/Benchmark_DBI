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
        conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
    }

    /**
     * Simuliert die Abfrage des Kontostands eines Kunden
     * @param accid entspricht der Kontonummer des besagten Kunden
     * @return gibt den entsprechenden Kontostand zurück
     * @throws SQLException
     */
    public int kontostandTransaktion(int accid) throws SQLException {

        int balance;

        // Initialisierung des PreparedStatements
        stmt = conn.prepareStatement("SELECT accounts.balance FROM tps100.accounts WHERE accounts.accid = ?;");
        // Einfügen der erhaltenen 'accid' in das PreparedStatement
        stmt.setInt(1, accid);
        // Ausführung des PreparedStatements
        results = stmt.executeQuery();

        // Auslesen des ermittelten Wertes für das Attribut 'balance'
        results.next();
        balance = results.getInt(1);

        results.close();
        stmt.close();

        conn.commit();

        return balance;
    }

    /**
     * Simuliert die Einzahlung eines Geldbetrags und führt dementsprechende Operationen durch
     * @param accid identifiziert den gewünschten Tupel innerhalb der Relation 'Accounts'
     * @param tellerid identifiziert den gewünschten Tupel innerhalb der Relation 'Tellers'
     * @param branchid identifiziert den gewünschten Tupel innerhalb der Relation 'Branches'
     * @param delta repräsentiert den Einzahlungsbetrag
     * @return der veränderte Kontostand wird zurückgegeben
     * @throws SQLException
     */
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

        conn.commit();

        return balance;
    }

    /**
     * Ermittelt die Anzahl von Tupeln, dessen Attribut 'delta' den Wert des Parameters delta besitzt
     * @param delta entspricht dem Einzahlungsbetrag, der für die Suche in der Relation verwendet wird
     * @return gibt die Anzahl der gefundenen Tupel zurück
     * @throws SQLException
     */
    public int analyseTransaktion(int delta) throws SQLException {

        int historyNumber;

        stmt = conn.prepareStatement("SELECT COUNT(*) FROM tps100.history WHERE delta = ?;");
        stmt.setInt(1, delta);
        results = stmt.executeQuery();

        results.next();
        historyNumber = results.getInt(1);

        results.close();
        stmt.close();

        conn.commit();

        return historyNumber;
    }

    /**
     * Schließt die aufgebaute Verbindung zu dem Datenbankserver
     * @throws SQLException
     */
    public void closeConnection() throws SQLException {
        conn.close();
    }

    public void rollbackTransaction() throws SQLException{
        conn.rollback();
    }

}
