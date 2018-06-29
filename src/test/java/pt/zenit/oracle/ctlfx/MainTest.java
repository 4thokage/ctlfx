package pt.zenit.oracle.ctlfx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import pt.zenit.oracle.ctlfx.controller.MainPageController;

import java.net.URL;

import static org.junit.Assert.assertNotNull;
import static org.testfx.assertions.api.Assertions.assertThat;

/**
 * Test class for the main pane
 */
public class MainTest extends ApplicationTest {


    private Parent root;
    private Main main = new Main();

    @Override
    public void start(Stage stage) throws Exception {
        URL mainFxml = getClass().getResource("/view/mainPane.fxml");
        assertNotNull(mainFxml);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(mainFxml);
        root = loader.load();
        MainPageController controller = loader.getController();
        controller.setMainApp(main);

        stage.setTitle("CTL-FX");
        stage.setScene(new Scene(root, 600, 400));
        stage.setResizable(false);

        stage.show();
    }

    @Test
    public void should_show_new_connection_dialog() throws Exception {
        clickOn("#btnAddNewConn");

        assertThat(lookup("#newConnDlg").queryAs(Pane.class));

    }

    @Test
    public void should_show_settings_dialog() throws Exception {
        clickOn("#optionsMenu").clickOn("#settingsMenu");
    }
}