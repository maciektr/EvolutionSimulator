package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.Animal;
import agh.cs.oop1.simulation.MapCell;
import agh.cs.oop1.simulation.MapEnumerator;
import agh.cs.oop1.simulation.Simulation;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationStage extends Stage {
    private Simulation simulation;
    private int windowWidth = 851;
    private int windowHeight = 555;
    private int cellWidth;
    private int cellHeight;
    private int circleRadius;

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
        return new Circle(this.circleRadius, Color.ORANGE);
    }

    private void drawMap(GridPane pane){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        for(int c = 0; c<enumerator.numberOfColumns(); c++)
            for(int r = 0; r<enumerator.numberOfRows(); r++){
                int x = this.cellWidth;
                int y = this.cellHeight;
                MapCell cell = enumerator.getMapCellByIndex(c,r);

                Rectangle rectangle = SimulationStage.setRectangleColor(new Rectangle(x,y), enumerator,c,r);
                pane.add(rectangle, c, r);
                if (cell != null && cell.anyAnimals())
                    pane.add(new Circle(this.circleRadius, Color.ORANGE),c,r);

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
                addCandidate.add(SimulationStage.setRectangleColor((Rectangle)node, enumerator, c,r));

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

    private void nextEpoch() throws IllegalAccessException {
        simulation.nextEpoch();
        refreshMap(this.mapPane);

    }

    private GridPane mapPane;
    private GridPane buttonsPane;

    SimulationStage(Simulation simulation){
        this.simulation = simulation;
        this.buttonsPane = new GridPane();

        Text text1 = new Text("Number of epochs:");
        TextField numberOfEpochsField = new TextField();
        numberOfEpochsField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    numberOfEpochsField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        Button button1 = new Button("Run");
        Button button2 = new Button("Next epoch");


        buttonsPane.add(text1, 0, 0);
        buttonsPane.add(numberOfEpochsField, 1, 0);
        buttonsPane.add(button1, 2, 0);
        buttonsPane.add(button2, 3, 0);
        buttonsPane.setAlignment(Pos.TOP_CENTER);
        buttonsPane.setPadding(new Insets(10, 10, 0, 10));
        buttonsPane.setHgap(10);

        this.mapPane = new GridPane();
        mapPane.setPadding(new Insets(5, 5, 5, 5));
        mapPane.setAlignment(Pos.BOTTOM_CENTER);
        MapEnumerator enumerator = new MapEnumerator(this.simulation.getMap());
        this.cellWidth = this.windowWidth/enumerator.numberOfColumns();
        this.cellHeight = this.windowHeight/enumerator.numberOfRows();
        this.circleRadius = Math.min(this.cellWidth,this.cellHeight)/2;

        Label epochsCount = new Label("0");

        this.drawMap(mapPane);
        Label theMostPopularGenotype = new Label(this.simulation.getTheMostPopularGenotype().toString());

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    nextEpoch();
                    epochsCount.setText(Integer.toString(simulation.getEpoch()));
                    theMostPopularGenotype.setText(simulation.getTheMostPopularGenotype().toString());
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(numberOfEpochsField.getText().equals(""))
                    return;

                int epochs = Integer.parseInt(numberOfEpochsField.getText());
                Timer timer = new Timer();
                TimerTask task = new TimerTask(){
                    int counter = 0;
                    @Override
                    public void run() {
                        if(epochs==++counter){
                            timer.cancel();
                            timer.purge();
                        }
                        Platform.runLater(()-> {
                            try {
                                if(simulation.getNumberOfAnimals() == 0)
                                    return;
                                nextEpoch();
                                if(simulation.getNumberOfAnimals() == 0)
                                    return;
                                epochsCount.setText(Integer.toString(simulation.getEpoch()));
                                theMostPopularGenotype.setText(simulation.getTheMostPopularGenotype().toString());
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                };
                timer.schedule(task, 0, 40l);
            }
        });


        GridPane statisticsPane = new GridPane();
        statisticsPane.setPadding(new Insets(5,5,5,5));
        statisticsPane.setAlignment(Pos.BOTTOM_CENTER);
        statisticsPane.add(new Label("Epoch number: "),0,0);
        statisticsPane.add(epochsCount,1,0);
        statisticsPane.add(new Label("The most popular genotype: "),0,1);
        statisticsPane.add(theMostPopularGenotype,1,1);

        VBox rootPane = new VBox();
        rootPane.getChildren().addAll(buttonsPane, mapPane, statisticsPane);
        VBox.setVgrow(mapPane, Priority.ALWAYS);

        Scene scene = new Scene(rootPane);
        this.setTitle("Evolution Simulator");
        this.setScene(scene);
//            primaryStage.setResizable(false);
        this.sizeToScene();

        this.show();

    }
}
