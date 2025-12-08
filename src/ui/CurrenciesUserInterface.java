//package ui;
//
//import Currency.CurrencyService;
//import Global.Utils.Printer;
//
//import java.util.InputMismatchException;
//import java.util.Scanner;
//
//public class CurrenciesUserInterface {
//    private final Scanner scanner = new Scanner(System.in);
//    private final Printer printer = new Printer();
//    //    private final StartUpUserInterface startUpUserInterface = new StartUpUserInterface();
//    private final CurrencyService currencyService = new CurrencyService();
//
//
//
//
//
//    // function to show the manage currencies page
//    public void manageCurrenciesPage() {
//        // StartUp Message
//        printer.printColoredTitle("Manage currencies");
//
//        // wait for user to chose
//        while (true) {
//            // options message
////            printer.printColoredLine(Printer.BLUE, );
//            System.out.println();
//            System.out.println();
//            System.out.println();
//            System.out.println("[q] Quit / Logout");
//            printer.printPrompt("Your choice: ");
//
//            // input from the user
//            var choice = scanner.nextLine().toLowerCase().trim();
//
//            // based on the choice go to the next direction
//            switch (choice) {
//
//                case ("q"):
//                    printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
//                    System.exit(0); // Exit successfully
//                    break;
//                default:
//                    printer.printWrongChoice();
//            }
//        }
//    }
//}
