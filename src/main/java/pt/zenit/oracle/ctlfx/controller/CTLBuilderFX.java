package pt.zenit.oracle.ctlfx.controller;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.helpers.db.DBInfo;
import pt.zenit.helpers.db.JDBCRepository;

import pt.zenit.oracle.ctl.CTLBuilder;
import pt.zenit.oracle.ctl.domain.CTLOptions;
import pt.zenit.oracle.ctl.domain.DBColumn;
import pt.zenit.oracle.ctl.domain.DBTable;
import pt.zenit.oracle.ctl.enums.CTLTypesEnum;
import pt.zenit.oracle.ctlfx.function.ConvertDBColumn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * FX implementation of ctlbuilder
 */
class CTLBuilderFX {

    private static final Logger logger = LoggerFactory.getLogger(CTLBuilderFX.class);


    /**
     * Default constructor
     */
    private CTLBuilderFX() {
        throw new AssertionError("Cant instantiate this class");
    }


    /**
     * Build method: calls CTLBuilder with accordingly, returns how many files were generated.
     *
     * @param selectionModel  The tables selected in the TableView
     * @param copyToClipboard flag indicating that the LAST file should be copied to the clipboard
     * @param isLoad          flag indicating that the program should generate a ctl of type LOAD
     * @param isExtract       flag indicating that the program should generate a ctl of type extract
     */
    static void build(Collection<DBTable> selectionModel, Boolean copyToClipboard, boolean isLoad, boolean isExtract, CTLOptions opts) {

        int count = 0;
        if (!selectionModel.isEmpty()) {
            StringBuilder completeResultSB = new StringBuilder();
            for (DBTable dbTable : selectionModel) {
                DBInfo.setConn(JDBCRepository.getConnection());
                Collection<pt.zenit.helpers.db.domain.DBColumn> dbColumns =DBInfo.getTableInfo(dbTable.getName());
                Collection<DBColumn> tableCols = new ConvertDBColumn().apply(dbColumns);
                try {
                    generateCTL(copyToClipboard, isLoad, isExtract, opts, completeResultSB, dbTable, tableCols);
                } catch (IOException e) {
                    logger.error("Error writing CTL result to file", e);
                }
                count++;

            }
            if(Boolean.TRUE.equals(copyToClipboard)) {
                final Clipboard clipboard = Clipboard.getSystemClipboard();
                final ClipboardContent content = new ClipboardContent();
                content.putString(completeResultSB.toString());
                clipboard.setContent(content);

            }
        }
        logger.info("build count: {}", count);

    }

    private static void generateCTL(Boolean copyToClipboard, boolean isLoad, boolean isExtract, CTLOptions opts, StringBuilder completeResultSB, DBTable dbTable, Collection<DBColumn> tableColumns) throws IOException {
        if (isExtract) {
            if (Boolean.TRUE.equals(copyToClipboard)) {
                completeResultSB.append(CTLBuilder.generateCTL(dbTable, tableColumns, CTLTypesEnum.EXTRACT, opts));
            } else {
                FileUtils.writeStringToFile(new File(dbTable.getName()+"_E.ctl"), CTLBuilder.generateCTL(dbTable, tableColumns, CTLTypesEnum.EXTRACT, opts), Charset.defaultCharset());

            }
        }
        if (isLoad) {
            if (Boolean.TRUE.equals(copyToClipboard)) {
                completeResultSB.append(CTLBuilder.generateCTL(dbTable, tableColumns, CTLTypesEnum.LOAD, opts));
            } else {
                FileUtils.writeStringToFile(new File(dbTable.getName()+"_L.ctl"), CTLBuilder.generateCTL(dbTable, tableColumns, CTLTypesEnum.LOAD, opts), Charset.defaultCharset());
            }
        }
    }


}
