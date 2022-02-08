package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
 
public class SongLib extends Application {

    @Override
	public void start(Stage primaryStage) throws Exception {
		
		// set up FXML loader
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/view/SongList.fxml"));
		
		// load the fxml
		AnchorPane root = (AnchorPane)loader.load();

		Scene scene = new Scene(root, 680, 500);
		primaryStage.setScene(scene);
        primaryStage.setResizable(false);
		primaryStage.show();
    }

    public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
    }
}