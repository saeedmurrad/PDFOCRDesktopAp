package org.pdfocrexport.com.pdfocrdesktopapp;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.pdfocrexport.com.pdfocrdesktopapp.utils.SlipParser;

import java.io.File;

public class MainController {
    @FXML
    private Label selectedPDFLabel;

    @FXML
    private Label selectedOutputDirLabel;

    @FXML
    private Button processButton;

    private File selectedPDFFile;
    private File selectedOutputDirectory;

    @FXML
    protected void onSelectPDFButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf"));
        Stage stage = (Stage) selectedPDFLabel.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            selectedPDFFile = file;
            selectedPDFLabel.setText(file.getAbsolutePath());
            if (selectedOutputDirectory != null) {
                processButton.setDisable(false);
            }
        }
    }

    @FXML
    protected void onSelectOutputDirButtonClick() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) selectedOutputDirLabel.getScene().getWindow();
        File directory = directoryChooser.showDialog(stage);
        if (directory != null) {
            selectedOutputDirectory = directory;
            selectedOutputDirLabel.setText(directory.getAbsolutePath());
            if (selectedPDFFile != null) {
                processButton.setDisable(false);
            }
        }
    }

    @FXML
    protected void onProcessButtonClick() {
        if (selectedPDFFile != null && selectedOutputDirectory != null) {
            int result = SlipParser.processPDF(selectedPDFFile, selectedOutputDirectory.getPath());
            if (result == 1) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Processing completed successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Something went wrong during processing.");
            }
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}