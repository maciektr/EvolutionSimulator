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
import static java.lang.Double.min;

public class SimulationPane extends VBox {
    private int windowWidth = 851;
    private int windowHeight = 555;
    private int cellWidth;
    private int cellHeight;
    private int circleRadius;

    private Simulation simulation;
    private GridPane mapPane;
    private Configuration config;

    private Label epochsCount = new Label("0");
    private Label animalsCount = new Label("0");
    private Label numberOfPlants = new Label("0");
    private Label theMostPopularGenotype = new Label("");
    private Label averageEnergy = new Label("0");
    private Label averageDeadAnimalEpoch = new Label("");
    private Label averageNumberOfChildren = new Label("0");

    SimulationPane(Configuration config) throws IllegalAccessException {
        this.config = config;
        this.simulation = new Simulation(config);
        this.mapPane = this.createMapPane();
        this.drawMap();

        this.epochsCount.setText(Integer.toString(simulation.getEpoch()));
        this.theMostPopularGenotype.setText(simulation.getStatistics().getDominantGenotype() != null ? simulation.getStatistics().getDominantGenotype().toString():"");
        this.animalsCount.setText(Integer.toString(simulation.getNumberOfAnimals()));
        this.numberOfPlants.setText(Integer.toString(simulation.getStatistics().getCurrentNumberOfPlants()));
        this.averageEnergy.setText(Integer.toString(config.startEnergy));

        GridPane statisticsPane = this.createStatisticsPane();
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

    private Color getCircleColor(MapEnumerator enumerator, int c, int r) throws IllegalAccessException {
        for(Animal a : enumerator.getMapCellByIndex(c,r).getAnimals()){
            if(a.getGenotype().equals(this.simulation.getStatistics().getDominantGenotype()))
                return Color.RED;
        }
        int animalEnergy = enumerator.getMapCellByIndex(c,r).getMostEnergeticAnimal().getEnergy();
        return Color.hsb(27,(min(1.0,((double)animalEnergy / (double)this.config.startEnergy))*0.8)+0.2,1.0);
    }

    private Circle createCircle(MapEnumerator enumerator, int c, int r) throws IllegalAccessException {
        Circle pane = new Circle(this.circleRadius, this.getCircleColor(enumerator, c, r));
        pane.setOnMouseClicked(e -> {
            try {
                new AnimalDetailsStage(enumerator.getMapCellByIndex(c,r).getMostEnergeticAnimal());
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        });
        return pane;
    }

    private void drawMap() throws IllegalAccessException {
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
                    this.mapPane.add(this.createCircle(enumerator,c,r), c, r);
            }
    }

    private GridPane createStatisticsPane(){
        GridPane statisticsPane = new GridPane();
        statisticsPane.setPadding(new Insets(5,5,5,5));
        statisticsPane.setAlignment(Pos.BOTTOM_CENTER);
        statisticsPane.add(new Label("Epoch number: "),0,0);
        statisticsPane.add(epochsCount,1,0);
        statisticsPane.add(new Label("Number of animals: "),0,1);
        statisticsPane.add(animalsCount,1,1);
        statisticsPane.add(new Label("Number of plants: "),0,2);
        statisticsPane.add(numberOfPlants, 1,2);
        statisticsPane.add(new Label("The most popular genotype (red): "),0,3);
        statisticsPane.add(theMostPopularGenotype,1,3);
        statisticsPane.add(new Label("Average animal energy: "),0,4);
        statisticsPane.add(this.averageEnergy, 1,4);
        statisticsPane.add(new Label("Average dead animals epoch: "), 0, 5);
        statisticsPane.add(this.averageDeadAnimalEpoch, 1,5);
        statisticsPane.add(new Label("Average number of children for living animals: "),0,6);
        statisticsPane.add(this.averageNumberOfChildren,1,6);
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

    private void refreshMap() throws IllegalAccessException {
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
            int c = enumerator.getColumn(animal.getPosition());
            int r = enumerator.getRow(animal.getPosition());
            this.mapPane.add(this.createCircle(enumerator,c,r),c,r);
        }
    }

    public void nextEpoch() throws IllegalAccessException {
        this.simulation.nextEpoch();
        refreshMap();
        epochsCount.setText(Integer.toString(simulation.getEpoch()));
        theMostPopularGenotype.setText(simulation.getStatistics().getDominantGenotype() != null ? simulation.getStatistics().getDominantGenotype().toString():"");
        animalsCount.setText(Integer.toString(simulation.getNumberOfAnimals()));
        numberOfPlants.setText(Integer.toString(simulation.getStatistics().getCurrentNumberOfPlants()));
        this.averageEnergy.setText(Integer.toString(simulation.getStatistics().getCurrentAverageEnergy()));
        this.averageDeadAnimalEpoch.setText(simulation.getStatistics().getAverageDeadAnimalEpoch() == 0 ? "":Integer.toString(simulation.getStatistics().getAverageDeadAnimalEpoch()));
        this.averageNumberOfChildren.setText(Integer.toString(simulation.getStatistics().getAverageNumberOfChildren()));
    }

    public Statistics getStatistics(){
        return this.simulation.getStatistics();
    }

}
