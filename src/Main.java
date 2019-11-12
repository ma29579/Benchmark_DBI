import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        int n;

        System.out.println("Eingabe Produkt-ID:");

        Scanner input = new Scanner(System.in);
        n = input.nextInt();

        try {

            DBHelper reader = new DBHelper("jdbc:postgresql://localhost:5432/DBI", "postgres", "postgres");
            ResultSet queryResults;


        } catch (SQLException e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
