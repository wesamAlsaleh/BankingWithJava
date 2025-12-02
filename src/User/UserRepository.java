package User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/*
 * Notes:
 * - File objects represent a file or folder path in the filesystem.
 *   A File object doesn’t hold the actual file contents, it represents a path to a file or directory.
 * - Scanner can read input from a File object.
 * - FileWriter writes output to a file, given its file name or path.
 */

public class UserRepository {
    // function to get the next new user id
    public long generateUserId() {
        // path for the file that contain the id's
        var indexesPath = getUserIndexPath();

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
        var recordsPath = getUsersPath();

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
//    public User getUserById(long id) {
//        // get the users folder as file object
//       var fileObject = new File(getUsersPath());
//
//       // create array of files (users files) from the file object
//        File[] files = Objects.requireNonNull(fileObject.listFiles()); // Throws NullPointerExceptio if null!
//
//        // get the user file by his id
//        for (File file : files) {
//            // check if the name contains the id
//            if (file.getName().endsWith("-" + id + ".txt")) {
//                // Read the Users file content
//                try (Scanner scanner = new Scanner()) {
//                    // read the content line by line
//                    String recordLine = scanner.nextLine();
//
//                    // split the line into parts (ex: index0: First_Name:wesam and index1: Last_Name:muneer ...)
//                    ArrayList<String> parts = new ArrayList<>(List.of(recordLine.split(",")));
//
//                    // get the part from the array and then split it two part and take the right part (after the ':')
//                    var firstName = parts.get(0).split(":")[1].trim();
//                    var lastName = parts.get(1).split(":")[1].trim();
//                    var email = parts.get(2).split(":")[1].trim();
//                    var hashedPassword = parts.get(3).split(":")[1].trim();
//                    var role = parts.get(4).split(":")[1].trim();
//
//                    // for the LocalDateTime, get the part from the array and then get the thing after the ':' (index of ':' + 1)
//                    var createdDate = parts.get(5).substring(parts.get(5).indexOf(':') + 1).trim();
//                    var updatedDate = parts.get(6).substring(parts.get(6).indexOf(':') + 1).trim();
//
//                    return new User(id, firstName, lastName, email, hashedPassword, UserRole.valueOf(role), LocalDateTime.parse(createdDate), LocalDateTime.parse(updatedDate));
//                } catch (FileNotFoundException e) {
//                    System.out.println("Something went wrong while reading the user file. Please try again.");
//                    System.out.println(e.getMessage());
//                } // catch end
//            } // if end
//        } // for end
//
//        return null; // it should never be here
//    } // function end

    // function to find user by email
    public void getUserByEmail(String email) {
        //
    }

    // function to get the path of the file that handle the id counter "user_index.txt"
    private String getUserIndexPath() {
        // return the path for the file that contain the id's
        return "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\user_index.txt";
    }

    // function to get the path of the directory that contains the users files
    private String getUsersPath() {
        return "C:\\Users\\wesam\\Desktop\\GA\\BankingWithJava\\db\\users";
    }

}
