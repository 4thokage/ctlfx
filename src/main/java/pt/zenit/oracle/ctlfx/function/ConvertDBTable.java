package pt.zenit.oracle.ctlfx.function;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import pt.zenit.helpers.db.domain.DBTable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConvertDBTable implements Function<ObservableList<TreeItem<DBTable>>, List<pt.zenit.oracle.ctl.domain.DBTable>> {

    @Override
    public List<pt.zenit.oracle.ctl.domain.DBTable> apply(ObservableList<TreeItem<DBTable>> treeItems) {
    return treeItems.parallelStream()
        .map(TreeItem::getValue)
        .map(
            table ->
                new pt.zenit.oracle.ctl.domain.DBTable(
                    table.getOwner(), table.getName(), table.getStatus()))
        .collect(Collectors.toList());
    }
}
