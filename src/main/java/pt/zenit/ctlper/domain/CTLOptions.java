package pt.zenit.ctlper.domain;

import pt.zenit.ctlper.controller.PreferencesController;
import pt.zenit.ctlper.enums.PadTypes;

import java.io.Serializable;
import java.util.prefs.Preferences;

/**
 * Guarda as prefenrecias do user que serão utilizadas na geração dos ficheiros de ontrole (CTL)
 */
public class CTLOptions implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String[] DEFAULT_LOADER_TYPE = new String[]{"ctl.load.type", "append"};
    private static final String[] DEFAULT_LOADER_HEADER = new String[]{"ctl.load.option", "OPTIONS (BINDSIZE=512000, ROWS=10000)"};
    private static final String[] DEFAULT_LOADER_CHARSET = new String[]{"ctl.load.charset", "CHARACTERSET WE8ISO8859P1"};

    private static final String[] DEFAULT_PAD_TYPE_NUMERIC = new String[]{"ctl.pad.type.numeric", PadTypes.LPAD.toString()};
    private static final String[] DEFAULT_PAD_CHAR_NUMERIC = new String[]{"ctl.pad.char.numeric", "'0'"};

    private static final String[] DEFAULT_PAD_TYPE_STRING = new String[]{"ctl.pad.type.string", PadTypes.RPAD.toString()};
    private static final String[] DEFAULT_PAD_CHAR_STRING = new String[]{"ctl.pad.char.string", "' '"};

    private String ctlLoaderType;
    private String ctlLoaderHeader;
    private String ctlLoaderCharSet;

    private PadTypes padTypeNumeric;
    private String padCharNumeric;

    private PadTypes padTypeString;
    private String padCharString;

    public static class CTLOptionsBuilder {
        private Preferences prefs = PreferencesController.getPrefs();
        private String ctlLoaderType = prefs.get(DEFAULT_LOADER_TYPE[0], DEFAULT_LOADER_TYPE[1]);
        private String ctlLoaderHeader = prefs.get(DEFAULT_LOADER_HEADER[0], DEFAULT_LOADER_HEADER[1]);
        private String ctlLoaderCharSet = prefs.get(DEFAULT_LOADER_CHARSET[0], DEFAULT_LOADER_CHARSET[1]);

        private PadTypes padTypeNumeric = PadTypes.valueOf(prefs.get(DEFAULT_PAD_TYPE_NUMERIC[0], DEFAULT_PAD_TYPE_NUMERIC[1]));
        private String padCharNumeric = prefs.get(DEFAULT_PAD_CHAR_NUMERIC[0], DEFAULT_PAD_CHAR_NUMERIC[1]);

        private PadTypes padTypeString = PadTypes.valueOf(prefs.get(DEFAULT_PAD_TYPE_STRING[0], DEFAULT_PAD_TYPE_STRING[1]));
        private String padCharString = prefs.get(DEFAULT_PAD_CHAR_STRING[0], DEFAULT_PAD_CHAR_STRING[1]);

        public CTLOptions build() {
            return new CTLOptions(this);
        }

    }

    private CTLOptions(CTLOptionsBuilder builder) {
        this.ctlLoaderType = builder.ctlLoaderType;
        this.ctlLoaderHeader = builder.ctlLoaderHeader;
        this.ctlLoaderCharSet = builder.ctlLoaderCharSet;

        this.padTypeNumeric = builder.padTypeNumeric;
        this.padCharNumeric = builder.padCharNumeric;

        this.padTypeString = builder.padTypeString;
        this.padCharString = builder.padCharString;

    }

    public String getCtlLoaderType() {
        return ctlLoaderType;
    }

    public String getCtlLoaderHeader() {
        return ctlLoaderHeader;
    }

    public String getCtlLoaderCharSet() {
        return ctlLoaderCharSet;
    }

    public PadTypes getPadTypeNumeric() {
        return padTypeNumeric;
    }

    public String getPadCharNumeric() {
        return padCharNumeric;
    }

    public PadTypes getPadTypeString() {
        return padTypeString;
    }

    public String getPadCharString() {
        return padCharString;
    }
}
