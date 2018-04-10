package pt.zenit.ctlper.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.ctlper.domain.ComboBoxItem;
import pt.zenit.ctlper.enums.DataTypes;
import pt.zenit.ctlper.enums.PadTypes;

/**
 * Controller das preferencias do user
 */
public class PreferencesDialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    @FXML
    private ComboBox<DataTypes> dataTypesCombo;
    @FXML
    private ComboBox<PadTypes> padTypeCombo;
    @FXML
    private TextField padChar;
    @FXML
    private Button btnClearPrefs;


    //TODO jsr: eia ah muitas mais settings q maybe dão jeito era fixe fazer...

    private Stage dialogStage;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {

        initCombos(padTypeCombo, dataTypesCombo);

        btnClearPrefs.setOnAction(e -> PreferencesController.softReset());

    }

    private void initCombos(ComboBox<PadTypes> padTypeCombo, ComboBox<DataTypes> dataTypesCombo) {
        padTypeCombo.getItems().addAll(PadTypes.values());

        dataTypesCombo.getItems().addAll(DataTypes.values());
        dataTypesCombo.getSelectionModel().selectedItemProperty().addListener((selected, oldVal, newVal) -> loadSettings(selected));
        dataTypesCombo.getSelectionModel().selectFirst();
    }

    private void loadSettings(ObservableValue<? extends DataTypes> selected) {
        DataTypes chosen = selected.getValue();
        if (chosen == DataTypes.NUMERIC) {
            padTypeCombo.getSelectionModel().selectFirst();
            padChar.setText("'0'");

        } else if (chosen == DataTypes.VARCHAR) {
            padTypeCombo.getSelectionModel().selectLast();
            padChar.setText("' '");

        }
    }

    public Stage getDialogStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
}