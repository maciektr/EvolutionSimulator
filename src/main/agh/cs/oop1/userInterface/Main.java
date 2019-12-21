package agh.cs.oop1.userInterface;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
//import javafx.scene.Group;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.layout.GridPane;
//import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;




import java.awt.*;

public class Main extends Application {

    private void drawMap(){

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Text text1 = new Text("Number of epochs:");
        TextField textField1 = new TextField();
        Button button1 = new Button("Run");

        //Creating a Grid Pane
        GridPane gridPane = new GridPane();

        //Setting size for the pane
        gridPane.setMinSize(400, 200);

        //Setting the padding
        gridPane.setPadding(new Insets(10, 10, 10, 10));
        //Setting the vertical and horizontal gaps between the columns
        gridPane.setVgap(5);
        gridPane.setHgap(5);
        //Setting the Grid alignment
        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1, 0, 0);
        gridPane.add(textField1, 1, 0);
        gridPane.add(button1, 2, 0);

        Scene scene = new Scene(gridPane);
        primaryStage.setTitle("Evolution Simulator");
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
