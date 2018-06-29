package pt.zenit.oracle.ctlfx.controller;

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
import pt.zenit.oracle.ctlfx.enums.ErrorCodesEnum;
import pt.zenit.helpers.db.JDBCRepository;
import java.sql.SQLException;

/**
 * Controller of 'add new connection dialog'
 */
public class NewConnDialogController {

    private static final Logger LOG = LoggerFactory.getLogger(MainPageController.class);

    private static final String RESULT_OK="OK";
    private static final String RESULT_COLOR_OK="green";
    private static final String RESULT_COLOR_NOK="red";
    private static final String JDBC_THIN_SEPARATOR = "//";

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
            PreferencesController.getPrefs().put("jdbc.conn." + connName.getText(), connString);
            okClicked = true;
            dialogStage.close();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Connection unsuccessful");
            alert.setHeaderText("Fix the connection settings and try again.");
            alert.showAndWait();
        }
    }

    /**
     * Called when the user clicks TEST!.
     */
    @FXML
    private void handleTest() {
        if (isInputValid()) {
            String result = testConnection(connString);
            if (RESULT_OK.equalsIgnoreCase(result)) {
                btnTest.setTextFill(Paint.valueOf(RESULT_COLOR_OK));
            } else {
                btnTest.setTextFill(Paint.valueOf(RESULT_COLOR_NOK));
                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Connection test failed");
                alert.setHeaderText(result);
                alert.showAndWait();
            }
        }

    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */

    private boolean isInputValid() {
        boolean isValid = false;
        if (StringUtils.isNotBlank(connName.getText())) {
            connString = getConnStringFromUserInput();
            if (!ErrorCodesEnum.INVALID_FORMAT.toString().equalsIgnoreCase(connString)) {
                isValid =  true;
            } else {
                Alert alert = new Alert(AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Something wrong");
                alert.setHeaderText("Please check all fields, name is mandatory!");

                alert.showAndWait();

                isValid = false;
            }
        }
        return isValid;
    }

    /**
     * Exracts a jbdcthin url from user input
     *
     * @return result {@link String} with a valid JDBC Thin URL, or an error.
     */
    private String getConnStringFromUserInput() {
        String result;
        if (StringUtils.isNotBlank(jdbcThin.getText()) && !jdbcThin.getText().contains(JDBC_THIN_SEPARATOR)) {
            result = jdbcThin.getText();
        } else if (jdbcThin.getText().contains(JDBC_THIN_SEPARATOR)) {
            result = jdbcThin.getText().replaceAll(JDBC_THIN_SEPARATOR, username.getText() + "/" + password.getText());
        } else if (username.getText() != null && password.getText() != null && sid.getText() != null && address.getText() != null) {
            result = String.format("jdbc:oracle:thin:%s/%s@%s:%s/%s", username.getText(), password.getText(), address.getText(), port.getText(), sid.getText());
        } else {
            result = ErrorCodesEnum.INVALID_FORMAT.toString();
        }

        return result;
    }

    /**
     * Attempts a DB Connection
     */
    private String testConnection(String connString) {
        try {
            JDBCRepository.disconnect();
            JDBCRepository.connect(PreferencesController.DEFAULT_DRIVER, connString);
            JDBCRepository.disconnect();
        } catch (SQLException | ClassNotFoundException e) {
            LOG.error("FAILED TESTING: [{}]", connString, e);
            return e.getMessage();
        }
        return RESULT_OK;
    }
}
