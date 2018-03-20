package pt.zenit.ctlper;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.ctlper.controller.MainPageController;
import pt.zenit.ctlper.controller.NewConnDialogController;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException{
        URL mainFxml = getClass().getResource("/view/mainPane.fxml");
        if(mainFxml == null) {
            LOG.error("Error loading main fxml scene");
            return;
        }
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainFxml);
        Parent root = loader.load();
        MainPageController controller = loader.getController();
        controller.setMainApp(this);

        this.primaryStage = primaryStage;
        primaryStage.setTitle("CTLper");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }


    /**
     * Opens a dialog to edit details for the specified person. If the user
     * clicks OK, the changes are saved into the provided person object and true
     * is returned.
     * @return true if the user clicked OK, false otherwise.
     */
    public boolean showPersonEditDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/newConnDlg.fxml"));
            Pane page = loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nova Conexão");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            NewConnDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
