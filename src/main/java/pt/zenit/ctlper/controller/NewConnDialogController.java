package pt.zenit.ctlper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.ctlper.repository.JDBCRepository;

import java.sql.SQLException;

/**
 * Controlla a dialog de nova conexão
 */
public class NewConnDialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    @FXML
    private TextField connName;
    @FXML
    private TextField jdbcThin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private TextField sid;

    @FXML
    private Button btnTest;


    private Stage dialogStage;
    private String connString;
    private boolean okClicked = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        //No need to initialize
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage stage to set
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * @return Returns true if the user clicked OK, false otherwise.
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        handleTest();
        if (btnTest.getTextFill().equals(Paint.valueOf("green"))) {
            PreferencesController.getPrefs().put("jdbc.conn."+connName.getText(), connString);
            okClicked = true;
            dialogStage.close();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("No can do!");
            alert.setHeaderText("A conn n presta br0!");
            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks TEST!.
     */
    @FXML
    private void handleTest() {
        if(isInputValid() && testConnection(connString)) {
            btnTest.setTextFill(Paint.valueOf("green"));
        } else {
            btnTest.setTextFill(Paint.valueOf("red"));
        }

    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        //TODO jsr: validar se a jdbc vem com user e pass duma melhor forma
        if(StringUtils.isNotBlank(jdbcThin.getText()) && !jdbcThin.getText().contains("//")) {
            connString = jdbcThin.getText();
        } else if(jdbcThin.getText().contains("//")) {
            connString = jdbcThin.getText().replaceAll("//", username.getText() + "/"+password.getText());
        } else if(username.getText() != null && password.getText() != null && sid.getText() != null){
            connString = String.format("jdbc:oracle:thin:%s/%s@%s:%s/%s",username.getText(), password.getText(), address.getText(), port.getText(), sid.getText());
        } else {
            errorMessage += "INVALID!";
        }
        if(connName.getText() == null || connName.getText().isEmpty()) {
            errorMessage += "NOME VAZIO!";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Something wrong");
            alert.setHeaderText("Please check all fields br0");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private boolean testConnection(String connString) {
        try {
            JDBCRepository.disconnect();
            JDBCRepository.connect(PreferencesController.DEFAULT_DRIVER ,connString);
        } catch (SQLException | ClassNotFoundException e) {
            LOG.error("FAILED TESTING: [{}]",connString, e);
            return false;
        }
        return true;
    }

}