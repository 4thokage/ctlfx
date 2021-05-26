package pt.zenit.oracle.ctlfx.function;

import pt.zenit.helpers.db.domain.DBColumn;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ConvertDBColumn implements Function<Collection<DBColumn>, Collection<pt.zenit.oracle.ctl.domain.DBColumn>> {

    @Override
    public Collection<pt.zenit.oracle.ctl.domain.DBColumn> apply(Collection<DBColumn> dbColumns) {

        return dbColumns.stream()
                .map(dbColumn -> new pt.zenit.oracle.ctl.domain.DBColumn(dbColumn.getName(), dbColumn.getDataType(), dbColumn.getLengthValue(), dbColumn.getLengthValue()))
                .collect(Collectors.toList());
    }
}
