package view;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Cell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;



public class SongListController implements Initializable{

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

    //Table Cols
    @FXML
    private TableView<Song> tableView;

    @FXML
    private TableColumn<Song, String> song;

    @FXML
    private TableColumn<Song, String> artist;

    @FXML
    private TableColumn<Song, String> album;

    @FXML
    private TableColumn<Song, Integer> year;
    
    //Declare observable list
    private ObservableList<Song> songList;
    private Song songSelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        Song default1 = new Song("Default Song1", "Default Artist1", "Default Album1", 2001);
        Song default2 = new Song("Default Song2", "Default Artist2", "Default Album2", 2002);
        Song default3 = new Song("Default Song3", "Default Artist3", "Default Album3", 2003);
        Song default4 = new Song("Default Song4", "Default Artist4", "Default Album4", 2004);
        Song default5 = new Song("Default Song5", "Default Artist5", "Default Album5", 2005);
        Song default6 = new Song("Default Song6", "Default Artist6", "Default Album6", 2006);
        
        songList = FXCollections.observableArrayList(default1, default2, default3, default4, default5, default6);
    
        tableView.setItems(songList);
        songSelected = default1; //set first song as selected
        tableView.getSelectionModel().select(0);

        //Button events
        addButton.setOnAction(e -> addButtonClicked());
        deleteButton.setOnAction(e -> deleteButtonClicked());
        editButton.setOnAction(e -> editButtonClicked());

        //Table events
        song.setCellValueFactory(new PropertyValueFactory<Song, String>("song"));
        song.setCellFactory(TextFieldTableCell.forTableColumn());
        
        artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        artist.setCellFactory(TextFieldTableCell.forTableColumn());

        
        //initialize listener for tableview row selections to display in edit text fields
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            songSelected = newVal;
            yearEdit.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, 
                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        yearEdit.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
            yearAdd.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, 
                    String newValue) {
                    if (!newValue.matches("\\d*")) {
                        yearAdd.setText(newValue.replaceAll("[^\\d]", ""));
                    }
                }
            });
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
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you wish to edit this song?");
            alert.setContentText("Confirm or close.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                songSelected.setSong(songEdit.getText());
                songSelected.setAlbum(albumEdit.getText());
                songSelected.setArtist(artistEdit.getText());
                songSelected.setYear(Integer.parseInt(yearEdit.getText()));
                songEdit.clear();
                artistEdit.clear();
                albumEdit.clear();
                yearEdit.clear();
                tableView.refresh();
            }else {
                // ... user chose CANCEL or closed the dialog
            songAdd.clear();
            artistAdd.clear();
            albumAdd.clear();
            yearAdd.clear();
            }
        }
    }

    //Add Button Method
    public void addButtonClicked() {
        try{
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
                newSong.setYear(Integer.parseInt(yearAdd.getText()));
                tableView.getItems().add(newSong);
                songAdd.clear();
                artistAdd.clear();
                albumAdd.clear();
                yearAdd.clear();
            }else {
                    // ... user chose CANCEL or closed the dialog
                songAdd.clear();
                artistAdd.clear();
                albumAdd.clear();
                yearAdd.clear();
            }
            
        } catch (Exception e)
        {
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
    
    //Delete Button Method
    public void deleteButtonClicked() {
        //boolean firstSongInList = tableView.getSelectionModel().isSelected(0);
        if(songSelected != null)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you wish to delete this song?");
            alert.setContentText("Confirm or close.");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                songList.remove(songSelected);
                //songSelected.forEach(songList::remove);
            }
        } 

        // else {
        //     songSelected.forEach(songList::remove);
        // tableView.getSelectionModel().selectNext();
        // }
    }

    
}

