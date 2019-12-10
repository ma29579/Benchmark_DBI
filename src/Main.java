public class Main {

    public static void main(String[] args) {

        try {

            for (int i = 0; i < 5; i++) {
                TransactionRunner t = new TransactionRunner();
                t.start();
            }


        } catch (Exception e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
