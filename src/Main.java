import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {


    public static void main(String[] args) {

        int n;
        String createSchemaAndTables;

        System.out.println("Eingabe TPS-Anzahl:");

        Scanner input = new Scanner(System.in);
        n = input.nextInt();

        String test = SQLStatements.getCreateSchema(n);

        try {

            DBHelper reader = new DBHelper("jdbc:postgresql://localhost:5432/DBI", "postgres", "postgres");
            reader.executeUpdate(test);


        } catch (SQLException e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
