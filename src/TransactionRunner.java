import org.postgresql.ds.PGConnectionPoolDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionRunner extends Thread {

    public void run(){

        try{
            DBReader reader = new DBReader("jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true","postgres","postgres");
            createTransactions(reader);
            reader.closeConnection();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


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

    private static void execute(DBReader reader) throws SQLException {

        int chosenMethod = ThreadLocalRandom.current().nextInt(0,100);


        if(chosenMethod < 35){
            reader.kontostandTransaktion(ThreadLocalRandom.current().nextInt(1,10000000));
        }
        else if(chosenMethod < 85){
            reader.einzahlungsTransaktion(
                    ThreadLocalRandom.current().nextInt(1,10000000),    // AccountID
                    ThreadLocalRandom.current().nextInt(1,1000),        // TellerID
                    ThreadLocalRandom.current().nextInt(1,100),         // BranchID
                    ThreadLocalRandom.current().nextInt(5,501));        // Delta
        }
        else{
            reader.analyseTransaktion(ThreadLocalRandom.current().nextInt(5,501));
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}