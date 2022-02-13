package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SongPersistence {
    String directory = System.getProperty("user.dir");
    String fileName = "songlib0.txt";
    String absolutePath = directory + File.separator + fileName;

    public void writeToFile()
    {
        // Write the content in file 
        try(FileWriter fileWriter = new FileWriter(absolutePath)) {
            String fileContent = "Hello";
            fileWriter.write(fileContent);
            fileWriter.close();
        } catch (IOException e1) {
            // Cxception handling
        }
        
    }

    public void restoreFromFile()
    {
        // Read the content from file
        try(FileReader fileReader = new FileReader(absolutePath)) {
            int ch = fileReader.read();
            while(ch != -1) {
                System.out.print((char)ch);
                fileReader.close();
            }
        } catch (FileNotFoundException e) {
            // Exception handling
        } catch (IOException e2) {
            // Exception handling
        }
    }
}
