package pt.zenit.ctlper.domain;

/**
 * Created by jsilvaro on 2018-03-17.
 */
public class DBTable {
    private String owner;
    private String name;
    private String status;

    public DBTable(String owner, String name, String status) {
        this.owner = owner;
        this.name = name;
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
