package pt.zenit.oracle.ctlfx.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.helpers.db.DBInfo;
import pt.zenit.helpers.db.JDBCRepository;
import pt.zenit.oracle.ctl.domain.CTLOptions;
import pt.zenit.oracle.ctlfx.Main;
import pt.zenit.oracle.ctlfx.domain.ComboBoxItem;
import pt.zenit.helpers.db.domain.DBTable;
import pt.zenit.oracle.ctlfx.function.ConvertDBTable;

import java.net.URL;
import java.sql.SQLException;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class MainPageController implements Initializable {

    private static final Logger logger = LoggerFactory.getLogger(MainPageController.class);
    private static final String CONN_PREFIX ="jdbc.conn.";

    private Main mainApp;

    @FXML //  fx:id="connCombo"
    private ComboBox<ComboBoxItem> connCombo;

    @FXML //  fx:id="tableHelper"
    private TreeTableView<DBTable> tableHelper;

    @FXML // fx:id="copyToClipboard"
    private RadioButton copyToClipboard;

    @FXML // fx:id="btnGenerate"
    private Button btnGenerate;

    @FXML // fx:id="btnRemoveConn"
    private Button btnRemoveConn;

    @FXML // fx:id="btnAddNewConn"
    private Button btnAddNewConn;

    @FXML // fx:id="isExtract"
    private CheckBox isExtract;

    @FXML // fx:id="isLoad"
    private CheckBox isLoad;

    @FXML // fx:id="settingsMenu"
    public MenuItem settingsMenu;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        assert connCombo != null : "fx:id=\"connCombo\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert tableHelper != null : "fx:id=\"tableHelper\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert copyToClipboard != null : "fx:id=\"copyToClipboard\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnGenerate != null : "fx:id=\"btnGenerate\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert isExtract != null : "fx:id=\"isExtract\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert isLoad != null : "fx:id=\"isLoad\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnAddNewConn != null : "fx:id=\"btnAddNewConn\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert settingsMenu != null : "fx:id=\"settingsMenu\" was not injected: check your FXML file 'mainPane.fxml'.";
        assert btnRemoveConn != null : "fx:id=\"btnRemoveConn\" was not injected: check your FXML file 'mainPane.fxml'.";


        Preferences prefs = PreferencesController.loadDefaultPreferences();
        initConnCombo(connCombo, prefs);

        TreeTableView.TreeTableViewSelectionModel<DBTable> selection = tableHelper.getSelectionModel();
        selection.setSelectionMode(SelectionMode.MULTIPLE);

        btnGenerate.setOnAction(e -> CTLBuilderFX.build(new ConvertDBTable().apply(selection.getSelectedItems()), copyToClipboard.isSelected(), isLoad.isSelected(), isExtract.isSelected(), new CTLOptions.CTLOptionsBuilder().build()));

        btnAddNewConn.setOnAction(e -> {
            mainApp.showNewConnDialog();
            initConnCombo(connCombo, prefs);
        });

        btnRemoveConn.setOnAction(e -> {
            prefs.remove(CONN_PREFIX+connCombo.getSelectionModel().getSelectedItem().getLabel());
            initConnCombo(connCombo, prefs);
        });

        settingsMenu.setOnAction(t -> mainApp.showSettingsDialog());

    }

    private void reloadTables(TreeTableView<DBTable> tableHelper, Collection<DBTable> dbTables) {
        tableHelper.setRoot(null);
        tableHelper.getColumns().clear();

        final TreeItem<DBTable> root = new TreeItem<>(new DBTable("root", "root", "root"));
        root.setExpanded(true);

        TreeTableColumn<DBTable,String> column = new TreeTableColumn<>("Table Name");
        column.setPrefWidth(350);

        for(DBTable table : dbTables) {
            root.getChildren().add(new TreeItem<>(table));
        }

        column.setCellValueFactory((TreeTableColumn.CellDataFeatures<DBTable, String> p) -> {
            String dataVal = p.getValue().getValue().getName() + "    [" +p.getValue().getValue().getOwner() + "]";
            return new ReadOnlyStringWrapper(dataVal);
        });

        tableHelper.getColumns().add(column);
        tableHelper.setRoot(root);
        tableHelper.setPrefWidth(368);
        tableHelper.setShowRoot(false);

    }


    /**
     * Initializes the connections combo box
     *
     * @param connCombo connection combobox
     * @param prefs connection preferences
     */
    private void initConnCombo(ComboBox<ComboBoxItem> connCombo, Preferences prefs) {

        try {
            connCombo.getItems().clear();
            for(String key : prefs.keys()) {
                if(key.startsWith(CONN_PREFIX)) {
                    connCombo.getItems().addAll(
                            new ComboBoxItem(key.replaceAll(CONN_PREFIX,""),prefs.get(key, "undefined")
                    ));

                }
            }
            connCombo.setButtonCell(new ListCell<ComboBoxItem>() {
                @Override
                protected void updateItem(ComboBoxItem t, boolean bln) {
                    super.updateItem(t, bln);
                    if (bln) {
                        setText("");
                    } else {
                        setText(t.getLabel());
                    }
                }
            });

            connCombo.setCellFactory(listView -> new ListCell<ComboBoxItem>() {
                @Override
                public void updateItem(ComboBoxItem item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setDisable(false);
                        setVisible(false);
                    } else {
                        setText(item.getLabel());
                        setDisable(false);
                    }
                }
            });

            connCombo.getSelectionModel().selectedItemProperty().addListener((selected, oldVal, newVal) -> tryConnection(connCombo, prefs));
            connCombo.getSelectionModel().selectFirst();
        } catch (BackingStoreException e) {
            logger.error("Erro a carregar as conexões", e);
        }
    }

    /**
     * Attempts a db connection via {@link JDBCRepository} and choosen driver classpath
     *
     */
    private void tryConnection(ComboBox<ComboBoxItem> connCombo, Preferences prefs) {
        if(prefs != null && connCombo != null && connCombo.getSelectionModel() != null && connCombo.getSelectionModel().getSelectedItem() != null) {
            try {
                JDBCRepository.disconnect();
                JDBCRepository.connect(prefs.get("jdbc.driver", PreferencesController.DEFAULT_DRIVER), connCombo.getSelectionModel().getSelectedItem().getValue());
                DBInfo.setConn(JDBCRepository.getConnection());
                ObservableList<DBTable> dbTables = FXCollections.observableArrayList(DBInfo.getDBInfo());
                reloadTables(tableHelper, dbTables);
            } catch (SQLException | ClassNotFoundException e) {
                logger.error("Error connecting to DB via JDBC", e);
            }
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp {@link Main} app que está running
     */
    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;

    }

}
