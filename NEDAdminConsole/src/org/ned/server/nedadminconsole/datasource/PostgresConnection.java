/*******************************************************************************
 * Copyright (c) 2011-2012 Nokia Corporation All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.server.nedadminconsole.datasource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.ned.server.nedadminconsole.shared.NedLanguage;
import org.ned.server.nedadminconsole.shared.NedObject;
import org.ned.server.nedadminconsole.shared.NedUser;

import com.google.appengine.repackaged.org.joda.time.DateTime;
import com.google.appengine.repackaged.org.joda.time.format.DateTimeFormatter;
import com.google.appengine.repackaged.org.joda.time.format.ISODateTimeFormat;

public class PostgresConnection {

    final String queryAll = "WITH RECURSIVE tree AS ( SELECT * FROM containers WHERE %1$s UNION ALL SELECT d.* FROM  containers AS d JOIN tree AS sd ON (d.parentid = sd.elementid)) SELECT * FROM tree ORDER BY index;";
    final String querySingle = "SELECT * FROM containers WHERE elementid = ? ;";
    final String dbRegExp = "^[[a-z][A-Z][0-9]]*$";
    final String insertNewItem = "INSERT INTO containers ( elementid,type, name, parentid, index) VALUES (?,?,?,?,?);";
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

    final String getLanguages = "SELECT name, locale_string, translation_file FROM languages ;";
    final String addLanguage = "INSERT INTO languages(locale_string, name, translation_file) VALUES(?, ?, ?) ;";

    final String getStatistics = "SELECT * FROM statistics";

    private Connection sqlConnection = null;

    public void connect() throws NamingException, SQLException {
        if( sqlConnection == null ) {
            InitialContext cxt = new InitialContext();
            DataSource ds = (DataSource) cxt.lookup( "java:/comp/env/jdbc/postgres" );
            if( ds != null ) {
                sqlConnection = ds.getConnection();
            }
        }
    }

    public void disconnect() {
        if( sqlConnection != null ) {
            try {
                sqlConnection.close();
                sqlConnection = null;
            } catch( SQLException ex ) {
                Logger.getLogger( PostgresConnection.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
            }
        }
    }

    private ResultSet getSqlResults( String request, String... parameters ) throws SQLException {
        ResultSet retval = null;
        try {
            connect();
            if( sqlConnection != null ) {
                PreparedStatement sqlStatement;
                sqlStatement = sqlConnection.prepareStatement( request );
                for( int i = 1; i <= parameters.length; i++ ) {
                    sqlStatement.setString( i, parameters[i - 1] );
                }
                retval = sqlStatement.executeQuery();
            }
        } catch( NamingException ex ) {
            Logger.getLogger( PostgresConnection.class.getName() ).log( Level.SEVERE, ex.getMessage(), ex );
        }
        return retval;
    }

    public List<NedObject> getLibraries() throws Exception {
        List<NedObject> retval = new LinkedList<NedObject>();
        ResultSet sqlResults = getSqlResults( queryLibraries );
        while( sqlResults.next() ) {
            retval.add( NedObjectUtils.GetSingleElement( sqlResults ) );
        }
        return retval;
    }

	public NedObject GetFullNode(String id) throws Exception {
        String sqlCompare = (id == null) ? "parentid is NULL" : "elementid = ?";
        ResultSet results = null;

        if (id.matches(dbRegExp)) {
            results = getSqlResults(String.format(queryAll, sqlCompare), id);
        } else {
            throw new Exception("Bad input id");
        }

        NedObject library = NedObjectUtils.GenerateSortedList(results, id);
        if( NedObjectUtils.updateIndexes( library ) ){
        	updateFullNode( library );
        }
        
        return library;
    }
	
	public void updateFullNode( NedObject object )throws Exception{
		for( NedObject child : object.childes ){
			updateFullNode( child );
		}
		updateItem( object );
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
        return NedObjectUtils.GetSingleElement( results );
    }

    public void updateItem( NedObject item ) throws Exception {
        StringBuilder updateSqlData = new StringBuilder();
        List<Object> queryParameters = new ArrayList<Object>();
        boolean firstParam = true;
        NedObject current = getSingleElement( item.id );
        if( !current.name.equals( item.name ) ) {
            updateSqlData.append( "name = ?" );
            queryParameters.add( item.name );
            firstParam = false;
        }
        if( item.description != null && !item.description.equals( current.description ) ) {
            if( !firstParam ) {
                updateSqlData.append( " , " );
            }
            updateSqlData.append( "description = ?" );
            queryParameters.add( item.description );
            firstParam = false;
        }
        if( item.keywords != null && !java.util.Arrays.equals( current.keywords, item.keywords ) ) {
            if( !firstParam ) {
                updateSqlData.append( " , " );
            }
            updateSqlData.append( "keywords = ?" );
            queryParameters.add( item.keywords );
            firstParam = false;
        }
        if( item.externalLinks != null && !java.util.Arrays.equals( current.externalLinks, item.externalLinks ) ) {
            if( !firstParam ) {
                updateSqlData.append( " , " );
            }
            updateSqlData.append( "links = ?" );
            queryParameters.add( item.externalLinks );
            firstParam = false;
        }
        
        if( !firstParam ){
        	updateSqlData.append(" , ");
        }
        updateSqlData.append("index = ?");
 
        connect();
        if( updateSqlData.length() > 0 ) {
            PreparedStatement sqlStatement = sqlConnection.prepareStatement( String.format( updateItem,
                    updateSqlData.toString() ) );
            int count = 0;
            for( count = 0; count < queryParameters.size(); count++ ) {
                if( queryParameters.get( count ) instanceof String ) {
                    sqlStatement.setString( count + 1, (String) queryParameters.get( count ) );
                } else if( queryParameters.get( count ) instanceof String[] ) {
                    Array sqlArray = sqlConnection.createArrayOf( "varchar", (Object[]) queryParameters.get( count ) );
                    sqlStatement.setArray( count + 1, sqlArray );
                }
            }
            
            sqlStatement.setInt(count + 1, item.index);
            sqlStatement.setString(count+2, item.id);
            
            sqlStatement.executeUpdate();
        } else {
            throw new Exception( "no changes" );
        }
    }

    public void saveNewItem( NedObject element ) throws Exception {
        if( !element.id.matches( dbRegExp ) ) {
            throw new Exception( "Bad input id" );
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
            sqlStatement.setInt(5, element.index);
        }
        sqlStatement.executeUpdate();
    }

    public void deleteItem( String itemId ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( deleteItem );
        sqlStatement.setString( 1, itemId );
        sqlStatement.executeUpdate();
    }

    public void updateContentData( String mediaFile, String type, String contentId ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( updateMedia );
        sqlStatement.setString( 1, mediaFile );
        sqlStatement.setString( 2, type );
        sqlStatement.setString( 3, contentId );
        sqlStatement.executeUpdate();
    }

    public String getMediaType( String extension ) throws Exception {
        ResultSet result = null;
        result = getSqlResults( selectMediaType, extension.toLowerCase() );
        result.next();
        return result.getString( "type" );
    }

    public String getMotd() throws Exception {
        ResultSet result = null;
        result = getSqlResults( selectMotd );
        result.next();
        return result.getString( "message" );
    }

    public void updateMotd( String message ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( updateMotd );
        try {
            sqlStatement.setString( 1, message );
            sqlStatement.executeUpdate();
        } catch( SQLException ex ) {
            Logger.getAnonymousLogger().info( ex.getMessage() );
        }
    }

    public void insertMotd( String message ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( insertMotd );
        sqlStatement.setString( 1, message );
        sqlStatement.executeUpdate();
    }

    public List<NedUser> getUserList() throws Exception {
        List<NedUser> retval = new LinkedList<NedUser>();
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( getUserList );
        ResultSet results = sqlStatement.executeQuery();

        while( results.next() ) {
            NedUser user = new NedUser( results.getString( 1 ), results.getString( 2 ), NedUser.DATABASE_PRESENT );
            retval.add( user );
        }
        return retval;
    }

    public void addUser( NedUser user ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( addUser );
        sqlStatement.setString( 1, user.username );
        sqlStatement.setString( 2, user.password );

        sqlStatement.executeUpdate();
    }

    public void deleteUser( NedUser user ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( deleteUser );
        sqlStatement.setString( 1, user.username );
        sqlStatement.executeUpdate();
    }

    public void updateUserPassword( NedUser user ) throws Exception {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( updateUserPassword );
        sqlStatement.setString( 1, user.password );
        sqlStatement.setString( 2, user.username );
        sqlStatement.executeUpdate();
    }

    public void getFullStatistics( PrintWriter writer ) throws NamingException, SQLException, IOException {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( getStatistics );
        ResultSet results = sqlStatement.executeQuery();
        List<String[]> res = toArray( results );

        CSVWriter csvWriter = new CSVWriter( writer );

        csvWriter.writeAll( res );

    }

    private List<String[]> toArray( ResultSet results ) throws SQLException {
        ResultSetHelperService helper = new ResultSetHelperService();
        List<String[]> resultArray = new LinkedList<String[]>();

        resultArray.add( helper.getColumnNames( results ) );
        while( results.next() ) {
            try {
                resultArray.add( helper.getColumnValues( results ) );
            } catch( IOException e ) {
                // ignore
            }
        }

        String[] columnNames = helper.getColumnNames( results );
        int timeColNo = -1;
        for( int i = 0; i < columnNames.length; i++ ) {
            if( columnNames[i].equals( "time" ) ) {
                timeColNo = i;
                break;
            }
        }

        if( timeColNo != -1 ) {
            for( String[] row : resultArray ) {
                row[timeColNo] = covertDataTime( row[timeColNo] );
            }
        }

        return resultArray;
    }

    private String covertDataTime( String aInputDate ) {
        final String excelFormat = "%1$tY-%1$tm-%1$td %1$tT";
        Calendar cal;
        try {
            DateTimeFormatter parser2 = ISODateTimeFormat.dateHourMinuteSecondFraction();// yyyy-MM-dd'T'HH:mm:ss.SSS
            DateTime date = parser2.parseDateTime( aInputDate );
            cal = date.toCalendar( Locale.ENGLISH );
            return String.format( excelFormat , cal );
        } catch( Exception ex ) {
        }

        try {
            DateTimeFormatter parser2 = ISODateTimeFormat.dateTime();// yyyy-MM-dd'T'HH:mm:ss.SSSZ
            DateTime date = parser2.parseDateTime( aInputDate );
            cal = date.toCalendar( Locale.ENGLISH );
            return String.format( excelFormat, cal );
        } catch( Exception ex ) {
        }

        Date dateobj;
        cal = Calendar.getInstance( new SimpleTimeZone( 0, "GMT" ) );

        try {
            dateobj = new SimpleDateFormat( "EEE MMM dd HH:mm:ss zz yyyy", Locale.ENGLISH ).parse( aInputDate );
            cal.setTime( dateobj );
            return String.format( excelFormat, cal );
        } catch( ParseException e1 ) {

        }
        System.out.print( aInputDate );

        return aInputDate;
    }

    public List<NedLanguage> getLanguageList() throws Exception {
        List<NedLanguage> retval = new LinkedList<NedLanguage>();
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( getLanguages );
        ResultSet results = sqlStatement.executeQuery();

        while( results.next() ) {
            NedLanguage language = new NedLanguage( results.getString( 1 ), results.getString( 2 ),
                    results.getString( 3 ) );
            retval.add( language );
        }
        return retval;
    }

    public void uploadNewLanguage( String finalFileName, String languageName, String languageLocale )
            throws NamingException, SQLException {
        connect();
        PreparedStatement sqlStatement = sqlConnection.prepareStatement( addLanguage );
        sqlStatement.setString( 1, languageLocale );
        sqlStatement.setString( 2, languageName );
        sqlStatement.setString( 3, finalFileName );
        sqlStatement.executeUpdate();
    }

}
