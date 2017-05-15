package filer;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class FilerUIController {

    private Properties properties;
    private String inputFilePath;
    private List<String> directories = null;

    @FXML
    private VBox vbox;

    @FXML
    private ListView<String> directory_list;

    @FXML
    private Label input_file_path_label;

    @FXML
    private TextField new_file_name_text_box;

    private String searchTerm = "";

    public FilerUIController() {

        properties = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("SSFiler.properties");
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Store the list of directories
        try {
            directories = Files.readAllLines(Paths.get(properties.getProperty("directory_list")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void initialize() {

        VBox.setVgrow(directory_list, Priority.ALWAYS);

        this.new_file_name_text_box.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM-yyyy")) + ".pdf");
        this.new_file_name_text_box.setOnAction(event -> {
            this.moveFile();
        });

        FilteredList<String> filteredData = new FilteredList<>(
                FXCollections.observableArrayList(this.directories), p -> true);

        directory_list.setItems(filteredData);

        directory_list.addEventHandler(KeyEvent.KEY_RELEASED, event -> {

            if (event.getCode() == KeyCode.ENTER){
                this.moveFile();
            }

            if (event.getCode() != KeyCode.DOWN && event.getCode() != KeyCode.UP) {

                if (event.getCode() == KeyCode.BACK_SPACE) {
                    this.searchTerm = "";
                }else{
                    this.searchTerm += event.getText().trim();
                }

                filteredData.setPredicate(directory ->
                        this.stringContainsLettersInOrder(directory.toLowerCase(),
                                this.searchTerm.toLowerCase()));

                // If the data has changed set the first item as selected
                if (filteredData.size() > 0){
                    this.directory_list.getSelectionModel().select(0);
                }
            }
        });
    }

    private boolean stringContainsLettersInOrder(String haystack, String needle) {

        if (needle.equals(haystack)){
            return true;
        }
        else if (needle.length() == 0){
            return true;
        }else{
            int nextCharAt = haystack.indexOf(needle.charAt(0));
            if (nextCharAt > -1){
                return (stringContainsLettersInOrder(haystack.substring(nextCharAt+1), needle.substring(1)));
            }else{
                return false;
            }
        }
    }

    void setInputFilePath(String path) {

        if (Files.exists(Paths.get(path))){
            this.input_file_path_label.setText(path);
            this.inputFilePath = path;
        } else {
            this.input_file_path_label.setText("Doesn't exist: " + path);
        }
    }

    @FXML
    private void fileButtonAction(ActionEvent event) {
        moveFile();
    }

    private void moveFile() {

        Path sourcePath = Paths.get(this.inputFilePath);
        Path destPath = Paths.get(
                this.properties.getProperty("root"),
                this.directory_list.getSelectionModel().getSelectedItem(),
                this.new_file_name_text_box.getText());

        try {
            Files.move(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No File Provided");
            alert.setHeaderText(null);

            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();

            alert.setContentText(e.getMessage() + " " + exceptionAsString);
            alert.showAndWait();
        } finally {
            System.exit(0);
        }
    }
}
