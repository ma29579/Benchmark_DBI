import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Eingabe Connection (1: Max; 2: Joshua; 3: VM; 4: Remote)");
        Scanner input = new Scanner(System.in);
        int connection = input.nextInt();

        int n;

        System.out.println("Eingabe TPS-Anzahl:");

        n = input.nextInt();

        String databaseURL = "";
        String username = "";
        String password = "";


        if (connection == 1) {
            databaseURL = "jdbc:postgresql://192.168.51.132:5432/dbi?rewriteBatchedStatements=true";
            username = "postgres";
            password = "dbi";
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
            DBWriter writer = new DBWriter(databaseURL, username, password);

            // Initialisierung der Benchmark-Datenbank
            writer.createBenchmarkDatabase(n);
            writer.insertData(n);
            writer.closeConnection();

        } catch (SQLException e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
