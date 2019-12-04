
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class TransactionRunner extends Thread {

    public void run(){

        try{
            // Maximilian
            DBReader reader = new DBReader("jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true","postgres","postgres");
            // Joshua
            //DBReader reader = new DBReader("jdbc:postgresql://localhost:5433/postgres?rewriteBatchedStatements=true","jen","");
            // VM
            //DBReader reader = new DBReader("jdbc:postgresql://192.168.122.38:5432/DBI?rewriteBatchedStatements=true","postgres","dbidbi");
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
