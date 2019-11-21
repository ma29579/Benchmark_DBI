import java.sql.SQLException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        System.out.println("Eingabe Connection (1: Max; 2: Joshua; 3: VM)");
        Scanner input = new Scanner(System.in);
        int connection = input.nextInt();

        int n;

        System.out.println("Eingabe TPS-Anzahl:");

        n = input.nextInt();

        String databaseURL = "";
        String username = "";
        String password = "";


        if (connection == 1) {
            databaseURL = "jdbc:postgresql://localhost:5432/DBI";
            username = "postgres";
            password = "postgres";
        } else if (connection == 2) {
            databaseURL = "jdbc:postgresql://localhost:5433/postgres";
            username = "jen";
            password = "";
        } else {
            databaseURL = "jdbc:postgresql://localhost:5432/DBI";
            username = "postgres";
            password = "dbidbi";
        }

        try {

            DBHelper reader = new DBHelper(databaseURL, username, password);
            reader.createBenchmarkDatabase(n);
            reader.insertData(n);


        } catch (SQLException e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
