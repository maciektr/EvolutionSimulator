package agh.cs.oop1.userInterface;

import agh.cs.oop1.simulation.*;
import javafx.application.Platform;
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
import java.util.Timer;
import java.util.TimerTask;

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
                    left.nextEpoch();
                    if(config.dualMode)
                        right.nextEpoch();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                }
            }
        });
        Timer timer = new Timer();

        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if(numberOfEpochsField.getText().equals(""))
                    return;

                int epochs = Integer.parseInt(numberOfEpochsField.getText());
                TimerTask task = new TimerTask(){
                    int counter = 0;
                    @Override
                    public void run() {
                        if(epochs==++counter){
                            timer.cancel();
                            timer.purge();
                        }
                        System.out.println("ADD");
                        Platform.runLater(()-> {
                            try {
                                left.nextEpoch();
                                if(config.dualMode)
                                    right.nextEpoch();
                            } catch (IllegalAccessException ex) {
                                ex.printStackTrace();
                            }
                        });
                    }
                };
                timer.schedule(task, 0, 40l);
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
}
