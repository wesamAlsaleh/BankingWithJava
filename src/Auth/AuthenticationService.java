package Auth;

import Global.Utils.Printer;
import User.User;
import User.UserRepository;
import User.UserValidation;
import ui.UserInterfaces;

import java.util.Scanner;

public class AuthenticationService {
    private final Scanner input = new Scanner(System.in);
    private final BCryptService bcryptService = new BCryptService();
    private final UserRepository userRepository = new UserRepository();
    private final UserValidation userValidation = new UserValidation();
    private static final Printer printer = new Printer();
    private static final UserInterfaces ui = new UserInterfaces();

    // function to register a new user
    public void register() {
        // response container
        String reply;

        // initial message
        printer.printColoredTitle("Create new account in GA01 Bank");

        while (true) {
            // store the first name
            printer.printQuestion("What is your first name:");
            var firstName = input.nextLine();

            // store the last name
            printer.printQuestion("What is your last name:");

            var lastName = input.nextLine();

            // store the email
            printer.printQuestion("What is your email address:");

            var email = input.nextLine();

            // store the hashed password
            printer.printQuestion("What is your password:");
            var password = bcryptService.hashPassword(input.nextLine());

            // validate the input
            reply = userValidation.validateRegisterInput(firstName, lastName, email, password);

            // if no error then create new user
            if (reply.isEmpty()) {
                // check if the email exists in the system
                if (userRepository.getUserByEmail(email) != null) {
                    // print error message
                    printer.printError("User with email " + email + " already exists!");

                    // restart the loop
                    continue;
                }

                // generate new id
                var id = userRepository.generateUserId();

                // create user record and todo: customer record
                var user = new User(id, firstName, lastName, email, password);

                // create user file
                userRepository.saveUser(user);

                // exit the while loop
                break;
            } else {
                // print the errors available
                printer.printError(reply);
            }
        }

        // redirect to startup page
        ui.startApplication();
    }

    // function to log in the user
    public void login() {
        // response container
        String reply;

        // initial message
        printer.printColoredTitle("login to your account in GA01 Bank");

        while (true) {
            // get the email from the user
            printer.printQuestion("Enter your email address:");
            var email = input.nextLine();

            // get the password from the user
            printer.printQuestion("Enter your password:");
            var password = input.nextLine();

            // validate the input
            reply = userValidation.validateLoginInput(email, password);

            // if no error then try to log in the user
            if (reply.isEmpty()) {
                // get the user that has the email
                var user = userRepository.getUserByEmail(email);

                // if the user is not available in the system return error
                if (user == null) {
                    // not found error:
                    printer.printError("Sorry, that email does not exist in the system!");

                    // restart the loop
                    continue;
                }

                // if the password is not correct
                if (!bcryptService.verifyPassword(password, user.getPassword())) {
                    // wrong password message
                    printer.printError("Wrong password, try again!");

                    // increase the attempts counter
                    userRepository.increaseFraudAttemptsCounter(user.getId());

                    // if the counter is more than 3 terminate the login attempt
                    if (user.getFraudAttemptsCount() >= 3) {
                        // locked message
                        printer.printError("This account has already been locked! try after one minute");

                        // exit the while loop
                        break;
                    }

                    continue;  // restart the loop
                } // end of if statement of wrong password

                // check if the account is locked
                if (user.isLocked()) {
                    // locked account message
                    printer.printError("Your account has been locked! please try again later.");

                    // terminate the attempt
                    break;
                }

                // redirect to home page
                ui.homePage(user);

                // exit the loop (should not reach here)
                break;
            } else {
                // print the errors available
                printer.printError(reply);
            }
        } // end of while loop
    }
}
