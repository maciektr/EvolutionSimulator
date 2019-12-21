package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

//import java.awt.*;

public class Main extends Application {
    private static final String parametersPath = "parameters.json";

    @Override
    public void start(Stage primaryStage) throws Exception{
        try {
            final Configuration config = Configuration.fromJson(Main.parametersPath);
            new SimulationStage(new Simulation(config));

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
