/*******************************************************************************
* Copyright (c) 2011 Nokia Corporation
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
* Comarch team - initial API and implementation
*******************************************************************************/
package org.ned.server.nedadminconsole.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
    final String querySingle = "SELECT * FROM containers WHERE elementid = ? ;";
    final String dbRegExp = "^[[a-z][A-Z][0-9]]*$";
    final String insertNewItem = "INSERT INTO containers ( elementid,type, name,  parentid) VALUES (?,?,?,?);";
    final String insertNewLibrary = "INSERT INTO containers ( elementid, type, name) VALUES (?,?,?);";
    final String queryLibraries = "SELECT * FROM containers WHERE parentid is NULL ;";
    
    final String updateItem = "UPDATE containers SET %1$s WHERE elementid = ?";
    final String deleteItem = "DELETE FROM containers WHERE elementid = ?";
    final String updateMedia = "UPDATE containers SET data=?, type=? WHERE elementid=?;";
    final String selectMediaType = "SELECT type FROM extensions WHERE extension=?";
    
    final String getUserList = "SELECT name, password FROM users WHERE name IN (SELECT name FROM userroles WHERE role = 'user');";
    final String deleteUser = "DELETE FROM users WHERE name = ?;";
    final String addUser = "INSERT INTO users (name, password) VALUES (?, ?);";
    final String updateUserPassword = "UPDATE users SET password = ? WHERE name = ?;";


    final String selectMotd = "SELECT message FROM motd WHERE  motd_id=1;";
    final String updateMotd = "UPDATE motd SET  message=? WHERE motd_id=1;";
    final String insertMotd = "INSERT INTO motd(motd_id, message) VALUES (1, ?)";

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

    private ResultSet getSqlResults(String request, String... parameters) throws SQLException {
        ResultSet retval = null;
        try {
            connect();
            if (sqlConnection != null) {
                PreparedStatement sqlStatement;
                sqlStatement = sqlConnection.prepareStatement(request);
                for(int i = 1; i <= parameters.length; i++)
                {
                    sqlStatement.setString(i, parameters[i-1]);
                }
                retval = sqlStatement.executeQuery();
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
        String sqlCompare = (id == null) ? "parentid is NULL" : "elementid = ?";
        ResultSet results = null;

        if (id.matches(dbRegExp)) {
            results = getSqlResults(String.format(queryAll, sqlCompare), id);
        } else {
            throw new Exception("Bad input id");
        }

        return NedObjectUtils.GenerateSortedList(results);
    }

    public NedObject getSingleElement(String id) throws Exception {
        ResultSet results = null;
        if (id.matches(dbRegExp)) {
            results = getSqlResults(querySingle, id);
            if (!results.next()) {
                throw new Exception("No results");
            }
        } else {
            throw new Exception("Bad input id");
        }
        return NedObjectUtils.GetSingleElement(results);
    }

    public void updateItem(NedObject item) throws Exception {
        StringBuilder updateSqlData = new StringBuilder();
        List<Object> queryParameters = new ArrayList<Object>();
        boolean firstParam = true;
        NedObject current = getSingleElement(item.id);
        if (!current.name.equals(item.name)) {
            updateSqlData.append("name = ?");
            queryParameters.add(item.name);
            firstParam = false;
        }
        if (item.description != null
                && !item.description.equals(current.description)) {
            if (!firstParam) {
                updateSqlData.append(" , ");
            }
            updateSqlData.append("description = ?");
            queryParameters.add(item.description);
            firstParam = false;
        }
        if (item.keywords != null
                && !java.util.Arrays.equals(current.keywords, item.keywords)) {
            if (!firstParam) {
                updateSqlData.append(" , ");
            }
            updateSqlData.append("keywords = ?");
            queryParameters.add(item.keywords);
            firstParam = false;
        }
        if (item.externalLinks != null
                && !java.util.Arrays.equals(current.externalLinks,
                        item.externalLinks)) {
            if (!firstParam) {
                updateSqlData.append(" , ");
            }
            updateSqlData.append("links = ?");
            queryParameters.add( item.externalLinks);
            firstParam = false;
        }
        connect();
        if (updateSqlData.length() > 0) {
            PreparedStatement sqlStatement = sqlConnection.prepareStatement(String.format(updateItem, updateSqlData.toString()));
            int count = 0;
            for (count =0; count < queryParameters.size(); count++) {
                if(queryParameters.get(count) instanceof String )
                {
                    sqlStatement.setString(count+1, (String) queryParameters.get(count));
                } else if(queryParameters.get(count) instanceof String[])
                {
                   Array sqlArray = sqlConnection.createArrayOf("varchar", (Object[]) queryParameters.get(count));
                    sqlStatement.setArray(count+1, sqlArray);
                }
            }
            sqlStatement.setString(count+1, item.id);
            sqlStatement.executeUpdate();
        } else {
            throw new Exception("no changes");
        }
    }

    public void saveNewItem(NedObject element) throws Exception {
         if (!element.id.matches(dbRegExp))
         {
             throw new Exception("Bad input id");
         }

        connect();

        String sqlQuery = element.parentId == null ? insertNewLibrary
                                                     :insertNewItem;
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(sqlQuery);
        sqlStatement.setString(1, element.id);
        sqlStatement.setString(2, element.type);
        sqlStatement.setString(3, element.name);
        if(element.parentId != null)
        {
            sqlStatement.setString(4, element.parentId);
        }
        sqlStatement.executeUpdate();
    }

    public void deleteItem(String itemId) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(deleteItem);
        sqlStatement.setString(1, itemId);
        sqlStatement.executeUpdate();
    }

    public void updateContentData(String mediaFile, String type,
            String contentId) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(updateMedia);
        sqlStatement.setString(1, mediaFile);
        sqlStatement.setString(2, type);
        sqlStatement.setString(3, contentId);
        sqlStatement.executeUpdate();
    }

    public String getMediaType(String extension) throws Exception {
        ResultSet result = null;
        result = getSqlResults(selectMediaType, extension.toLowerCase());
        result.next();
        return result.getString("type");
    }

    public String getMotd() throws Exception {
        ResultSet result = null;
        result = getSqlResults(selectMotd);
        result.next();
        return result.getString("message");
    }

    public void updateMotd(String message) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(updateMotd);
        try
        {
        sqlStatement.setString(1, message);
        sqlStatement.executeUpdate();
        }
        catch(SQLException ex)
        {
            Logger.getAnonymousLogger().info(ex.getMessage());
        }
    }

    public void insertMotd(String message) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(insertMotd);
        sqlStatement.setString(1, message);
        sqlStatement.executeUpdate();
    }

    public List<NedUser> getUserList() throws Exception {
        List<NedUser> retval = new LinkedList<NedUser>();
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(getUserList);
        ResultSet results = sqlStatement.executeQuery();

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
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(addUser);
        sqlStatement.setString(1, user.username);
        sqlStatement.setString(2, user.password);

        sqlStatement.executeUpdate();
    }
    
    public void deleteUser(NedUser user) throws Exception
    {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(deleteUser);
        sqlStatement.setString(1, user.username);
        sqlStatement.executeUpdate();
    }
    
    public void updateUserPassword(NedUser user) throws Exception
    {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(updateUserPassword);
        sqlStatement.setString(1, user.password);
        sqlStatement.setString(2, user.username);
        sqlStatement.executeUpdate();
    }

    public void getFullStatistics(PrintWriter writer) throws NamingException, SQLException, IOException {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement(getStatistics);
        ResultSet results = sqlStatement.executeQuery();

        CSVWriter csvWriter = new CSVWriter(writer);
        
        csvWriter.writeAll(results, true);        
     
    }

}
