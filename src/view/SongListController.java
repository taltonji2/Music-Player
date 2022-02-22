//by Scott Skibin, Tim Altonji
package view;

import java.io.File;
import java.util.Comparator;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;


public class SongListController {

    //Buttons
    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    //Add Text Fields
    @FXML
    private TextField songAdd;

    @FXML
    private TextField artistAdd;

    @FXML
    private TextField albumAdd;
    
    @FXML
    private TextField yearAdd;

    //Edit Text Fields
    @FXML
    private TextField songEdit;

    @FXML
    private TextField artistEdit;

    @FXML
    private TextField albumEdit;
    
    @FXML
    private TextField yearEdit;

    //List View
    @FXML
    private ListView<Song> listView;
    
    //Declare observable list
    private ObservableList<Song> songList;
    private Song songSelected;

    
    public void start(Stage mainStage){
        songList = FXCollections.observableArrayList();
        String directory = System.getProperty("user.dir");
        String fileName = "library0.txt";
        String absolutePath = directory + File.separator + "src/" + fileName;
        File f = new File(absolutePath);

        //initialize listener for listview row selections to display in edit text fields
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            songSelected = newVal;
            if (newVal != null) {
                //update selected song
                songEdit.setText(newVal.getSong());
                artistEdit.setText(newVal.getArtist());
                albumEdit.setText(newVal.getAlbum());
                yearEdit.setText(Integer.toString(newVal.getYear()));
                if (newVal.getYear() == 0){
                    yearEdit.clear();
                }
            }
            
        });

        if(f.exists() && !f.isDirectory()) { 
            if (f.length() != 0 && SongPersistence.restoreFromFile() != null)
            {
                for (Song s : SongPersistence.restoreFromFile())
                {
                    songList.add(s);
                }
                sortSongList();
                listView.setItems(songList);
                songSelected = songList.get(0); //set first song as selected **DO WE NEED THIS?**
                listView.getSelectionModel().select(0);
            }
        } else
        {
            SongPersistence.createFile();
        }

        //Button events
        addButton.setOnAction(e -> addButtonClicked());
        deleteButton.setOnAction(e -> deleteButtonClicked());
        editButton.setOnAction(e -> editButtonClicked());

        //display song/artist in listview
        listView.setCellFactory(param -> new ListCell<Song>() {
            @Override
            protected void updateItem(Song s, boolean empty){
            super.updateItem(s, empty);
                if(empty || s == null || s.getSong() == null){
                    setText("");
                }
                else {
                    setText(s.getSong()+"/"+s.getArtist());
                }             
            }
        });         
    }
    
    //Is song present? Method
    boolean boolSongInList (String songTitle, String songArtist){
        for(Song s : listView.getItems())
            if(s.getSong().equals(songTitle) && s.getArtist().equals(songArtist))
            {return true;}
        return false;
    }
    Song songInList (String songTitle, String songArtist){
        for(Song s : listView.getItems())
            if(s.getSong().equals(songTitle) && s.getArtist().equals(songArtist))
            {return s;}
        return null;
    }
    //checks if string in TextField input is an integer
    boolean isInt(TextField tf){ 
        if (tf.getText().isEmpty()){
            System.out.println("if");
            return true;
            
        }
        else {
            System.out.println("else");
            try{ 
                Integer.parseInt(tf.getText()); 
                return true; 
            } 
            catch (NumberFormatException e){  
                return false; 
            } 
        }
    }

    //Sort Method
    public void sortSongList() {
        songList.sort(Comparator.comparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Comparator.comparing(Song::getSong, String.CASE_INSENSITIVE_ORDER)));
    }

    //Edit Button Method
    public void editButtonClicked()
    {   
        if(songSelected != null) // **DO WE NEED THIS**
        {
            Song songIL = songInList(songEdit.getText(), artistEdit.getText());
            boolean inList = boolSongInList(songEdit.getText(), artistEdit.getText());
            if(songEdit.getText().isEmpty() || artistEdit.getText().isEmpty())
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Song title and artist are required.");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                }
            } 
            
            else if((inList) && (songIL.getSong() != songSelected.getSong() && songIL.getArtist() != songSelected.getArtist()))
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Song already present");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                }
            }
            else if(!(isInt(yearEdit))){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Invalid Year input");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                    if (songSelected.getYear() == 0){
                        yearEdit.clear();
                    }
                }
            }
            else {
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Do you wish to edit this song?");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    songSelected.setSong(songEdit.getText());
                    songSelected.setArtist(artistEdit.getText());
                    if(!albumEdit.getText().isEmpty())
                        songSelected.setAlbum(albumEdit.getText());
                    if(!yearEdit.getText().isEmpty())
                        songSelected.setYear(Integer.parseInt(yearEdit.getText()));

                    sortSongList();
                    SongPersistence.clearFile();
                    String songListString = "";
                    for (Song s : songList)
                    {
                        songListString += s.getSong() + ", " + s.getArtist() + ", " + s.getAlbum() + ", " + s.getYear() + "\n"; 
                        SongPersistence.writeToFile(songListString);
                    }
                    listView.refresh();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                }
            }
        }
    }

    //Add Button Method
    public void addButtonClicked() {
        if (!songAdd.getText().equals(null) && !artistAdd.getText().equals(null)) // ** DO WE NEED THIS**
        {   
            if(songAdd.getText().isEmpty() || artistAdd.getText().isEmpty())
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Song title and artist are required.");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                }
            } 
            else if(boolSongInList(songAdd.getText(), artistAdd.getText()) == true)
            {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Song already present");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh(); //  **IS THIS refresh neccessary?**
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                }
            } 
            else if(!(isInt(yearAdd))){
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Invalid Year input");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    listView.refresh();
                    yearAdd.clear();
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    yearAdd.clear();
                }
            }
            else {
                //Adding the song
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Do you wish to add this song?");
                alert.setContentText("Confirm or close.");
                
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    Song newSong = new Song();
                    newSong.setSong(songAdd.getText());
                    newSong.setArtist(artistAdd.getText());
                    newSong.setAlbum(albumAdd.getText());
                    if(!yearAdd.getText().isEmpty())
                        newSong.setYear(Integer.parseInt(yearAdd.getText()));
                    listView.getItems().add(newSong);
                    sortSongList();
                    SongPersistence.clearFile();
                    String songListString = "";
                    for (Song s : listView.getItems())
                    {
                        songListString += s.getSong() + ", " + s.getArtist() + ", " + s.getAlbum() + ", " + s.getYear() + "\n"; 
                        SongPersistence.writeToFile(songListString);
                    }
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                } else {
                        // ... user chose CANCEL or closed the dialog
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                } 
            }
        } 
    }

    //Delete deletes two songs
    //Delete Button Method
    public void deleteButtonClicked() {
        if(songSelected != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you wish to delete this song?");
            alert.setContentText("Confirm or close.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                boolean firstSongInList = listView.getSelectionModel().isSelected(0);
                if (firstSongInList) 
                {
                    listView.getItems().remove(songSelected);
                    SongPersistence.clearFile();
                    String songListString = "";
                    for (Song s : listView.getItems())
                    {
                        songListString += s.getSong() + ", " + s.getArtist() + ", " + s.getAlbum() + ", " + s.getYear() + "\n"; 
                        SongPersistence.writeToFile(songListString);
                    }
                } else {
                    listView.getItems().remove(songSelected);
                    listView.getSelectionModel().selectNext();
                    SongPersistence.clearFile();
                    String songListString = "";
                    for (Song s : songList)
                    {
                    songListString += s.getSong() + ", " + s.getArtist() + ", " + s.getAlbum() + ", " + s.getYear() + "\n"; 
                    SongPersistence.writeToFile(songListString);
                    }        
                }
                if(listView.getItems().isEmpty())
                {
                    songEdit.clear();
                    artistEdit.clear();
                    albumEdit.clear();
                    yearEdit.clear();
                    SongPersistence.clearFile();
                }   
            }     
        } 
    }
}

