package pt.zenit.ctlper.domain;

/**
 * Created by jsilvaro on 2018-03-17.
 */
public class DBTable {
    private String owner;
    private String tableName;
    private String status;

    public DBTable(String owner, String tableName, String status) {
        this.owner = owner;
        this.tableName = tableName;
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
