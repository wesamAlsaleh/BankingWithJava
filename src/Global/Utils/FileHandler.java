package Global.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FileHandler {
    private final Printer printer = new Printer();

    // function to perform a write operation on a file
    public void write(String fileName, String content, String fallbackMessage) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            // write the in the file
            writer.write(content);
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
                printer.printSuccessful("File Created successfully!");
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
    public boolean isExistsInTheDirectory(String directoryPath, String targetName) {
        // get the directory files
        var files = getDirectoryContentAsList(directoryPath);

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

    // function to delete a file
    public boolean delete(File file) {
        return file.delete();
    }

    // function to delete a line from a file with key:value structure
    public void overwrite(File file, String key, String value) {
        // temporary buffer to store the updated data without the one to delete
        StringBuilder tempBuffer = new StringBuilder();

        // try to read the file to exclude the targeted value
        try (Scanner scanner = new Scanner(file)) {
            // while there is data
            while (scanner.hasNextLine()) {
                // get the currency record line
                var line = scanner.nextLine();

                // if the line is empty skip to the next line
                if (line.isEmpty()) continue;

                // get the record parts
                List<String> parts = new ArrayList<>(List.of(line.split(",")));

                // iterate over the data in the record
                for (String part : parts) {
                    // if the key is the one to use
                    if (part.split(":")[0].contains(key)) {
                        // extract the value
                        var extractedValue = part.split(":")[1];

                        // if the provided code similar to the extracted code
                        if (extractedValue.equals(value)) {
                            // do not add it in the buffer
                            continue; // skip writing it
                        }

                        // add the value to the buffer
                        tempBuffer.append(line).append("\n"); // each record in a single line
                    }
                } // for loop end
            } // end of reading while
        } catch (FileNotFoundException e) {
            printer.printError("Cannot find or read the file!");
        } // end of reading catch

        // try to write the new data (overwrite the date in the file)
        writeWithoutAppending(
                file.getPath(),
                tempBuffer.toString(),
                "Failed to overwrite " + file.getName() + "!"
        );
    }

    // function to delete a line from a file with lines structure
    public void overwriteList(File file, String targetLine) {
        // temporary buffer to store the updated data without the one to delete
        StringBuilder tempBuffer = new StringBuilder();

        // try to read the file
        try (Scanner scanner = new Scanner(file)) {
            // while there is a line
            while (scanner.hasNextLine()) {
                // get the record line
                var line = scanner.nextLine();

                // if the line is empty skip to next one
                if (line.isEmpty()) continue;

                System.out.println(line.equals(targetLine));

                // if the line is the target skip it
                if (line.equals(targetLine)) continue;

                // add it to the buffer
                tempBuffer.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            printer.printError("Cannot find or read the file!");
        }

        System.out.println("Buffer: "  + tempBuffer.toString());

        // overwrite the file with the buffer data
        writeWithoutAppending(
                file.getPath(),
                tempBuffer.toString(),
                "Failed to overwrite " +  file.getName() + "!"
        );
    }
}
