package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SongPersistence {
    static String directory = System.getProperty("user.dir");
    static String fileName = "library0.txt";
    static String absolutePath = directory + File.separator + "src/" + fileName;

    public static void addToFile(Song song)
    {
        // Write the content in file 
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, true));) {
            String songContent = song.getSong() + ", " + song.getArtist() + ", " + song.getAlbum() + ", " + song.getYear();
            writer.append("\n" + songContent);
            writer.close();
        } catch (IOException e) {
            // Cxception handling
            System.out.println(e);
        }
    }

    public static void writeToFile(Song song)
    {
        // Write the content in file 
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, true));) {
            String songContent = song.getSong() + ", " + song.getArtist() + ", " + song.getAlbum() + ", " + song.getYear();
            writer.append(songContent + "\n");
            writer.close();
        } catch (IOException e) {
            // Cxception handling
            System.out.println(e);
        }
    }

    public static void clearFile()
    {
      File mySongLib = new File(absolutePath); 
      mySongLib.delete();
      new File(absolutePath); 
    }

    public static List<Song> restoreFromFile()
    {
        try {
            File myFile = new File(absolutePath);
            Scanner myReader = new Scanner(myFile);
            List<Song> songList = new ArrayList<Song>();
            while (myReader.hasNextLine()) {
              String songStringData = myReader.nextLine();
              String[] a = songStringData.split(", ");
              Song songFromFile = new Song(a[0], a[1], a[2], Integer.parseInt(a[3]));
              songList.add(songFromFile);
            }
            myReader.close();
            return  songList;
          } catch (FileNotFoundException e) {
            System.out.println(e);
            e.printStackTrace();
          }
        return null;
    }
}
