
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LaunchGame extends Application {
	@Override
	public void start(Stage primaryStage) {
		BoardPane boardPane = new BoardPane();
		boardPane.loadLevel();
		
		Scene scene = new Scene(boardPane, 300, 335);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Game Board");
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}
		
	public static void main(String[] args) {
		launch(args);
	}
}
