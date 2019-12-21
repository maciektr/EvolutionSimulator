package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.ArrayList;

//import java.awt.*;

public class Main extends Application {
    private static final String parametersPath = "parameters.json";
    private int windowWidth = 851;
    private int windowHeight = 555;
    private Simulation simulation;

    private int cellX;
    private int cellY;
    private int circleR;

    private static Rectangle setRectangleColor(Rectangle rectangle, MapEnumerator enumerator, int c, int r){
        MapCell cell = enumerator.getMapCellByIndex(c,r);
        rectangle.setFill(Color.LAWNGREEN);
        if (enumerator.indexesInJungle(c, r))
            rectangle.setFill(Color.FORESTGREEN);

        if (cell != null && cell.isPlantSet())
            rectangle.setFill(Color.DARKGREEN);

        return rectangle;
    }

    private Circle makeCircle(){
        return new Circle(this.circleR, Color.ORANGE);
    }

    private void drawMap(GridPane pane){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        for(int c = 0; c<enumerator.numberOfColumns(); c++)
            for(int r = 0; r<enumerator.numberOfRows(); r++){
                int x = this.cellX;
                int y = this.cellY;
                MapCell cell = enumerator.getMapCellByIndex(c,r);

                Rectangle rectangle = Main.setRectangleColor(new Rectangle(x,y), enumerator,c,r);
                pane.add(rectangle, c, r);
                if (cell != null && cell.anyAnimals())
                    pane.add(new Circle(this.circleR, Color.ORANGE),c,r);

            }
    }

    private void refreshMap(GridPane pane){
        MapEnumerator enumerator = new MapEnumerator(this.simulation.getMap());
        ArrayList<Node> removalCandidates = new ArrayList<>();
        ArrayList<Node> addCandidate = new ArrayList<>();

        for(Node node : pane.getChildren()){
            if(node == null)
                continue;
            int c = GridPane.getColumnIndex(node);
            int r = GridPane.getRowIndex(node);
            if(node instanceof Rectangle){
                removalCandidates.add(node);
                addCandidate.add(Main.setRectangleColor((Rectangle)node, enumerator, c,r));

            }else if(node instanceof Circle){
                removalCandidates.add(node);
            }
        }
        pane.getChildren().removeAll(removalCandidates);
        pane.getChildren().addAll(addCandidate);
        for(Animal animal : this.simulation.getMap().getAnimalsList()){
            pane.add(this.makeCircle(), enumerator.getColumn(animal.getPosition()),enumerator.getRow(animal.getPosition()));
        }


    }



    @Override
    public void start(Stage primaryStage) throws Exception{
//        Setting size for the pane
//        gridPane.setMinSize(400, 200);
//        Setting the padding
//        gridPane.setPadding(new Insets(10, 10, 10, 10));
//        Setting the vertical and horizontal gaps between the columns
//        gridPane.setVgap(5);
//        gridPane.setHgap(5);
//        Setting the Grid alignment
//        gridPane.setAlignment(Pos.CENTER);
        try {
            final Configuration config = Configuration.fromJson(Main.parametersPath);
            this.simulation = new Simulation(config);

            GridPane buttonsPane = new GridPane();

            Text text1 = new Text("Number of epochs:");
            TextField textField1 = new TextField();
            Button button1 = new Button("Run");
            Button button2 = new Button("Next epoch");


            buttonsPane.add(text1, 0, 0);
            buttonsPane.add(textField1, 1, 0);
            buttonsPane.add(button1, 2, 0);
            buttonsPane.add(button2, 3, 0);
            buttonsPane.setAlignment(Pos.TOP_CENTER);
            buttonsPane.setPadding(new Insets(10, 10, 0, 10));
            buttonsPane.setHgap(10);

            GridPane mapPane = new GridPane();
            mapPane.setPadding(new Insets(5, 5, 5, 5));
            mapPane.setAlignment(Pos.BOTTOM_CENTER);
//        mapPane.setVgap(1);
//        mapPane.setHgap(1);
            MapEnumerator enumerator = new MapEnumerator(this.simulation.getMap());
            this.cellX = this.windowWidth/enumerator.numberOfColumns();
            this.cellY = this.windowHeight/enumerator.numberOfRows();
            this.circleR = Math.min(this.cellX,this.cellY)/2;

            this.drawMap(mapPane);
            button2.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
//                    button2.setText("Accepted");
                    try {
                        simulation.nextEpoch();
                        refreshMap(mapPane);
                    } catch (IllegalAccessException ex) {
                        System.out.println("CATCHED EXCEP");
                        ex.printStackTrace();
                    }
                }
            });

            VBox rootPane = new VBox();
            rootPane.getChildren().addAll(buttonsPane, mapPane);
            VBox.setVgrow(mapPane, Priority.ALWAYS);


            Scene scene = new Scene(rootPane);
            primaryStage.setTitle("Evolution Simulator");
            primaryStage.setScene(scene);
//            primaryStage.setResizable(false);
            primaryStage.sizeToScene();

            primaryStage.show();
//            System.out.println(primaryStage.resizableProperty());
//            System.out.println(primaryStage.getWidth() + "x" + primaryStage.getHeight());
        }catch(FileNotFoundException ex){
            System.out.println("Application cannot be launched!");
        }catch (IllegalArgumentException ex){
            System.out.println("Illegal configuration! Check parameters.json file.\n"+ex.toString());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
