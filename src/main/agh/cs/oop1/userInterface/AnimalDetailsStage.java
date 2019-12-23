package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.Animal;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class AnimalDetailsStage extends Stage {
    AnimalDetailsStage(Animal animal){
        GridPane rootPane = new GridPane();

        rootPane.add(new Label("Genotype: "),0,0);
        rootPane.add(new Label(animal.getGenotype().toString()), 1,0);
        rootPane.add(new Label("Energy: "),0,1);
        rootPane.add(new Label(Integer.toString(animal.getEnergy())), 1,1);
        rootPane.add(new Label("Number of children: "),0,2);
        rootPane.add(new Label(Integer.toString(animal.getNumberOfChildren())), 1,2);
        rootPane.add(new Label("Position: "),0,3);
        rootPane.add(new Label(animal.getPosition().toString()), 1,3);

        Scene scene = new Scene(rootPane);
        this.setTitle("Animal details");
        this.setScene(scene);
//        this.setResizable(false);
        this.sizeToScene();
        this.show();
    }
}
