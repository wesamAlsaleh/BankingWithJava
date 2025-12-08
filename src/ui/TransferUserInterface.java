//package ui;
//
//import Account.Account;
//import Account.AccountRepository;
//import Account.AccountService;
//import Global.Utils.Printer;
//import Transaction.TransactionService;
//import User.User;
//import User.UserRepository;
//import User.UserValidation;
//
//import java.util.List;
//import java.util.Scanner;
//
//public class TransferUserInterface {
//    private final Scanner scanner = new Scanner(System.in);
//    private final Printer printer = new Printer();
//    private final AccountService accountService = new AccountService();
//    private final TransactionService transactionService = new TransactionService();
//    private final UserRepository userRepository = new UserRepository();
//    private final UserValidation userValidation = new UserValidation();
//
//
//
//    // function to show the transfer page
////    public void transferPage(User user) {
////        // init message
////        printer.printColoredTitle("Transfer");
////        System.out.println("[H] Transfer history");
////        System.out.println("[T] Transfer to");
////        System.out.println("[q] Quit / Logout");
////        printer.printPrompt("Your choice: ");
////
////        // input from the user
////        var choice = scanner.nextLine().toLowerCase().trim();
////
////        // based on the choice go to the next direction
////        switch (choice) {
////            case ("h"):
////                userTransferHistoryPage(user);
////                break;
////            case ("t"):
////                transferToPage(user);
////                break;
////            case ("q"):
////                printer.printSuccessful("Thank you for using GA01 Bank. Goodbye!");
////                System.exit(0); // Exit the terminal
////                break;
////            default:
////                printer.printWrongChoice();
////        }
////    }
//}
