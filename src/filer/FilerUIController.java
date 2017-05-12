package filer;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FilerUIController {

    private String inputFilePath;

    @FXML
    private Label input_file_path_label;

    @FXML
    private TextField new_file_name_text_box;

    public FilerUIController() { }

    public void setInputFilePath(String path) {
        this.input_file_path_label.setText(path);
        this.inputFilePath = path;
    }

    @FXML
    private void fileButtonAction(ActionEvent event) {

        Path sourcePath = Paths.get(this.inputFilePath);
        Path destPath = Paths.get(sourcePath.getParent().toString(), this.new_file_name_text_box.getText());

        try {
            Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
