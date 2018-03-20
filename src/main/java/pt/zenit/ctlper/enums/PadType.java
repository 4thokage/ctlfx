package pt.zenit.ctlper.enums;

/**
 * Created by jsilvaro on 2018-03-19.
 */
public enum PadType {
    LPAD("'0'"),
    RPAD("' '");

    private String padChar;

    PadType(String padChar) {
        this.padChar = padChar;
    }

    public String getPadChar() {
        return this.padChar;
    }
}
