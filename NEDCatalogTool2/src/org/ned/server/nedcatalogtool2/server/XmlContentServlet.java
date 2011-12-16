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
package org.ned.server.nedcatalogtool2.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ned.server.nedcatalogtool2.datasource.ContentXmlGenerator;
import org.ned.server.nedcatalogtool2.datasource.NedObject;
import org.ned.server.nedcatalogtool2.datasource.PostgresConnection;
import org.ned.server.nedcatalogtool2.datasource.StringRepository;

public class XmlContentServlet extends HttpServlet {

    private ContentXmlGenerator generator = new ContentXmlGenerator();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String id = request.getHeader("id");
        String nonRecursive = request.getHeader(StringRepository.HEADER_NONRECURSIVE);
        if (id == null) { // TODO remove later
            id = request.getParameter("id");
        }  //
        if (nonRecursive == null) { // TODO remove later
            nonRecursive = request.getParameter(StringRepository.HEADER_NONRECURSIVE);
        }  //


        PrintWriter out = response.getWriter();
        try {
            if (nonRecursive != null) {
                if(!SetResponseHeaders(id, response)){
                    response.sendError(HttpServletResponse.SC_NO_CONTENT);
                }
            } else {
                if(SetVersionHeader(id, response)){
                    generator.printContentXml(id, out);
                }else{
                    response.sendError(HttpServletResponse.SC_NO_CONTENT);
                }
            }
        } catch (Exception e) {
            e.printStackTrace(out);
        } finally {
            out.close();
        }
    }

    private boolean SetResponseHeaders(String id, HttpServletResponse response) {
        PostgresConnection dbConnection = new PostgresConnection();
        boolean success = true;
        try {
            NedObject result = dbConnection.GetSingleElement(id);
            if(result != null){
                response.setHeader("Title", result.name);
                response.setHeader("Type", result.type);
                response.setHeader("Version", result.version);
            }else{
                success = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(XmlContentServlet.class.getName()).log(
                    Level.SEVERE, ex.getMessage(), ex);
            success = false;
        } finally {
            dbConnection.disconnect();
        }
        return success;
    }

    private boolean SetVersionHeader(String id, HttpServletResponse response) {
        PostgresConnection dbConnection = new PostgresConnection();
        boolean success = true;
        try {
            NedObject result = dbConnection.GetSingleElement(id);

            if(result != null){
                response.setHeader("Version", result.version);
            }else{
                success = false;
            }
        } catch (Exception ex) {
            Logger.getLogger(XmlContentServlet.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            dbConnection.disconnect();
        }
        return success;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "XmlContentServlet";
    }// </editor-fold>
}
