package pt.zenit.ctlper.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.zenit.ctlper.domain.CTLOptions;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesController {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesController.class);

    static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static Preferences prefs;

    private PreferencesController() {}

    //TODO jsr: Is there a better way to store user settings?
    static Preferences loadDefaultPreferences() {
        prefs = Preferences.userRoot().node(PreferencesController.class.getName());
        prefs.put("jdbc.driver", DEFAULT_DRIVER);
        prefs.put("jdbc.conn.LOCAL", "jdbc:oracle:thin:CLICLI/CLICLI@localhost:1521/xe");
        //prefs.put("jdbc.conn.GE5T01", "jdbc:oracle:thin:PIPJIPJ1/vzwwaYk2qVOFdH@127.0.0.1:6002:GE5T01");
        //prefs.put("jdbc.conn.ISORPR1T", "jdbc:oracle:thin:PISORPR1/Pisorpr1C41x4_@bkoradbt01-vip.lacaixa.es:1554/ISORPR1T");

        return prefs;
    }

    public static Preferences getPrefs() {
        return prefs != null ? prefs : loadDefaultPreferences();
    }

    static CTLOptions buildOptions() {
        return new CTLOptions.CTLOptionsBuilder().build();
    }

    static void softReset() {
        try {
            prefs.clear();
            loadDefaultPreferences();
        } catch (BackingStoreException e) {
            LOG.error("Error clearing user prefs", e);
        }
    }
}