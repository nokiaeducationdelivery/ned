/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ned.server.nedcatalogtool2.server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ned.server.nedcatalogtool2.datasource.PostgresConnection;

/**
 *
 * @author damian.janicki
 */
public class MotdServlet extends HttpServlet{


    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        String message = null;
        PostgresConnection dbConnection = new PostgresConnection();
        try{
            message = dbConnection.getMotd();
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            dbConnection.disconnect();

        }

        if(message == null ){
            message = "";
        }


        PrintWriter out = response.getWriter();

        try{
            out.print(message);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            out.close();
        }
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Ned message of the day update servlet";
    }// </editor-fold>

}
