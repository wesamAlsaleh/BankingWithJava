package Global.Utils;

import java.io.FileWriter;
import java.io.IOException;

public class FileHandler {
    // function to perform a write operation on a file
    public void write(FileWriter fileWriter, String content, String message) throws IOException {
        try (fileWriter) {
            // write the in the file
            fileWriter.write(content);
        } catch (IOException e) {
            System.out.println(message);
            System.out.println(e.getMessage());
        }
    }

    // function to create a file
    public void createFile(){}
}
