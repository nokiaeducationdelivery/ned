package org.ned.server.nedadminconsole.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

public class PostgresConnection {

    final String queryAll = "WITH RECURSIVE tree AS ( SELECT * FROM containers WHERE %1$s UNION ALL SELECT d.* FROM  containers AS d JOIN tree AS sd ON (d.parentid = sd.elementid)) SELECT * FROM tree ORDER BY id;";
    final String querySingle = "SELECT * FROM containers WHERE elementid = '%1$s' ;";
    final String dbRegExp = "^[[a-z][A-Z][0-9]]*$";
    final String insertStat = "INSERT INTO statistics( username, contentid, type, data) VALUES ('%1$s', '%2$s', '%3$s', '%4$s');";
    final String insertNewItem = "INSERT INTO containers ( elementid, parentid, type, name) VALUES ('%1$s', '%2$s', '%3$s', '%4$s');";
    final String insertNewLibrary = "INSERT INTO containers ( elementid, type, name) VALUES ('%1$s', '%2$s', '%3$s');";
    final String queryLibraries = "SELECT * FROM containers WHERE parentid is NULL ;";
    
    final String updateItem = "UPDATE containers SET %1$s WHERE elementid = '%2$s'";
    final String deleteItem = "DELETE FROM containers WHERE elementid = '%1$s'";
    final String updateMedia = "UPDATE containers SET data='%1$s', type='%2$s' WHERE elementid='%3$s';";
    final String selectMediaType = "SELECT type FROM extensions WHERE extension='%1$s'";
    
    final String getUserList = "SELECT name, password FROM users WHERE name IN (SELECT name FROM userroles WHERE role = 'user');";
    final String deleteUser = "DELETE FROM users WHERE name = '%1$s';";
    final String addUser = "INSERT INTO users (name, password) VALUES ('%1$s', '%2$s');";
    final String updateUserPassword = "UPDATE users SET password = '%1$s' WHERE name = '%2$s';";


    final String selectMotd = "SELECT message FROM motd WHERE  motd_id=1;";
    final String updateMotd = "UPDATE motd SET  message='%1$s' WHERE motd_id=1;";
    final String insertMotd = "INSERT INTO motd(motd_id, message) VALUES (1, '%1$s')";

    final String getStatistics = "SELECT * FROM statistics";
    
    private Connection sqlConnection = null;

