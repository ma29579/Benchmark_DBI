import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int n;

        System.out.println("Eingabe TPS-Anzahl:");
        n = input.nextInt();

        String databaseURL = "jdbc:postgresql://192.168.122.38:5432/DBI?rewriteBatchedStatements=true";
        String username = "postgres";
        String password = "dbidbi";


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
