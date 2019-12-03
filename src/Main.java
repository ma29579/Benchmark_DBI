import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    public static void main(String[] args) {

        System.out.println("Eingabe Connection (1: Max; 2: Joshua; 3: VM; 4: Remote)");
        Scanner input = new Scanner(System.in);
        int connection = input.nextInt();

        int n;

        //System.out.println("Eingabe TPS-Anzahl:");

        //n = input.nextInt();

        String databaseURL = "";
        String username = "";
        String password = "";


        if (connection == 1) {
            databaseURL = "jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true";
            username = "postgres";
            password = "postgres";
        } else if (connection == 2) {
            databaseURL = "jdbc:postgresql://localhost:5433/postgres?rewriteBatchedStatements=true";
            username = "jen";
            password = "";
        } else if (connection == 3){
            databaseURL = "jdbc:postgresql://localhost:5432/DBI?rewriteBatchedStatements=true";
            username = "postgres";
            password = "dbidbi";
        } else {
            databaseURL = "jdbc:postgresql://192.168.122.38:5432/DBI?rewriteBatchedStatements=true";
            username = "postgres";
            password = "dbidbi";
        }

        try {
            // Instanziierung des DBWriters
            //DBWriter writer = new DBWriter(databaseURL, username, password);

            DBReader reader = new DBReader(databaseURL,username,password);

            run(reader);

            // Initialisierung der Benchmark-Datenbank
            //writer.createBenchmarkDatabase(n);
            //writer.insertData(n);
            //writer.closeConnection();

        } catch (SQLException e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }

    public static void run(DBReader reader) throws SQLException {

        long startTime = System.currentTimeMillis();
        int count = 0;

        // Einschwingphase
        while(System.currentTimeMillis() - startTime < 240000){
            execute(reader);
        }

        // Messphase
        while(System.currentTimeMillis() - startTime < 540000){
            execute(reader);
            count++;
        }

        System.out.println("count = " + count);

        // Ausschwingphase
        while (System.currentTimeMillis() - startTime < 600000){
            execute(reader);
        }

    }

    public static void execute(DBReader reader) throws SQLException {

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
