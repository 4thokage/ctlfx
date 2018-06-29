package pt.zenit.oracle.ctlfx.controller;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.zenit.oracle.ctlfx.enums.DataTypesEnum;
import pt.zenit.oracle.ctl.enums.PadTypesEnum;

import java.util.prefs.Preferences;

/**
 * Controls application preferences
 */
public class PreferencesDialogController {

    @FXML
    private ComboBox<DataTypesEnum> dataTypesCombo;
    @FXML
    private ComboBox<PadTypesEnum> padTypeCombo;
    @FXML
    private TextField padChar;
    @FXML
    private Button btnClearPrefs;
    @FXML
    private Button btnSave;
    @FXML
    private TextArea optsTextArea;


    private Stage dialogStage;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        assert dialogStage != null : "\"dialogStage\" was not injected!";

        initCombos(padTypeCombo, dataTypesCombo);

        btnClearPrefs.setOnAction(e -> PreferencesController.softReset());

        btnSave.setDisable(true);
        optsTextArea.setDisable(true);
        btnSave.setOnAction(e -> PreferencesController.saveSettings(null, null, null, null));

    }

    /**
     * Initialez the Datatype and padType combos
     *
     * @param padTypeCombo {@link ComboBox} containing all {@link PadTypesEnum} values
     * @param dataTypesCombo {@link ComboBox} containing all {@link DataTypesEnum} values
     */
    private void initCombos(ComboBox<PadTypesEnum> padTypeCombo, ComboBox<DataTypesEnum> dataTypesCombo) {
        padTypeCombo.getItems().addAll(PadTypesEnum.values());

        dataTypesCombo.getItems().addAll(DataTypesEnum.values());
        dataTypesCombo.getSelectionModel().selectedItemProperty().addListener((selected, oldVal, newVal) -> loadSettings(selected));
        dataTypesCombo.getSelectionModel().selectFirst();
    }

    /**
     * Loads the settings saved in user preferences
     *
     * @param selected the {@link DataTypesEnum} selected (observable)
     */
    private void loadSettings(ObservableValue<? extends DataTypesEnum> selected) {
        Preferences prefs = PreferencesController.getPrefs();
        DataTypesEnum chosen = selected.getValue();
        if (chosen == DataTypesEnum.NUMBER) {
            PadTypesEnum type = PadTypesEnum.valueOf(prefs.get("pad.type.numeric", "''"));
            padTypeCombo.getSelectionModel().select(type);
            padChar.setText(prefs.get("pad.char.numeric", "??"));

        } else if (chosen == DataTypesEnum.VARCHAR2) {
            PadTypesEnum type = PadTypesEnum.valueOf(prefs.get("pad.type.string", "''"));
            padTypeCombo.getSelectionModel().select(type);
            padChar.setText(prefs.get("pad.char.string", "??"));

        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

}
