package pt.zenit.ctlper.repository;

import java.sql.SQLException;

/**
 * Interface de management de repositorio
 */
public interface Repository {

    static void connect(String jdbcDriver, String connString) throws SQLException, ClassNotFoundException {}

    static void disconnect()  throws SQLException {}
}
