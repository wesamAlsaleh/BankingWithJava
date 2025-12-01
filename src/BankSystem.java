
// This class is the entry point for the bank project

import Auth.AuthenticationService;
import Auth.BCryptService;

public class BankSystem {
    public static void main(String[] args) {
//        BCryptService bcryptService = new BCryptService();
//
//        System.out.println(bcryptService.hashPassword("password"));
//
//        String osName = System.getProperty("os.name");
//        System.out.println("Operating System Name: " + osName);
        AuthenticationService authenticationService = new AuthenticationService();

        authenticationService.register();

    }
}
