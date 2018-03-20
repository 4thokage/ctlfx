package pt.zenit.ctlper.domain;

/**
 * representa uma conexão a BD
 */
public class CTLOptions {

    private String loadType;
    private String headerOptions;
    private String charSet;

    public CTLOptions(String loadType, String headerOptions, String charSet) {
        this.loadType = loadType;
        this.headerOptions = headerOptions;
        this.charSet = charSet;
    }

    public String getLoadType() {
        return loadType;
    }

    public void setLoadType(String loadType) {
        this.loadType = loadType;
    }

    public String getHeaderOptions() {
        return headerOptions;
    }

    public void setHeaderOptions(String headerOptions) {
        this.headerOptions = headerOptions;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }
}
