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

        if(f.exists() && !f.isDirectory()) { 
            if (f.length() != 0 && SongPersistence.restoreFromFile() != null)
            {
                for (Song s : SongPersistence.restoreFromFile())
                {
                    songList.add(s);
                }
                sortSongList();
                listView.setItems(songList);
                songSelected = songList.get(0); //set first song as selected
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
   
        //initialize listener for tableview row selections to display in edit text fields
        listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            songSelected = newVal;
            if (newVal != null) {
                //update selected song
                songEdit.setText(newVal.getSong());
                artistEdit.setText(newVal.getArtist());
                albumEdit.setText(newVal.getAlbum());
                yearEdit.setText(Integer.toString(newVal.getYear()));
            }
            
        });
            
    }
    
    //Edit Button Method
    public void editButtonClicked()
    {   
        if(songSelected != null)
        {
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
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
                }
            } 
            if(songInList(songEdit.getText(), artistEdit.getText()) == true)
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
                } else {
                    // ... user chose CANCEL or closed the dialog
                    listView.refresh();
                    songEdit.setText(songSelected.getSong());
                    artistEdit.setText(songSelected.getArtist());
                    albumEdit.setText(songSelected.getAlbum());
                    yearEdit.setText(Integer.toString(songSelected.getYear()));
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
                    songEdit.clear();
                    artistEdit.clear();
                    albumEdit.clear();
                    yearEdit.clear();
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

    //Is song present? Method
    boolean songInList (String songTitle, String songArtist)
    {
        for(Song s : listView.getItems())
            if(s.getSong().equals(songTitle) && s.getArtist().equals(songArtist))
            {return true;}
        return false;
    }

    //Sort Method
    public void sortSongList() {
        songList.sort(Comparator.comparing(Song::getArtist, String.CASE_INSENSITIVE_ORDER)
                .thenComparing(Comparator.comparing(Song::getSong, String.CASE_INSENSITIVE_ORDER)));
    }

    //Add Button Method
    public void addButtonClicked() {
        if (!songAdd.getText().equals(null) && !artistAdd.getText().equals(null))
        {
            if(songInList(songAdd.getText(), artistAdd.getText()) == true)
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
                    listView.refresh();
                    songAdd.clear();
                    artistAdd.clear();
                    albumAdd.clear();
                    yearAdd.clear();
                }
            } else {
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
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Song title and artist are required.");
            alert.setContentText("Confirm or close.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // ... user chose OK
            } else {
                // ... user chose CANCEL or closed the dialog
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
                SongPersistence.clearFile();
                }   
            }     
        } 
    }
}

