package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import com.sun.org.apache.bcel.internal.generic.LADD;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sun.awt.im.InputMethodAdapter;
import sun.security.krb5.Config;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SimulationStage extends Stage {
//    private Simulation simulation;
    private int windowWidth = 851;
    private int windowHeight = 555;
    private int cellWidth;
    private int cellHeight;
    private int circleRadius;

    private static Color setRectangleColor(MapEnumerator enumerator, int c, int r){
        MapCell cell = enumerator.getMapCellByIndex(c,r);
        Color res = (Color.LAWNGREEN);
        if (enumerator.indexesInJungle(c, r))
            res=  (Color.FORESTGREEN);

        if (cell != null && cell.isPlantSet())
            res = (Color.DARKGREEN);

        return res;
    }

    private Circle makeCircle(){
        return new Circle(this.circleRadius, Color.ORANGE);
    }

    private void drawMap(GridPane pane, Simulation simulation){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        for(int c = 0; c<enumerator.numberOfColumns(); c++)
            for(int r = 0; r<enumerator.numberOfRows(); r++){
                int x = this.cellWidth;
                int y = this.cellHeight;
                MapCell cell = enumerator.getMapCellByIndex(c,r);

                Rectangle rectangle = (new Rectangle(x,y));
                rectangle.setFill(SimulationStage.setRectangleColor(enumerator,c,r));
                pane.add(rectangle, c, r);
                if (cell != null && cell.anyAnimals())
                    pane.add(new Circle(this.circleRadius, Color.ORANGE),c,r);

            }
    }

    private void refreshMap(GridPane pane, Simulation simulation){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        ArrayList<Node> removalCandidates = new ArrayList<>();
        ArrayList<Node> addCandidate = new ArrayList<>();

        for(Node node : pane.getChildren()){
            if(node == null)
                continue;
            int c = GridPane.getColumnIndex(node);
            int r = GridPane.getRowIndex(node);
            if(node instanceof Rectangle){
                Color color = SimulationStage.setRectangleColor(enumerator, c,r);
                if(((Rectangle)node).getFill() != color){
                    removalCandidates.add(node);
                    ((Rectangle) node).setFill(color);
                    addCandidate.add(node);
                }
            }else if(node instanceof Circle){
                removalCandidates.add(node);
            }
        }
        pane.getChildren().removeAll(removalCandidates);
        pane.getChildren().addAll(addCandidate);
        for(Animal animal : simulation.getMap().getAnimalsList()){
            pane.add(this.makeCircle(), enumerator.getColumn(animal.getPosition()),enumerator.getRow(animal.getPosition()));
        }
    }

    private void nextEpoch(GridPane pane, Simulation simulation) throws IllegalAccessException {
        simulation.nextEpoch();
        refreshMap(pane, simulation);

    }

    private GridPane createMapPane(Simulation simulation){
        GridPane mapPane = new GridPane();
        mapPane.setPadding(new Insets(5, 5, 5, 5));
        mapPane.setAlignment(Pos.BOTTOM_CENTER);
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        this.cellWidth = this.windowWidth/enumerator.numberOfColumns();
        this.cellHeight = this.windowHeight/enumerator.numberOfRows();
        this.circleRadius = Math.min(this.cellWidth,this.cellHeight)/2;
        this.drawMap(mapPane, simulation);
        return mapPane;
    }

    private GridPane createStatisticsPane(Label epochsCount,
                                          Label theMostPopularGenotype,
                                          Label animalsCount,
                                          Label numberOfPlants){
        GridPane statisticsPane = new GridPane();
        statisticsPane.setPadding(new Insets(5,5,5,5));
        statisticsPane.setAlignment(Pos.BOTTOM_CENTER);
        statisticsPane.add(new Label("Epoch number: "),0,0);
        statisticsPane.add(epochsCount,1,0);
        statisticsPane.add(new Label("Number of animals: "),0,1);
        statisticsPane.add(animalsCount,1,1);
        statisticsPane.add(new Label("Number of plants: "),0,2);
        statisticsPane.add(numberOfPlants, 1,2);
        statisticsPane.add(new Label("The most popular genotype: "),0,3);
        statisticsPane.add(theMostPopularGenotype,1,3);

        return statisticsPane;
    }

    SimulationStage(Configuration config){
//        Left Map Pane
        Simulation leftSimulation = new Simulation(config);
        GridPane leftMapPane = this.createMapPane(leftSimulation);
        Simulation rightSimulation = config.dualMode ? new Simulation(config) : null;
        GridPane rightMapPane =config.dualMode ? this.createMapPane(rightSimulation) : null;

//        Statistics Pane
        Label leftEpochsCount = new Label("0");
        Label leftAnimalsCount = new Label("0");
        Label leftNumberOfPlants = new Label("0");
        Label leftTheMostPopularGenotype = new Label(leftSimulation.getTheMostPopularGenotype().toString());
        GridPane leftStatisticsPane = this.createStatisticsPane(leftEpochsCount,leftTheMostPopularGenotype, leftAnimalsCount, leftNumberOfPlants);

        Label rightEpochsCount = config.dualMode ? new Label("0") : null;
        Label rightAnimalsCount = config.dualMode ? new Label("0") : null;
        Label rightNumberOfPlants = config.dualMode ? new Label("0") : null;
        Label rightTheMostPopularGenotype =config.dualMode ?new Label(leftSimulation.getTheMostPopularGenotype().toString()): null;
        GridPane rightStatisticsPane =config.dualMode ?this.createStatisticsPane(rightEpochsCount,rightTheMostPopularGenotype, rightAnimalsCount, rightNumberOfPlants): null;


//        ButtonsPane
        GridPane buttonsPane = new GridPane();
        Text text1 = new Text("Number of epochs:");
        TextField numberOfEpochsField = new TextField();
//        Ensure only numeric input
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

        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    nextEpoch(leftMapPane, leftSimulation);
                    leftEpochsCount.setText(Integer.toString(leftSimulation.getEpoch()));
                    leftTheMostPopularGenotype.setText(leftSimulation.getTheMostPopularGenotype().toString());
                    leftAnimalsCount.setText(Integer.toString(leftSimulation.getNumberOfAnimals()));
                    leftNumberOfPlants.setText(Integer.toString(leftSimulation.getNumberOfPlants()));
                    if(config.dualMode){
                        nextEpoch(rightMapPane, rightSimulation);
                        rightEpochsCount.setText(Integer.toString(rightSimulation.getEpoch()));
                        rightAnimalsCount.setText(Integer.toString(rightSimulation.getNumberOfAnimals()));
                        rightTheMostPopularGenotype.setText(rightSimulation.getTheMostPopularGenotype().toString());
                        rightNumberOfPlants.setText(Integer.toString(rightSimulation.getNumberOfPlants()));

                    }
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
                                if(leftSimulation.getNumberOfAnimals() == 0)
                                    return;
                                nextEpoch(leftMapPane, leftSimulation);
                                if(leftSimulation.getNumberOfAnimals() == 0)
                                    return;
                                leftEpochsCount.setText(Integer.toString(leftSimulation.getEpoch()));
                                leftTheMostPopularGenotype.setText(leftSimulation.getTheMostPopularGenotype().toString());
                                leftAnimalsCount.setText(Integer.toString(leftSimulation.getNumberOfAnimals()));
                                leftNumberOfPlants.setText(Integer.toString(leftSimulation.getNumberOfPlants()));
                                if(config.dualMode) {
                                    if (rightSimulation.getNumberOfAnimals() == 0)
                                        return;
                                    nextEpoch(rightMapPane, rightSimulation);
                                    if (rightSimulation.getNumberOfAnimals() == 0)
                                        return;
                                    rightEpochsCount.setText(Integer.toString(rightSimulation.getEpoch()));
                                    rightAnimalsCount.setText(Integer.toString(rightSimulation.getNumberOfAnimals()));
                                    rightTheMostPopularGenotype.setText(rightSimulation.getTheMostPopularGenotype().toString());
                                    rightNumberOfPlants.setText(Integer.toString(rightSimulation.getNumberOfPlants()));
                                }
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                };
                timer.schedule(task, 0, 40l);
            }
        });

//        Application window
        GridPane maps = new GridPane();
        maps.add(leftMapPane,0,0);
        if(config.dualMode)
            maps.add(rightMapPane,1,0);

//        HBox statistics = new HBox();
        maps.add(leftStatisticsPane,0,1);
        if(config.dualMode)
            maps.add(rightStatisticsPane,1,1);

        VBox rootPane = new VBox();
        rootPane.getChildren().addAll(buttonsPane, maps);
        VBox.setVgrow(leftMapPane, Priority.ALWAYS);

        Scene scene = new Scene(rootPane);
        this.setTitle("Evolution Simulator");
        this.setScene(scene);
//        this.setResizable(false);
        this.sizeToScene();

        this.show();

    }
}
