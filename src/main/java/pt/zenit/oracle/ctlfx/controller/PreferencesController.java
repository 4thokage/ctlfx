package pt.zenit.oracle.ctlfx.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class PreferencesController {

    private static final Logger LOG = LoggerFactory.getLogger(PreferencesController.class);

    static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static Preferences prefs;

    private PreferencesController() {}

    static Preferences loadDefaultPreferences() {
        prefs = Preferences.userRoot().node(PreferencesController.class.getName());
        prefs.put("jdbc.driver", DEFAULT_DRIVER);
        prefs.put("pad.char.numeric", "'0'");
        prefs.put("pad.type.numeric", "LPAD");
        prefs.put("pad.char.string", "' '");
        prefs.put("pad.type.string", "RPAD");

        return prefs;
    }

    static Preferences getPrefs() {
        return prefs != null ? prefs : loadDefaultPreferences();
    }

    static void saveSettings(String padCharNumeric, String padCharString, String padTypeNumeric, String padTypeString) {
        prefs.put("pad.char.numeric", padCharNumeric);
        prefs.put("pad.type.numeric", padTypeNumeric);
        prefs.put("pad.char.string", padCharString);
        prefs.put("pad.type.string", padTypeString);
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
