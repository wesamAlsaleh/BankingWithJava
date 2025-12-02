
// This class is the entry point for the bank project

import Auth.AuthenticationService;

public class BankSystem {
    public static void main(String[] args) {
        AuthenticationService authenticationService = new AuthenticationService();

        authenticationService.register();
//        authenticationService.testGetUser();
    }
}
