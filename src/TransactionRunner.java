import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionRunner extends Thread {

    /**
     * Überschreibt die in der Klasse Thread implementierte Methode 'run'
     * Instanziiert ein Objekt der Klasse DBReader, beginnt die Transaktionsmessung und schließt daraufhin die benötigte Datenbankverbindung
     */
    public void run(){

        try{
            // Maximilian
            DBReader reader = new DBReader("jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true","postgres","postgres");
            // Joshua
            //DBReader reader = new DBReader("jdbc:postgresql://localhost:5434/postgres?rewriteBatchedStatements=true","jen","");
            // VM
            //DBReader reader = new DBReader("jdbc:postgresql://192.168.122.38:5432/DBI","postgres","dbidbi");
            createTransactions(reader);
            reader.closeConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Wird für die Einhaltung des zeitlichen Rahmens und der Messung der durchgeführten Transaktionen benötigt
     * @param reader erwartet ein Objekt der Klasse DBReader, um den Aufruf der Methode execute durchzuführen
     * @throws SQLException
     */
    private static void createTransactions(DBReader reader) throws SQLException {

        long startTime = System.currentTimeMillis();
        int count = 0;

        // Einschwingphase
        while (System.currentTimeMillis() - startTime < 2400) {
            execute(reader);
        }

        // Messphase
        while (System.currentTimeMillis() - startTime < 5400) {
            execute(reader);
            count++;
        }

        System.out.println("count = " + count);

        // Ausschwingphase
        while (System.currentTimeMillis() - startTime < 6000) {
            execute(reader);
        }
    }

    /**
     * Ausführung einer der in DBReader formulierten Transaktion mit dafür ermittelten Parametern
     * @param reader erwartet ein Objekt der Klasse DBReader, um die benötigten Methoden auszuführen
     * @throws SQLException
     */
    private static void execute(DBReader reader) throws SQLException {

        // Ermittlung der Zufallszahl für die Transaktionsauswahl
        int chosenMethod = ThreadLocalRandom.current().nextInt(0,100);

        // Ermittlung von ggf. benötigten Zufallszahlen für die ausgewählte Transaktion
        int accid = ThreadLocalRandom.current().nextInt(1, 10000000);
        int tellerid = ThreadLocalRandom.current().nextInt(1, 1000);
        int branchid = ThreadLocalRandom.current().nextInt(1, 100);
        int delta = ThreadLocalRandom.current().nextInt(5, 501);

        // Ausführung der Transaktion
        while(true) {
            try {
                if (chosenMethod < 35) {
                    reader.kontostandTransaktion(accid);
                } else if (chosenMethod < 85) {
                    reader.einzahlungsTransaktion(accid,tellerid,branchid,delta);
                } else {
                    reader.analyseTransaktion(delta);
                }
                break;
            }
            catch (PSQLException e){

            }

        }

        // Wartezeit nach der Durchführung einer Transaktion
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