    public void connect() throws NamingException, SQLException {
        if (sqlConnection == null) {
            InitialContext cxt = new InitialContext();
            DataSource ds = (DataSource) cxt
                    .lookup("java:/comp/env/jdbc/postgres");
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

    private ResultSet getSqlResults(String request) throws SQLException {
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

    public List<NedObject> getLibraries() throws Exception {
        List<NedObject> retval = new LinkedList<NedObject>();
        ResultSet sqlResults = getSqlResults(queryLibraries);
        while (sqlResults.next()) {
            retval.add(NedObjectUtils.GetSingleElement(sqlResults));
        }
        return retval;
    }

    public List<NedObject> GetFullNode(String id) throws Exception {
        String sqlCompare = (id == null) ? "parentid is NULL" : "elementid = '"
                + id + "'";
        ResultSet results = null;

        if (id.matches(dbRegExp)) {
            results = getSqlResults(String.format(queryAll, sqlCompare));
        } else {
            throw new Exception("Bad input id");
        }

        return NedObjectUtils.GenerateSortedList(results);
    }

    public NedObject getSingleElement(String id) throws Exception {
        ResultSet results = null;
        if (id.matches(dbRegExp)) {
            results = getSqlResults(String.format(querySingle, id));
            if (!results.next()) {
                throw new Exception("No results");
            }
        } else {
            throw new Exception("Bad input id");
        }
        return NedObjectUtils.GetSingleElement(results);
    }

    public void updateItem(NedObject item) throws Exception {
        String updateSqlData = "";
        boolean firstParam = true;
        NedObject current = getSingleElement(item.id);
        if (!current.name.equals(item.name)) {
            updateSqlData += " name = '" + item.name + "'";
            firstParam = false;
        }
        if (item.description != null
                && !item.description.equals(current.description)) {
            if (!firstParam) {
                updateSqlData += " , ";
            }
            updateSqlData += "description = '" + item.description + "'";
            firstParam = false;
        }
        if (item.keywords != null
                && !java.util.Arrays.equals(current.keywords, item.keywords)) {
            if (!firstParam) {
                updateSqlData += " , ";
            }
            updateSqlData += "keywords = '"
                    + createSqlArrayString(item.keywords) + "'";
            firstParam = false;
        }
        if (item.externalLinks != null
                && !java.util.Arrays.equals(current.externalLinks,
                        item.externalLinks)) {
            if (!firstParam) {
                updateSqlData += " , ";
            }
            updateSqlData += "links = '"
                    + createSqlArrayString(item.externalLinks) + "'";
            firstParam = false;
        }
        connect();
        if (sqlConnection != null && !updateSqlData.isEmpty()) {
            Statement sqlStatement;
            sqlStatement = sqlConnection.createStatement();
            sqlStatement.executeUpdate(String.format(updateItem, updateSqlData,
                    item.id));
        } else {
            throw new Exception("no changes");
        }
    }

    private String createSqlArrayString(String[] array) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        for (int i = 0; i < array.length; i++) {
            builder.append(array[i]);
            if (i < array.length - 1) {
                builder.append(",");
            }
        }
        builder.append("}");
        return builder.toString();
    }

    public void saveNewItem(NedObject element) throws Exception {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        String sqlQuery = element.parentId == null ? String.format(
                insertNewLibrary, element.id, element.type, element.name)
                : String.format(insertNewItem, element.id, element.parentId,
                        element.type, element.name);
        sqlStatement.executeUpdate(sqlQuery);
    }

    public void deleteItem(String itemId) throws Exception {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        String sqlQuery = String.format(deleteItem, itemId);
        sqlStatement.executeUpdate(sqlQuery);
    }

    public void updateContentData(String mediaFile, String type,
            String contentId) throws Exception {
        connect();
        String query = String.format(updateMedia, mediaFile, type,
                contentId);
        Statement sqlStatement = sqlConnection.createStatement();
        sqlStatement.executeUpdate(query);
    }

    public String getMediaType(String extension) throws Exception {
        ResultSet result = null;
        connect();
        String query = String.format(selectMediaType, extension.toLowerCase());
        result = getSqlResults(query);
        result.next();
        return result.getString("type");
    }

    public String getMotd() throws Exception {
        ResultSet result = null;
        connect();
        result = getSqlResults(selectMotd);
        result.next();
        return result.getString("message");
    }

    public void updateMotd(String message) throws Exception {
        connect();
        String query = String.format(updateMotd, message);
        Statement sqlStatement = sqlConnection.createStatement();
        sqlStatement.executeUpdate(query);
    }

    public void insertMotd(String message) throws Exception {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        String sqlQuery = String.format(insertMotd, message);

        sqlStatement.executeUpdate(sqlQuery);
    }

    public List<NedUser> getUserList() throws Exception {
        List<NedUser> retval = new LinkedList<NedUser>();
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        ResultSet results = sqlStatement.executeQuery(getUserList);

        while (results.next()) {
            NedUser user = new NedUser(results.getString(1),
                    results.getString(2), NedUser.DATABASE_PRESENT);
            retval.add(user);
        }
        return retval;
    }
    
    public void addUser(NedUser user) throws Exception
    {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        sqlStatement.executeUpdate(String.format(addUser, user.username, user.password));
    }
    
    public void deleteUser(NedUser user) throws Exception
    {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        sqlStatement.executeUpdate(String.format(deleteUser, user.username));
    }
    
    public void updateUserPassword(NedUser user) throws Exception
    {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        sqlStatement.executeUpdate(String.format(updateUserPassword, user.password, user.username));
    }

    public void getFullStatistics(PrintWriter writer) throws NamingException, SQLException, IOException {
        connect();
        Statement sqlStatement = sqlConnection.createStatement();
        ResultSet results = sqlStatement.executeQuery(getStatistics);

        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeAll(results, true);        
     
    }

}
