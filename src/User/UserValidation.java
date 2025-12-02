package User;

public class UserValidation {
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

    // function to check if there is a validation issue in the input for the registeration
    public String validateRegisterInput(
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

        // validate the password
        var passwordError = validatePassword(password);

        // if there is an error add it to the string
        if (!passwordError.isEmpty()) {
            errors.append(passwordError).append("\n");
        }

        // return the errors available
        return errors.toString().trim();
    }

    // function to check if there is a validation error for the login
    public String validateLoginInput(String email, String password) {
        // String to contain the errors
        StringBuilder errors = new StringBuilder();

        // validate the email todo: refactor duplicate code
        var emailError = validateEmail(email);

        // if there is an error add it to the string
        if (!emailError.isEmpty()) {
            errors.append(emailError).append("\n");
        }

        // validate the password
        var passwordError = validatePassword(password);

        // if there is an error add it to the string
        if (!passwordError.isEmpty()) {
            errors.append(passwordError).append("\n");
        }

        // return the errors available
        return errors.toString().trim();
    }
}
