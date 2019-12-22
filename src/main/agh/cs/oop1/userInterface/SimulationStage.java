package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import com.google.gson.Gson;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;

public class SimulationStage extends Stage {

    SimulationStage(Configuration config){
        SimulationPane left = new SimulationPane(config);
        SimulationPane right = config.dualMode ? new SimulationPane(config) : null;

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

        Button buttonRun = new Button("Run");
        Button buttonNextEpoch = new Button("Next epoch");
        Button buttonStop = new Button("Stop");
        Button buttonSaveToFile = new Button("Save to file");

        buttonsPane.add(text1, 0, 0);
        buttonsPane.add(numberOfEpochsField, 1, 0);
        buttonsPane.add(buttonRun, 2, 0);
        buttonsPane.add(buttonNextEpoch, 3, 0);
        buttonsPane.add(buttonStop, 4,0);
        buttonsPane.add(buttonSaveToFile,5,0);

        buttonsPane.setAlignment(Pos.TOP_CENTER);
        buttonsPane.setPadding(new Insets(10, 10, 0, 10));
        buttonsPane.setHgap(10);

        buttonSaveToFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    if(config.dualMode) {
                        saveStatisticsToFile(left, "Left");
                        saveStatisticsToFile(right, "Right");
                    }else
                        saveStatisticsToFile(left, "Main");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

        buttonNextEpoch.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                try {
                    left.nextEpoch();
                    if(config.dualMode)
                        right.nextEpoch();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });

        Timeline timelineNextEpochs = new Timeline(new KeyFrame(Duration.millis(40), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    left.nextEpoch();
                    if(config.dualMode)
                        right.nextEpoch();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        }));

        buttonRun.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(numberOfEpochsField.getText().equals("")){
                    timelineNextEpochs.setCycleCount(Timeline.INDEFINITE);
                }else {
                    int epochs = Integer.parseInt(numberOfEpochsField.getText());
                    timelineNextEpochs.setCycleCount(epochs);
                }
                timelineNextEpochs.play();
            }
        });

        buttonStop.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                timelineNextEpochs.stop();
            }
        });


        HBox maps = new HBox();
        maps.getChildren().add(left);
        if(config.dualMode)
            maps.getChildren().add(right);

        VBox rootPane = new VBox();
        rootPane.getChildren().addAll(buttonsPane, maps);

        Scene scene = new Scene(rootPane);
        this.setTitle("Evolution Simulator");
        this.setScene(scene);
//        this.setResizable(false);
        this.sizeToScene();

        this.show();

    }

    private void saveStatisticsToFile(SimulationPane simulationPane, String name) throws IOException {
        try(Writer writer = new FileWriter("EvolutionalSimulatorStatistics_nEpochs"+simulationPane.getStatistics().getEpoch()+"_"+name+".json")) {
            Gson gson = new Gson();
            gson.toJson(simulationPane.getStatistics(), writer);
        }
    }
}
