package Auth;

import User.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class AuthenticationService {
    private static final Scanner input = new Scanner(System.in);
    private static final BCryptService bcryptService = new BCryptService();

    // function to register a new user
    public void register() {
        // response container
        String reply;

        // initial message
        System.out.println("Welcome to GA01 Bank");

        // path's for the files
        var indexesPath = "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\user_index.txt";
        var recordsPath = "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\users";

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
            reply = validateUserInput(firstName, lastName, email, password);

            // if no error then create new user
            if (reply.isEmpty()) {
                // Get the index file (make sure of the path in WIN/MACOS)
                File fileObject = new File(indexesPath);

                // index container
                long id = 0; // with initial value just in case

                // try to get the last id number in the system from the index file
                try (Scanner scanner = new Scanner(fileObject)) {
                    // While there is lines to read
                    while (scanner.hasNextLine()) {
                        // read the index
                        String index = scanner.nextLine();

                        // convert it to long and then increase it by one
                        id = Long.parseLong(index);
                        id++;

                        // update the index file with the new index
                        try (FileWriter writer = new FileWriter(indexesPath)) {
                            // write the new id as string
                            writer.write(String.valueOf(id));
                        } catch (IOException e) {
                            System.out.println("Something went wrong while writing the indexes. Please try again.");
                            System.out.println(e.getMessage());
                        }
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Something went wrong while reading the indexes. Please try again.");
                    System.out.println(e.getMessage());
                }

                // create user record and customer record
                var user = new User(id, firstName, lastName, email, password);

                // create new file to store the new user record
                try {
                    // prepare the new file name to be created
                    var fileName = "Customer-" + user.getFirstName() + "_" + user.getLastName() + "-" + user.getId() + ".txt";

                    // prepare the path of the new record
                    var newRecordPath = recordsPath + "\\" + fileName;

                    // prepare the file object to create the text (id.txt)
                    File newFileObject = new File(newRecordPath);

                    // try to create the file
                    if (newFileObject.createNewFile()) {
                        // successful message
                        System.out.println("User with the ID " + id + " created successfully!");
                    } else {
                        // if the file with the same name exits
                        System.out.println("File already exists.");
                    }

                    // add the record to the created file
                    try (FileWriter writer = new FileWriter(newRecordPath)) {
                        // write the user record
                        // Customer-<CustomerName>-<CustomerID>
                        writer.write(user.getUserRecord());
                    } catch (IOException e) {
                        System.out.println("Something went wrong while writing the indexes. Please try again.");
                        System.out.println(e.getMessage());
                    }

                } catch (IOException e) {
                    System.out.println("Something went wrong while writing the new user record. Please try again.");
//                    e.printStackTrace(); // Print error details
                }

                // exit the loop
                break;
            } else {
                // print the errors available
                System.out.println(reply);
                System.out.println(" ");
            }
        }
    }

    // todo: function to log in the user

    // function to validate user names
    private String validateName(String name) {
        // if the name is null
        if (name == null) {
            return "Name must be provided.";
        }
        // trim the name
        name = name.trim();

        // if the name is empty "" or blank " " return error
        if (name.isBlank()) {
            return "Name is empty, please add a valid name!";
        }

        // if the name is less than 3 char return error
        if (name.length() < 3) {
            return "Name is too short, please add a valid name!";
        }

        // if the name is more than 20 char return error
        if (name.length() > 20) {
            return "Name is too long, please add a valid name!";
        }

        // return empty, so its valid
        return ""; // valid
    }

    // function to validate the email
    private String validateEmail(String email) {
        if (email == null) {
            return "Email is empty, please add a valid email!";
        }

        // remove spaces from the email
        email = email.trim();

        // if empty "" or blank " " return error
        if (email.isBlank()) {
            return "Email is empty, please add a valid email!";
        }

        // basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            return "Email is not valid, please add a valid email!";
        }

        // basic email validation
        if (email.length() < 5 || email.length() > 50) {
            return "Email length must be between 5 and 50 characters only!";
        }

        // valid email
        return "";
    }

    // function to validate the password
    private String validatePassword(String password) {
        // null validation
        if (password == null) {
            return "Password is empty, please add a valid password!";
        }

        // remove spaces
        password = password.trim();

        // length validation
        if (password.length() < 6) {
            return "Password is too short, please add a valid password!";
        }

        // length validation
        if (password.length() < 30) {
            return "Password is too long, please add a valid password!";
        }

        // ensure the password contain letters and numbers
        var hasLetter = password.matches(".*[a-zA-Z].*");
        var hasDigit = password.matches(".*\\d.*");

        // if the password does not contain letters or digits return error
        if (!hasLetter || !hasDigit) {
            return "Password must contain letters and numbers!";
        }

        // valid password
        return "";
    }

    // function to check if there is a validation issue in the input
    private String validateUserInput(
            String firstName,
            String lastName,
            String email,
            String password) {
        // String to contain the errors
        StringBuilder errors = new StringBuilder();

        // validate the first name
        var firstNameError = validateName(firstName);

        // if there is an error add it to the string
        if (!firstNameError.isEmpty()) {
            errors.append(firstNameError).append("\n");
        }

        // validate the last name
        var lastNameError = validateName(lastName);

        // if there is an error add it to the string
        if (!lastNameError.isEmpty()) {
            errors.append(lastNameError).append("\n");
        }

        // validate the email
        var emailError = validateEmail(email);

        // if there is an error add it to the string
        if (!emailError.isEmpty()) {
            errors.append(emailError).append("\n");
        }

        // validate the last name
        var passwordError = validatePassword(password);

        // if there is an error add it to the string
        if (!passwordError.isEmpty()) {
            errors.append(passwordError).append("\n");
        }

        // return the errors available
        return errors.toString().trim();
    }
}
