package User;

import Global.Utils.DBPaths;
import Global.Utils.FileHandler;

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
    private final DBPaths dbPaths = new DBPaths();
    private final FileHandler fileHandler = new FileHandler();

    // function to get the next new user id
    public long generateUserId() {
        // path for the file that contain the id's
        var indexFilePath = dbPaths.getUserIndexPath();

        // Get the index file "user_index.txt" (make sure of the path in WIN/MACOS)
        File fileObject = new File(indexFilePath);

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
                try (FileWriter writer = new FileWriter(indexFilePath)) {
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
        var recordsPath = dbPaths.getUsersPath();

        // create new file to store the new user record
        try {
            // prepare the new file name to be created
            var fileName = "Customer-" + user.getFirstName() + "_" + user.getLastName() + "-" + user.getId() + ".txt";

            // prepare the file object to create the text (id.txt)
            File newFileObject = new File(recordsPath, fileName); // this will handle the location of the new file (rootPath + newFileName)

            // try to create the file
            if (newFileObject.createNewFile()) {
                // successful message
                System.out.println("User with the ID " + user.getId() + " created successfully!");
            } else {
                // if the file with the same name exits
                System.out.println("File already exists.");
            }

            // add the record to the created file
            try (FileWriter writer = new FileWriter(newFileObject.getPath())) {
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

    // function to get the user record as a file among the user files
    private File getUserRecordFile(Long id) {
        // get the user files
        var files = getDirectoryContentAsList(dbPaths.getUsersPath());

        // iterate over the files and get the user file by his id
        for (File file : files) {
            // if the name contains the user id
            if (file.getName().endsWith("-" + id + ".txt")) return file;
        }

        // if the user is not found return null
        return null;
    }

    // function to find user file by id
    public User getUserById(long id) {
        // get the user file by his id
        var userFile = getUserRecordFile(id);

        // return null if the user file is not found
        if (userFile == null) return null;

        // Read the Users file content
        try (Scanner scanner = new Scanner(userFile)) {
            // return the user object
            return getUserFromRecord(scanner.nextLine(), id);
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while reading the user file. Please try again.");
            System.out.println(e.getMessage());
        } // catch end

        // should not reach here
        return null;
    } // function end

    // function to find user by email
    public User getUserByEmail(String email) {
        // get the user files as array
        var userFiles = getDirectoryContentAsList(dbPaths.getUsersPath());

        // read each file until the email match the provided
        for (File file : userFiles) {
            // read the file using the scanner
            try (Scanner scanner = new Scanner(file)) {
                // if there is no content (safety check required by java)
                if (!scanner.hasNextLine()) {
                    // it should not be an empty file in the first place
//                    System.out.println("Skipping empty file...");

                    // keep searching next file
                    continue;
                }

                // read the record line
                var record = scanner.nextLine();

                // extract the data
                ArrayList<String> extractedParts = new ArrayList<>(List.of(record.split(",")));

                // id holder
                var id = 0L;

                // get the email property from the record line
                var emailFromRecord = extractedParts.get(2).substring(extractedParts.get(2).indexOf(':') + 1).trim();

                // if the provided email match the email in the record, get the full data
                if (email.equals(emailFromRecord)) {
                    // get the file name
                    var fileName = file.getName().replace(".txt", "");

                    // get the id from the file name (get what is after {+1} the last index of -)
                    id = Long.parseLong(fileName.substring(fileName.lastIndexOf("-") + 1));

                    // return as user object
                    return getUserFromRecord(record, id);
                }
            } catch (Exception e) {
                System.out.println("Something went wrong while reading the user file. Please try again.");
                throw new RuntimeException(e);
            } // catch end
        } // for loop end

        // should not reach here
        return null;
    }

    // function to get the list of files of a folder
    private File[] getDirectoryContentAsList(String sourcePath) {
        // create array of list of files from the file object of the provided path
        return Objects.requireNonNull(new File(sourcePath).listFiles()); // Throws NullPointerExceptio if null!
    }

    // function to extract user data from the record into array
    private List<String> extractUserData(String userRecord) {
        // split the user record line into parts and return it (ex: index0: First_Name:wesam and index1: ,Last_Name:muneer ...)
        return new ArrayList<>(List.of(userRecord.split(",")));
    }

    // function to extract user record from the text file
    public User getUserFromRecord(String userRecord, Long id) {
        // split the user record line into parts
        var parts = extractUserData(userRecord);

        // get the part from the array and then split it two part and take the right part (after the ':')
        var firstName = parts.get(0).split(":")[1].trim();
        var lastName = parts.get(1).split(":")[1].trim();
        var email = parts.get(2).split(":")[1].trim();
        var hashedPassword = parts.get(3).split(":")[1].trim();
        var role = parts.get(4).split(":")[1].trim();
        var fraudAttemptsCount = parts.get(5).split(":")[1].trim();

        // for the LocalDateTime, get the part from the array and then get the thing after the ':' (index of ':' + 1)
        var lockUntilString = parts.get(6).substring(parts.get(6).indexOf(':') + 1).trim();
        var createdDate = parts.get(7).substring(parts.get(7).indexOf(':') + 1).trim();
        var updatedDate = parts.get(8).substring(parts.get(8).indexOf(':') + 1).trim();

        // if the lock until is not null then cast the String to LocalDateTime, otherwise make it null
        LocalDateTime lockUntil =
                lockUntilString.equals("null") || lockUntilString.isEmpty()
                        ? null
                        : LocalDateTime.parse(lockUntilString);

        // return as User object
        return new User(
                id,
                firstName,
                lastName,
                email,
                hashedPassword,
                UserRole.valueOf(role),
                Integer.parseInt(fraudAttemptsCount),
                lockUntil,
                LocalDateTime.parse(createdDate),
                LocalDateTime.parse(updatedDate)
        );
    }

    // function to update fraud counter for a user
    public boolean increaseFraudAttemptsCounter(Long id) {
        // get the user record as a file
        var userFile = getUserRecordFile(id);

        // return if the user is not found (should not be activated since there is a validation on the parent)
        if (userFile == null) return false;

        // read the content of the file
        try (Scanner scanner = new Scanner(userFile)) {
            var originalRecord = scanner.nextLine();
            // extract the data from the record line
            var parts = extractUserData(originalRecord);

            // iterate over the parts
            for (int i = 0; i < parts.size(); i++) {
                // get the part of each iteration
                var part = parts.get(i);

                // if the part is the counter part
                if (part.contains("fraud_Attempts_Count")) {
                    // get the number after the ':'
                    var count = Integer.parseInt(part.split(":")[1].trim());

                    // increase the count
                    count = count + 1;

                    // replace the old count in the array
                    parts.set(i, "fraud_Attempts_Count: " + count);

                    // exit the for loop
                    break;
                }
            }

            // join the array again
            var newRecord = String.join(",", parts);

            // overwrite the line (false to append)
            try (FileWriter writer = new FileWriter(userFile, false)) {
                writer.write(newRecord);
            } catch (IOException e) {
                System.out.println("Error updating fraud counter: " + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Something went wrong while reading the user file. Please try again.");
            System.out.println(e.getMessage());
        } // catch end

        return true;
    }

}
