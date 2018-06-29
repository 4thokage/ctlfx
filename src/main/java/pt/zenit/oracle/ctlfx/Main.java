package pt.zenit.oracle.ctlfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.oracle.ctlfx.controller.MainPageController;
import pt.zenit.oracle.ctlfx.controller.NewConnDialogController;
import pt.zenit.oracle.ctlfx.controller.PreferencesDialogController;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException{
        URL mainFxml = getClass().getResource("/view/mainPane.fxml");
        if(mainFxml == null) {
            logger.error("Error loading main fxml scene");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainFxml);
        Parent root = loader.load();
        MainPageController controller = loader.getController();
        controller.setMainApp(this);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("CTL-FX");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.getScene().setOnKeyPressed(k -> {
            if(k.isControlDown() && k.isAltDown() && KeyCode.S.equals(k.getCode())) {
                showSettingsDialog();
            }
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided Preferences node
     */
    public void showNewConnDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/newConnDlg.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            Scene scene = setupScene(page, dialogStage, "Add new Connection");
            dialogStage.setScene(scene);

            NewConnDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

        } catch (IOException e) {
            logger.error("Error showing 'add new connection' dialog", e);
        }
    }
    /**
     * Opens a Dialog with user prefenreces and saves it on esc or close presse
     */
    public void showSettingsDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/preferencesDlg.fxml"));
            Pane page = loader.load();

            Stage dialogStage = new Stage();
            Scene scene = setupScene(page, dialogStage, "Settings");
            scene.setOnKeyPressed(event -> {
                if("ESCAPE".equalsIgnoreCase(event.getCode().toString())) {
                    dialogStage.close();
                }
            });
            dialogStage.setScene(scene);

            PreferencesDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();
        } catch (IOException e) {
            logger.error("Error showing settings dialog", e);
        }
    }

    private Scene setupScene(Pane page, Stage dialogStage, String title) {
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        dialogStage.setResizable(false);
        return new Scene(page);
    }
}
