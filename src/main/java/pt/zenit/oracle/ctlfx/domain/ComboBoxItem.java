package pt.zenit.oracle.ctlfx.domain;

/**
 * Reprents a JavaFX ComboBox simple item
 */
public class ComboBoxItem {

    private String label;

    private String value;

    public ComboBoxItem(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
