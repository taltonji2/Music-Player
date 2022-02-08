package view;


import javafx.event.EventHandler;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

public class SongListController implements Initializable{

    //Buttons
    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    //Text Fields
    @FXML
    private TextField songText;

    @FXML
    private TextField artistText;

    @FXML
    private TextField albumText;
    
    @FXML
    private TextField yearText;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Button events
        addButton.setOnAction(e -> addButtonClicked());
        deleteButton.setOnAction(e -> deleteButtonClicked());

        //Table events
        song.setCellValueFactory(new PropertyValueFactory<Song, String>("song"));
        song.setCellFactory(TextFieldTableCell.forTableColumn());
        song.setOnEditCommit(new EventHandler<CellEditEvent<Song, String>>() {
            @Override
            public void handle(CellEditEvent<Song, String> event) {
                Song song = event.getRowValue();
                song.setSong(event.getNewValue());
            }
        });
        artist.setCellValueFactory(new PropertyValueFactory<Song, String>("artist"));
        artist.setCellFactory(TextFieldTableCell.forTableColumn());
        artist.setOnEditCommit(new EventHandler<CellEditEvent<Song, String>>() {
            @Override
            public void handle(CellEditEvent<Song, String> event) {
                Song song = event.getRowValue();
                song.setArtist(event.getNewValue());
            }
        });
        album.setCellValueFactory(new PropertyValueFactory<Song, String>("album"));
        album.setCellFactory(TextFieldTableCell.forTableColumn());
        album.setOnEditCommit(new EventHandler<CellEditEvent<Song, String>>() {
            @Override
            public void handle(CellEditEvent<Song, String> event) {
                Song song = event.getRowValue();
                song.setAlbum(event.getNewValue());
            }
        });
        year.setCellValueFactory(new PropertyValueFactory<Song, Integer>("year"));
        year.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        year.setOnEditCommit(new EventHandler<CellEditEvent<Song, Integer>>() {
            @Override
            public void handle(CellEditEvent<Song, Integer> event) {
                Song song = event.getRowValue();
                song.setYear(event.getNewValue());
            }
        });

        //set default song and select first list item at start
        tableView.getItems().add(new Song("Default1", "Default1", "Default1", 2001));
        tableView.getItems().add(new Song("Default2", "Default2", "Default2", 2002));
        tableView.getItems().add(new Song("Default3", "Default3", "Default3", 2003));
        tableView.getItems().add(new Song("Default4", "Default4", "Default4", 2004));
        tableView.getItems().add(new Song("Default5", "Default5", "Default5", 2005));
        tableView.getSelectionModel().select(0);
    }
    //Add Button Method
    public void addButtonClicked() {
        Song newSong = new Song();
        newSong.setSong(songText.getText());
        newSong.setArtist(artistText.getText());
        newSong.setAlbum(albumText.getText());
        newSong.setYear(Integer.parseInt(yearText.getText()));
        tableView.getItems().add(newSong);
        songText.clear();
        artistText.clear();
        albumText.clear();
        yearText.clear();

    }
    //Delete Button Method
    public void deleteButtonClicked() {

        ObservableList<Song> songSelected, allSongs;
        allSongs = tableView.getItems();
        songSelected = tableView.getSelectionModel().getSelectedItems();

        boolean firstSongInList = tableView.getSelectionModel().isSelected(0);
        if (firstSongInList == true) {
            songSelected.forEach(allSongs::remove);
        }
        else {
            songSelected.forEach(allSongs::remove);

        
        tableView.getSelectionModel().selectNext();
        }

    }

}

