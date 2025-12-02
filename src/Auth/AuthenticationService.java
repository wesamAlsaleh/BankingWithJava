package Auth;

import User.User;
import User.UserRepository;
import User.UserValidation;

import java.util.Scanner;

public class AuthenticationService {
    private static final Scanner input = new Scanner(System.in);
    private static final BCryptService bcryptService = new BCryptService();
    private static final UserRepository userRepository = new UserRepository();
    private static final UserValidation userValidation = new UserValidation();

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

        // exit flag
        var exitLogin = false;

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
                // verify the password

            }

            // exit the while loop
            break;
        }
    }
}
