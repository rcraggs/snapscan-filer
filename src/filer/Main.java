package filer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "FilerUI.fxml"
                )
        );

        Parent root = loader.load();
        FilerUIController controller = loader.getController();
        controller.setInputFilePath(this.getParameters().getUnnamed().get(0));



        primaryStage.setTitle("ScanSnap Filer");
        primaryStage.setScene(new Scene(root, 400, 1000));

        primaryStage.getScene().addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (event.getCode() == KeyCode.ESCAPE){
                System.exit(0);
            }
        });

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
