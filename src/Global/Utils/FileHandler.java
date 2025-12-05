package Global.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class FileHandler {
    private final Printer printer = new Printer();

    // function to perform a write operation on a file
    public void write(String fileName, String content, String fallbackMessage) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            // write the in the file
            writer.write(content);

            // operation done successfully
        } catch (IOException e) {
            printer.printError(fallbackMessage);
            throw new RuntimeException(e);
        }
    }

    // function to perform a write operation on a file
    public void writeWithoutAppending(String fileName, String content, String fallbackMessage) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            // write the in the file
            writer.write(content);
        } catch (IOException e) {
            printer.printError(fallbackMessage);
            throw new RuntimeException(e);
        }
    }

    // function to create a file
    public void createFile(File file, String fallbackMessage) {
        // try to create the file
        try {
            if (file.createNewFile()) {
                printer.printSuccessful("File created");
            } else {
                // if the file with the same name exits
                printer.printError("File already exists.");
            }
        } catch (IOException e) {
            printer.printError(fallbackMessage);
            throw new RuntimeException(e);
        }
    }

    // function to get the list of files of a folder
    public File[] getDirectoryContentAsList(String sourcePath) {
        // create array of list of files from the file object of the provided path
        return Objects.requireNonNull(new File(sourcePath).listFiles()); // Throws NullPointerExceptio if null!
    }

    // function to check if target is available in the directory
    public boolean isExistsInTheDirectory(String sourcePath, String targetName) {
        // get the directory files
        var files = getDirectoryContentAsList(sourcePath);

        // if there is a file with similar to the target name return true
        for (File file : files) {
            // if the file name equals the target return true
            if (file.getName().equals(targetName)) {
                return true;
            }
        }

        // if not found return false
        return false;
    }

    // function to read a file and check if the target exist in it
    public boolean isExistInTheFile(File file, String targetContent) {
        // try to read the file
        try (Scanner scanner = new Scanner(file)) {
            // read while there is a lines
            while (scanner.hasNextLine()) {
                // get the line
                String line = scanner.nextLine();

                // if the line contains the exits return
                if (line.contains(targetContent)) return true;
            }
        } catch (FileNotFoundException e) {
            printer.printError("File not found!");
        }

        // if not available return false
        return false;
    }
}
