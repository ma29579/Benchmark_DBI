public class Main {

    public static void main(String[] args) {

        try {

            // Erzeugung der einzelnen Threads
            for (int i = 0; i < 5; i++) {
                TransactionRunner t = new TransactionRunner();
                // Durch den Aufruf der Methode Start, wird die in TransactionRunner implementierte Methode 'run' aufgerufen
                t.start();
            }


        } catch (Exception e) {
            System.out.println("Fehler bei der Datenbankverbindung!");
            e.printStackTrace();
        }

    }
}
