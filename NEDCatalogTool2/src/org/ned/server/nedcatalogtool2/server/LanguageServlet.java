/*******************************************************************************
 * Copyright (c) 2012 Nokia Corporation
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Comarch team - initial API and implementation
 *******************************************************************************/
package org.ned.server.nedcatalogtool2.server;

import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerConfigurationException;
import org.ned.server.nedcatalogtool2.datasource.LanguageInfoSerializer;
import org.ned.server.nedcatalogtool2.datasource.NedLanguage;
import org.ned.server.nedcatalogtool2.datasource.PostgresConnection;
import org.ned.server.nedcatalogtool2.datasource.WPResSerializer;
import org.xml.sax.SAXException;

/**
 *
 */
public class LanguageServlet extends HttpServlet {

    private final static String ACTION = "action";
    private final static String LIST = "list";
    private final static String DO = "do";
    private final static String ID = "id";
    private final static String TYPE = "type";
    private final static String WP = "WP";
    private LanguageInfoSerializer serializer = new LanguageInfoSerializer();
    private WPResSerializer wpSerializer = new WPResSerializer();

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest( HttpServletRequest request,
                                   HttpServletResponse response )
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            String action = request.getParameter( ACTION );

            //invalid or unknown action
            if ( action == null || action.isEmpty() ) {
                response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
                return;
            }

            if ( action.equals( LIST ) ) {
                handleList( request, out );
                response.setContentType( "text/xml;charset=UTF-8" );
            } else if ( action.equals( DO ) ) {
                handleDo( request, response );
            } else {
                response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            }
        } catch ( Exception ex ) {
            response.setStatus( HttpServletResponse.SC_INTERNAL_SERVER_ERROR );
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet( HttpServletRequest request,
                          HttpServletResponse response )
            throws ServletException, IOException {
        processRequest( request, response );
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost( HttpServletRequest request,
                           HttpServletResponse response )
            throws ServletException, IOException {
        processRequest( request, response );
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Localization check&download servlet";
    }// </editor-fold>

    private void handleList( HttpServletRequest request, PrintWriter out ) throws SQLException, TransformerConfigurationException, SAXException {
        PostgresConnection connection = null;
        try {
            connection = new PostgresConnection();
            List<NedLanguage> list = connection.getLocalizations();
            serializer.printXml( list, out );
        } finally {
            if ( connection != null ) {
                connection.disconnect();
            }
        }
    }

    private void handleDo( HttpServletRequest request,
                           HttpServletResponse response ) throws FileNotFoundException, IOException {
        String type = request.getParameter( TYPE );
        String localeId = request.getParameter( ID );

        if ( type == null || type.isEmpty() || !type.equals( "WP" ) || localeId
                == null || localeId.isEmpty() ) {
            response.setStatus( HttpServletResponse.SC_BAD_REQUEST );
            return;
        }

        //1. do request to DB for file related to given id
        NedLanguage info = getLanguageInfo( localeId );
        //2. open and parse file
        if ( info != null ) {
            HashMap<String, String> content = loadFile( info.file );
            //3. serialize to WP7 format
            String buffer = wpSerializer.serialize( content );
            //4. serve
            response.setContentType( "text/xml;charset=UTF-8" );
            response.getWriter().print( buffer );
            
        }
    }

    private NedLanguage getLanguageInfo( String localeId ) {
        PostgresConnection connection = null;
        try {
            connection = new PostgresConnection();
            return connection.getLocalizationInfo( localeId );
        } catch ( SQLException ex ) {
            return null;
        } finally {
            if ( connection != null ) {
                connection.disconnect();
            }
        }
    }

    private HashMap<String, String> loadFile( String file ) throws FileNotFoundException, IOException {
        ServletContext context = getServletConfig().getServletContext();
        String path = context.getRealPath( "/" );

        boolean serveronwindows = !path.startsWith( "/" );

        String root = "";
        int idx = path.lastIndexOf( "NEDCatalogTool2" );
        root = path.substring( 0, idx );
        root += "ROOT\\";

        String directory = root + "locales\\";

        if ( !serveronwindows ) {
            directory = directory.replace( '\\', '/' );
        }

        FileReader locale = new FileReader( directory + file );
        BufferedReader buf = new BufferedReader( locale );

        String line = null;
        HashMap<String, String> tokens = new HashMap<String, String>( 200 );
        while ( (line = buf.readLine()) != null ) {
            if ( !line.startsWith( "#" ) ) {
                String[] entry = line.split( "=" );
                if ( entry.length >= 2 ) {
                    tokens.put( entry[0], entry[1] );
                }

            }
        }
        return tokens;
    }
}
