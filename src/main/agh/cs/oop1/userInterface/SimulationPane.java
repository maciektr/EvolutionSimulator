package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SimulationPane extends VBox {
    private int windowWidth = 851;
    private int windowHeight = 555;
    private int cellWidth;
    private int cellHeight;
    private int circleRadius;

    private Simulation simulation;
    private GridPane mapPane;

    private Label epochsCount = new Label("0");
    private Label animalsCount = new Label("0");
    private Label numberOfPlants = new Label("0");
    private Label theMostPopularGenotype;

    SimulationPane(Configuration config){
        this.simulation = new Simulation(config);
        this.mapPane = this.createMapPane();
        this.drawMap();
        this.theMostPopularGenotype = new Label(this.simulation.getTheMostPopularGenotype().toString());
        GridPane statisticsPane = this.createStatisticsPane(epochsCount, theMostPopularGenotype, animalsCount, numberOfPlants);

        this.getChildren().addAll(this.mapPane, statisticsPane);


    }
    private GridPane createMapPane(){
        GridPane mapPane = new GridPane();
        mapPane.setPadding(new Insets(5, 5, 5, 5));
        mapPane.setAlignment(Pos.BOTTOM_CENTER);
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        this.cellWidth = this.windowWidth/enumerator.numberOfColumns();
        this.cellHeight = this.windowHeight/enumerator.numberOfRows();
        this.circleRadius = Math.min(this.cellWidth,this.cellHeight)/2;
        return mapPane;
    }

    private void drawMap(){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        for(int c = 0; c<enumerator.numberOfColumns(); c++)
            for(int r = 0; r<enumerator.numberOfRows(); r++){
                int x = this.cellWidth;
                int y = this.cellHeight;
                MapCell cell = enumerator.getMapCellByIndex(c,r);

                Rectangle rectangle = (new Rectangle(x,y));
                rectangle.setFill(SimulationPane.setRectangleColor(enumerator,c,r));
                this.mapPane.add(rectangle, c, r);
                if (cell != null && cell.anyAnimals())
                    this.mapPane.add(new Circle(this.circleRadius, Color.ORANGE),c,r);

            }
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

    private static Color setRectangleColor(MapEnumerator enumerator, int c, int r){
        MapCell cell = enumerator.getMapCellByIndex(c,r);
        Color res = (Color.LAWNGREEN);
        if (enumerator.indexesInJungle(c, r))
            res=  (Color.FORESTGREEN);

        if (cell != null && cell.isPlantSet())
            res = (Color.DARKGREEN);

        return res;
    }

    private void refreshMap(){
        MapEnumerator enumerator = new MapEnumerator(simulation.getMap());
        ArrayList<Node> removalCandidates = new ArrayList<>();
        ArrayList<Node> addCandidate = new ArrayList<>();

        for(Node node : this.mapPane.getChildren()){
            if(node == null)
                continue;
            int c = GridPane.getColumnIndex(node);
            int r = GridPane.getRowIndex(node);
            if(node instanceof Rectangle){
                Color color = SimulationPane.setRectangleColor(enumerator, c,r);
                if(((Rectangle)node).getFill() != color){
                    removalCandidates.add(node);
                    ((Rectangle) node).setFill(color);
                    addCandidate.add(node);
                }
            }else if(node instanceof Circle){
                removalCandidates.add(node);
            }
        }
        this.mapPane.getChildren().removeAll(removalCandidates);
        this.mapPane.getChildren().addAll(addCandidate);
        for(Animal animal : simulation.getMap().getAnimalsList()){
            this.mapPane.add(this.makeCircle(), enumerator.getColumn(animal.getPosition()),enumerator.getRow(animal.getPosition()));
        }
    }

    private Circle makeCircle(){
        return new Circle(this.circleRadius, Color.ORANGE);
    }

    public void nextEpoch() throws IllegalAccessException {
        this.simulation.nextEpoch();
        refreshMap();
        epochsCount.setText(Integer.toString(simulation.getEpoch()));
        theMostPopularGenotype.setText(simulation.getTheMostPopularGenotype() != null ? simulation.getTheMostPopularGenotype().toString():"");
        animalsCount.setText(Integer.toString(simulation.getNumberOfAnimals()));
        numberOfPlants.setText(Integer.toString(simulation.getNumberOfPlants()));

    }

}
