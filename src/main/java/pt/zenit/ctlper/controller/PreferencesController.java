package pt.zenit.ctlper.controller;

import pt.zenit.ctlper.domain.CTLOptions;

import java.util.prefs.Preferences;

class PreferencesController {

    static final String DEFAULT_DRIVER = "oracle.jdbc.driver.OracleDriver";
    private static Preferences prefs;

    private PreferencesController() {}

    //TODO jsr: where dafuq do i save preferences
    static Preferences loadPreferences() {
        prefs = Preferences.userRoot().node(PreferencesController.class.getName());
        prefs.put("jdbc.driver", "oracle.jdbc.driver.OracleDriver");
        prefs.put("jdbc.conn.LOCAL", "jdbc:oracle:thin:CLICLI/CLICLI@localhost:1521/xe");
        prefs.put("jdbc.conn.GE5T01", "jdbc:oracle:thin:PIPJIPJ1/vzwwaYk2qVOFdH@127.0.0.1:6002:GE5T01");
        prefs.put("jdbc.conn.ISORPR1T", "jdbc:oracle:thin:PISORPR1/Pisorpr1C41x4_@bkoradbt01-vip.lacaixa.es:1554/ISORPR1T");

        prefs.put("ctl.load.type", "append");
        prefs.put("ctl.load.option", "OPTIONS ( BINDSIZE=512000, ROWS=10000)");
        prefs.put("ctl.load.charset", "CHARACTERSET WE8ISO8859P1");

        return prefs;
    }

    static Preferences getPrefs() {
        return prefs;
    }

    static CTLOptions buildOptions() {
        return new CTLOptions(prefs.get("ctl.load.type","append"), prefs.get("ctl.load.option", "OPTIONS ( BINDSIZE=512000, ROWS=10000)"), prefs.get("ctl.load.charset", "CHARSET WE8ISO8859P1"));
    }

}