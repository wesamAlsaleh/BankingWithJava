package User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserRepository {
    // function to get the next new user id
    public long generateUserId() {
        // path for the file that contain the id's
        var indexesPath = "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\user_index.txt";

        // Get the index file "user_index.txt" (make sure of the path in WIN/MACOS)
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

                // update the index file with the new index for the next user
                try (FileWriter writer = new FileWriter(indexesPath)) {
                    // write the new id as string
                    writer.write(String.valueOf(id));
                } catch (IOException e) {
                    System.out.println("Something went wrong while updating the indexes. Please try again.");
                    System.out.println(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while reading the indexes. Please try again.");
            System.out.println(e.getMessage());
        }

        // return the id to create new user
        return id;
    }

    // function to save the user in a file
    public void saveUser(User user) {
        // user files (root directory)
        var recordsPath = "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\users";

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
                System.out.println("User with the ID " + user.getId() + " created successfully!");
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
        }
    }

    // function to find user file by id

    // function to find user by email

}
