package pt.zenit.ctlper.controller;

import javafx.scene.control.ProgressBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.ctlper.domain.DBTable;
import pt.zenit.ctlper.domain.DBColumn;
import pt.zenit.ctlper.enums.CTLTypes;
import pt.zenit.ctlper.repository.JDBCRepository;
import java.io.*;
import java.util.Collection;


/**
 * Classe utilizada para geração dos ficheiros CTL
 */
class CTLBuilder {

    private static final Logger LOG = LoggerFactory.getLogger(CTLBuilder.class);

    private static final Clipboard clipboard = Clipboard.getSystemClipboard();
    private static final ClipboardContent content = new ClipboardContent();

    private static final VelocityEngine ve;

    static {
        ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());

        ve.init();
    }

    private CTLBuilder() {}

    static void build(TreeTableView.TreeTableViewSelectionModel<DBTable> selectionModel, Boolean copyToClipboard, boolean isLoad, boolean isExtract, ProgressBar progress) {
        progress.setProgress(0);
        if(!selectionModel.getSelectedItems().isEmpty()) {
            double eachPercentage = 1/selectionModel.getSelectedItems().size();

            for(TreeItem<DBTable> table : selectionModel.getSelectedItems()) {
                DBTable dbTable = table.getValue();
                Collection<DBColumn> tableColumns = prepareColumnPositions(JDBCRepository.getTableInfo(dbTable.getName()));
                if(isExtract) {
                    generateCTL(dbTable, tableColumns, CTLTypes.EXTRACT, copyToClipboard);
                }
                if(isLoad) {
                    generateCTL(dbTable, tableColumns, CTLTypes.LOAD, copyToClipboard);
                }
                progress.setProgress(progress.getProgress()+eachPercentage);
            }

        }

    }

    private static void generateCTL(DBTable dbTable, Collection<DBColumn> tableColumns, CTLTypes ctlType, Boolean copyToClipboard) {

        Template t = ve.getTemplate(String.format("/velocityTemplates/CTL_%s.vm",ctlType.toString()));

        VelocityContext context = new VelocityContext();
        context.put("table", dbTable);
        context.put("allColumns", tableColumns);
        context.put("nl", "\n");
        context.put("tab", "\t");
        context.put("maxLength", getMaxLength(tableColumns));
        context.put("opts", PreferencesController.buildOptions());

        StringWriter writer = new StringWriter();
        t.merge( context, writer );
        //TODO jsr: formatar o texto para ficar mais legivel (is it worth rly?)
        String ctlString = writer.toString();

        if(copyToClipboard) {
            content.putString(ctlString);
            clipboard.setContent(content);
            if(LOG.isDebugEnabled()) {
                LOG.debug("DEBUG CTL: \n\n"+ctlString);
            }
        } else {
            try (Writer fileWriter = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(String.format("%s_%s.ctl", ctlType.toString(), dbTable.getName())), "utf-8"))) {
                fileWriter.write(ctlString);
            } catch (IOException e) {
                LOG.error("Erro a escrever o ficheiro CTL da tabela {}, exeção: ", dbTable, e);
            }
        }
    }

    private static int getMaxLength(Collection<DBColumn> tables) {
        return tables.stream().mapToInt(DBColumn::getLengthValue).sum();
    }

    private static Collection<DBColumn> prepareColumnPositions(Collection<DBColumn> columns) {

        int sumColsLength = 0;
        int startPosition = 1;
        int oldColumnEndPosition = 0;
        for(DBColumn column : columns) {
            column.setStartPosition(startPosition);
            sumColsLength += column.getLengthValue();

            column.setEndPosition(oldColumnEndPosition + column.getLengthValue());
            oldColumnEndPosition += column.getLengthValue();

            startPosition = sumColsLength+1;
        }
        return columns;
    }
}
