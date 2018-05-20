package pt.zenit.oracle.ctlfx.repository;

import java.sql.SQLException;

/**
 * Simple Repo Interface
 */
public interface Repository {

    /**
     * @param jdbcDriver
     * @param connString
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    static void connect(String jdbcDriver, String connString) throws SQLException, ClassNotFoundException {}

    /**
     * @throws SQLException
     */
    static void disconnect()  throws SQLException {}
}
