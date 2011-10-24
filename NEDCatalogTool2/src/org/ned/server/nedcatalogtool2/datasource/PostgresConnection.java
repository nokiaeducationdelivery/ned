package org.ned.server.nedcatalogtool2.datasource;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import javax.naming.NamingException;
import org.ned.server.nedcatalogtool2.statistics.StatisticsEntry;

public class PostgresConnection {

    final String queryAll = "WITH RECURSIVE tree AS ( SELECT * FROM containers WHERE %1$s UNION ALL SELECT d.* FROM  containers AS d JOIN tree AS sd ON (d.parentid = sd.elementid)) SELECT * FROM tree ORDER BY id;";
    final String querySingle = "SELECT * FROM containers WHERE elementid = '%1$s' ;";
    final String dbRegExp = "^[[a-z][A-Z][0-9]]*$";
    final String insertStat = "INSERT INTO statistics( username, deviceid, type, details, time ) VALUES ('%1$s', '%2$s', '%3$s', '%4$s', '%5$s');";
    final String selectMotd = "SELECT message FROM motd WHERE  motd_id=1;";

    private Connection sqlConnection = null;
    
    private void connect() throws NamingException, SQLException  {
        if(sqlConnection == null){
            InitialContext cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/postgres");
            if (ds != null) {
                sqlConnection = ds.getConnection();
            }
        }
    }

    public void disconnect() {
        if (sqlConnection != null) {
            try {
                sqlConnection.close();
                sqlConnection = null;
            } catch (SQLException ex) {
                Logger.getLogger(PostgresConnection.class.getName()).log(
                        Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    public List<NedObject> GetFullNode(String id) throws Exception {
        String sqlCompare = (id == null)
                ? "parentid is NULL"
                : "elementid = '" + id + "'";
        ResultSet results = null;
        if (id.matches(dbRegExp)) {
            results = GetSqlResults(String.format(queryAll, sqlCompare));
        } else {
            throw new Exception("Bad input id");
        }
        return NedObject.GenerateSortedList(results);
    }

    public NedObject GetSingleElement(String id) throws Exception {
        ResultSet results = null;
        if (id.matches(dbRegExp)) {
            results = GetSqlResults(String.format(querySingle, id));
            if (!results.next()) {
                throw new Exception("No results");
            }
        } else {
            throw new Exception("Bad input id");
        }
        return NedObject.GetSingleElement(results);
    }

    private ResultSet GetSqlResults(String request) throws SQLException {
        ResultSet retval = null;
        try {
            connect();
            if (sqlConnection != null) {
                Statement sqlStatement;
                sqlStatement = sqlConnection.createStatement();
                retval = sqlStatement.executeQuery(request);
            }
        } catch (NamingException ex) {
            Logger.getLogger(PostgresConnection.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
        }
        return retval;
    }

    public void persistStats(LinkedList<StatisticsEntry> stats, String username, String deviceId ) throws SQLException {
        try {
            connect();
            Statement sqlStatement = sqlConnection.createStatement();
            for (int i = 0; i < stats.size(); i++) {
                String updateQuery = String.format( insertStat,
                                                    username,
                                                    deviceId,
                                                    stats.get(i).mEventType,
                                                    stats.get(i).mDetails,
                                                    stats.get(i).mTime );
                sqlStatement.executeUpdate(updateQuery);
            }
        } catch (NamingException ex) {
            Logger.getLogger(PostgresConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getMotd() throws Exception{
        ResultSet result = null;
        result = GetSqlResults(selectMotd);
        result.next();
        return result.getString("message");
    }
}
