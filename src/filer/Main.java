package filer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        primaryStage.setScene(new Scene(root, 400, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
