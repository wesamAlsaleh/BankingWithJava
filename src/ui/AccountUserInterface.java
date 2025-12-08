//package ui;
//
//import Account.AccountService;
//import Account.AccountType;
//import Currency.CurrencyService;
//import Global.Utils.Printer;
//import User.User;
//
//import java.util.Scanner;
//
//public class AccountUserInterface {
//    private final Scanner scanner = new Scanner(System.in);
//    private final Printer printer = new Printer();
//    private final AccountService accountService = new AccountService();
//    private final CurrencyService currencyService = new CurrencyService();
//
//
//
//    // function to show manage accounts page
//    public void manageAccountsPage(User user) {
//        // StartUp Message
//        printer.printColoredTitle("Manage accounts");
//
//        while (true) {
//            // options message
//            printer.printColoredLine(Printer.BLUE, "Please choose an option:");
//
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
//                    System.exit(0); // Exit the terminal
//                    break;
//                default:
//                    printer.printWrongChoice();
//            }
//        }
//
//    }
//}
