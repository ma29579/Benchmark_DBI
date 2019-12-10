
import org.postgresql.util.PSQLException;

import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionRunner extends Thread {

    public void run(){

        try{
            // Maximilian
            //DBReader reader = new DBReader("jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true","postgres","postgres");
            // Joshua
            //DBReader reader = new DBReader("jdbc:postgresql://localhost:5434/postgres?rewriteBatchedStatements=true","jen","");
            // VM
            DBReader reader = new DBReader("jdbc:postgresql://192.168.122.38:5432/DBI","postgres","dbidbi");
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
        while (System.currentTimeMillis() - startTime < 240000) {
            execute(reader);
        }

        // Messphase
        while (System.currentTimeMillis() - startTime < 540000) {
            execute(reader);
            count++;
        }

        System.out.println("count = " + count);

        // Ausschwingphase
        while (System.currentTimeMillis() - startTime < 600000) {
            execute(reader);
        }
    }

    private static void execute(DBReader reader) throws SQLException {

        int chosenMethod = ThreadLocalRandom.current().nextInt(0,100);
        int accid = ThreadLocalRandom.current().nextInt(1, 10000000);
        int tellerid = ThreadLocalRandom.current().nextInt(1, 1000);
        int branchid = ThreadLocalRandom.current().nextInt(1, 100);
        int delta = ThreadLocalRandom.current().nextInt(5, 501);

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
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
