
// This class is the entry point for the bank project

import Auth.AuthenticationService;

public class BankSystem {
    public static void main(String[] args) {
        AuthenticationService authenticationService = new AuthenticationService();

        authenticationService.testGetUser();

//        authenticationService.register();
//        authenticationService.login();
    }
}

// User 1 credentials: wesam@gmail.com and wwwweeee44
