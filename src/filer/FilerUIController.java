package filer;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FilerUIController {

    @FXML
    private Label input_file_path;

    public FilerUIController() { }

    public void setInputFilePath(String path) {
        this.input_file_path.setText(path);
    }

}
