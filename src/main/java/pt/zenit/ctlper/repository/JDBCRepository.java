    package pt.zenit.ctlper.repository;

    import org.slf4j.Logger;
    import org.slf4j.LoggerFactory;
    import pt.zenit.ctlper.domain.DBTable;
    import pt.zenit.ctlper.domain.DBColumn;

    import java.sql.*;
    import java.util.ArrayList;
    import java.util.Collection;
    import java.util.List;

    /**
     * Implementação JDBC de uma classe de suporte e acesso a DB
     */
    public class JDBCRepository implements Repository {

        private static final Logger LOG = LoggerFactory.getLogger(JDBCRepository.class);

        private static final String DB_INFO_QUERY = "SELECT OWNER,TABLE_NAME,STATUS FROM all_tables WHERE OWNER NOT IN ('SYS', 'MDSYS','CTXSYS','APEX_040000','XDB','SYSTEM')";
        private static final String TABLE_INFO_QUERY = "SELECT column_name,data_type,data_length,data_precision FROM all_tab_columns WHERE TABLE_NAME = ? ORDER BY COLUMN_ID";

        //Connection
        private static Connection conn = null;

        private JDBCRepository() {}


        /**
         * Trata da conexão à DB
         * @throws SQLException se n der pa se ligar for some reason
         * @throws ClassNotFoundException se n encontrar o driver Oracle
         */
        public static void connect(String jdbcDriver, String connString) throws SQLException, ClassNotFoundException {
            //Setting Oracle JDBC Driver
            try {
                Class.forName(jdbcDriver);
            } catch (ClassNotFoundException e) {
                LOG.error("I can´t seem to find your Oracle JDBC Driver... WHERE IS IT? o.o", e);
                throw e;
            }

            LOG.info("Oracle JDBC Driver Registered!");

            try {
                conn = DriverManager.getConnection(connString);
            } catch (SQLException e) {
                LOG.error("Connection Failed! Check output console" + e);
                throw e;
            }
        }

        /**
         * Fecha a conexão à DB, se estiver aberta
         * @throws SQLException aKa arrebentar tud
         */
        public static void disconnect() throws SQLException {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        }

        public static Collection<DBTable> getDBInfo() {
            List<DBTable> result= new ArrayList<>();
            if(conn != null) {
                try (PreparedStatement smt = conn.prepareStatement(DB_INFO_QUERY);
                     ResultSet rs = smt.executeQuery()) {

                    while (rs.next()) {
                        DBTable dbtable = new DBTable(rs.getString("OWNER"), rs.getString("TABLE_NAME"), rs.getString("STATUS"));
                        result.add(dbtable);
                    }

                } catch (SQLException e) {
                    LOG.error("Erro a obter os dados da DB");
                }
            }
            return result;
        }

        public static Collection<DBColumn> getTableInfo(String tableName) {
            List<DBColumn> result= new ArrayList<>();
            if(conn != null) {
                try (PreparedStatement smt = conn.prepareStatement(TABLE_INFO_QUERY)) {

                    smt.setString(1, tableName);
                    ResultSet rs = smt.executeQuery();
                    while (rs.next()) {
                        DBColumn dbTable = new DBColumn(rs.getString("COLUMN_NAME"), rs.getString("DATA_TYPE"), rs.getInt("DATA_LENGTH"), rs.getInt("DATA_PRECISION"));
                        result.add(dbTable);
                    }

                } catch (SQLException e) {
                    LOG.error("Erro a obter os dados da DB");
                }
            }
            return result;
        }
    }