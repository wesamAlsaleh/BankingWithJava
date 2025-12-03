package Auth;

import Global.Utils.FileHandler;
import User.User;
import User.UserRepository;
import User.UserValidation;

import java.time.LocalDateTime;
import java.util.Scanner;

public class AuthenticationService {
    private final Scanner input = new Scanner(System.in);
    private final BCryptService bcryptService = new BCryptService();
    private final UserRepository userRepository = new UserRepository();
    private final UserValidation userValidation = new UserValidation();

    // function to register a new user
    public void register() {
        // response container
        String reply;

        // initial message
        System.out.println("Welcome to GA01 Bank");

        while (true) {
            // store the first name
            System.out.println("What is your first name?");
            var firstName = input.nextLine();

            // store the last name
            System.out.println("What is your last name?");
            var lastName = input.nextLine();

            // store the email
            System.out.println("What is your email address?");
            var email = input.nextLine();

            // store the hashed password
            System.out.println("What is your password?");
            var password = bcryptService.hashPassword(input.nextLine());

            // validate the input
            reply = userValidation.validateRegisterInput(firstName, lastName, email, password);

            // if no error then create new user
            if (reply.isEmpty()) {
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
                System.out.println(reply);
                System.out.println(" ");
            }
        }
    }

    // todo: function to log in the user
    public void login() {
        // response container
        String reply;

        // initial message
        System.out.println("Welcome to GA01 Bank");

        while (true) {
            // get the email from the user
            System.out.println("Enter your email address:");
            var email = input.nextLine();

            // get the password from the user
            System.out.println("Enter your password:");
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
                    printMessage("Sorry, that email does not exist in the system!");

                    // restart the loop
                    continue;
                }

                // if the password is not correct
                if (!bcryptService.verifyPassword(password, user.getPassword())) {
                    // wrong password message
                    printMessage("Wrong password, try again!");

                    // update the user file record
                    var doneIncreaseOperation = userRepository.increaseFraudAttemptsCounter(user.getId());

                    // if failed to do the increment
                    if (!doneIncreaseOperation) {
                        System.out.println("Failed to increase the fraud counter");
                    }

                    // if the counter is more than 3 lock the account
                    if (user.getFraudAttemptsCount() + 1 > 3) {
                        // add 1 minute lock
                        user.setLockUntil(LocalDateTime.now().plusMinutes(1));

                        // update the file

                        // restart the loop
                        continue;
                    }
                } // end of if statement of wrong password

                // if the account is locked before (lockUntil is not null)
                if (user.getLockUntil() != null) {
                    // check if the account is locked (lock time is after the current time)
                    if (user.getLockUntil().isAfter(LocalDateTime.now())) {
                        // locked account message
                        printMessage("Your account has been locked! please try again later.");

                        // exit the while loop
                        break;
                    }
                }
            } else {
                // print the errors available
                System.out.println(reply);
                System.out.println(" ");
            }
        } // end of while loop

        // todo: redirect to bank features UI
        System.out.println("logged in successfully");
    }

    // function to test the get user by id
    public void testGetUser() {
        var user = userRepository.getUserById(10);
        System.out.println(user);
    }

    // function to print message with space below in the terminal
    private void printMessage(String message) {
        System.out.println(message);
        System.out.println(" ");
    }
}
