package pt.zenit.ctlper.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Dialog to edit details of a person.
 *
 * @author Marco Jakob
 */
public class NewConnDialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    @FXML
    private TextField connName;
    @FXML
    private TextField jdbThin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private TextField address;
    @FXML
    private TextField port;
    @FXML
    private TextField SID;


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
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }


    /**
     * Returns true if the user clicked OK, false otherwise.
     *
     * @return
     */
    public boolean isOkClicked() {
        return okClicked;
    }

    /**
     * Called when the user clicks ok.
     */
    @FXML
    private void handleOk() {
        if (isInputValid()) {
            PreferencesController.getPrefs().put("jdbc.conn."+connName.getText(), connString);
            okClicked = true;
            dialogStage.close();
        }
    }

    /**
     * Called when the user clicks TEST!.
     */
    @FXML
    private void handleTest() {
        if (isInputValid() && testConnection(connString)) {
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
        if(jdbThin.getText() != null && !jdbThin.getText().contains("//")) {
            connString = jdbThin.getText();
        } else if(jdbThin.getText().contains("//")) {
            connString = jdbThin.getText().replaceAll("//", username.getText() + "/"+password.getText());
        } else if(username.getText() != null && password.getText() != null && SID.getText() != null){
            connString = String.format("jdbc:oracle:thin:%s/%s@%s:%s/%s",username.getText(), password.getText(), address.getText(), port.getText(), SID.getText());
        } else {
            errorMessage += "INVALID!";
        }
        if(connName.getText() == null || connName.getText().isEmpty()) {
            errorMessage += "NOME VAZIO!";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Cenas mal");
            alert.setHeaderText("Ve la bem");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    private boolean testConnection(String connString) {
        try {
            DriverManager.getConnection(connString);
        } catch (SQLException e) {
            LOG.error("FAILED TESTING: [{}]",connString);
            return false;
        }
        return true;
    }

}