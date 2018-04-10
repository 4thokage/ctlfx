package pt.zenit.ctlper.domain;

import pt.zenit.ctlper.enums.PadTypes;

/**
 * Representa uma coluna de uma tabela de DB
 */
public class DBColumn {

    private static final int DEFAULT_PRECISION = 38;

    private String name;
    private String dataType;
    private int dataLength;
    private int dataPrecision;

    private int startPosition;
    private int endPosition;

    public DBColumn(String name, String dataType, int dataLength, int dataPrecision) {
        this.name = name;
        this.dataType = dataType;
        this.dataLength = dataLength;
        if(dataPrecision != 0) {
            this.dataPrecision = dataPrecision;
        } else {
            this.dataPrecision = DEFAULT_PRECISION;
        }

    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public int getLengthValue() {
        return isNumeric() ? this.dataPrecision : this.dataLength;
    }

    public String getPadChar() {
        return isNumeric() ? "'0'" : "' '";
    }

    public String getPadType() {
        return isNumeric() ? PadTypes.LPAD.toString() : PadTypes.RPAD.toString();
    }

    private boolean isNumeric() {
        return "NUMBER".equalsIgnoreCase(this.dataType);
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }


    private boolean isValidObject() {
        return getName() != null
                && getLengthValue() != 0
                && getPadChar() != null
                && getPadType() != null
                && (getEndPosition() > getStartPosition());
    }

    public int getStartPosition() {
        return startPosition;
    }


    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    @Override
    public String toString() {
        return "DBColumn{" +
                "name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                ", dataLength=" + dataLength +
                ", dataPrecision=" + dataPrecision +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", valid=" + isValidObject() +
                '}';
    }
}
