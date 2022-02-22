//by Scott Skibin, Tim Altonji
package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SongPersistence {
    static String directory = System.getProperty("user.dir");
    static String fileName = "library0.txt";
    static String absolutePath = directory + File.separator + "src/view/" + fileName;

    public static void writeToFile(String songListString)
    {
        // Write the content in file 
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, false));) {
            writer.append(songListString);
            writer.close();
        } catch (IOException e) {
            // Exception handling
            System.out.println(e);
        }
    }

    public static void clearFile()
    {
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath, false));) {
        String songContent = "";
        writer.append(songContent + "\n");
        writer.close();
    } catch (IOException e) {
        // Exception handling
        System.out.println(e);
    }
    }

    public static List<Song> restoreFromFile()
    {
      List<Song> songList = new ArrayList<Song>();
      try { 
          File f = new File(absolutePath);
          if(f.exists() && !f.isDirectory()) 
          { 
            File myFile = new File(absolutePath);
            Scanner myReader = new Scanner(myFile);
          
            if(myFile.length() == 0)
            {
              songList = null;
              myReader.close();
              return songList;
            }
            while (myReader.hasNextLine()) {
              String songStringData = myReader.nextLine();
              if (songStringData.equals(""))
              {
                myReader.close();
                songList = null;
                return songList;
              }
              String[] a = songStringData.split(", ");
              Song songFromFile = new Song(a[0], a[1], a[2], Integer.parseInt(a[3]));
              songList.add(songFromFile);
            }
            myReader.close();
            return  songList;
          } 
        } catch (FileNotFoundException e) {
         
          songList = new ArrayList<Song>();
          songList = null;
          return songList;
        }
      return songList;
    }

    public static void createFile ()
    {
      File f = new File(absolutePath);
      f.getParentFile().mkdirs(); 
      try {
        f.createNewFile();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
}
