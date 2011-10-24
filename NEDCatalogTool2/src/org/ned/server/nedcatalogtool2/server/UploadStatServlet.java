package org.ned.server.nedcatalogtool2.server;

import org.ned.server.nedcatalogtool2.statistics.StatisticsReader;
import org.ned.server.nedcatalogtool2.statistics.StatisticsEntry;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import java.io.InputStream;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.ned.server.nedcatalogtool2.datasource.PostgresConnection;

/**
 *
 */
public class UploadStatServlet extends HttpServlet {

    private static final int STATSUPDATED = 0;
    private static final int NEWSTATS = 1;
    private static final int MISSINGIMEI = -1;
    private static final int SERVERINTERNALERROR = -2;
    private static final int UNKNOWN = -9999;
    private boolean serverOnWindows;

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
        PrintWriter out = response.getWriter();
        try {
        } finally {
            out.close();
        }
    }

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
        int status = UNKNOWN;
        DataOutputStream outputStream = new DataOutputStream(response.getOutputStream());
        response.setStatus(HttpServletResponse.SC_OK);
        PostgresConnection connection = new PostgresConnection();
        InputStream inputStream = null;
        try {
            LinkedList<StatisticsEntry> stats = null;
            String userName = request.getHeader("Username");
            String deviceId = request.getHeader("DeviceId");
            if (userName != null) {
                inputStream = request.getInputStream();
                stats = StatisticsReader.parseStatistics( inputStream );
                if (stats != null) {
                    connection.persistStats(stats, userName, deviceId);
                }
                status = STATSUPDATED;
            } else {
                status = MISSINGIMEI;
            }
            outputStream.writeInt(status);
            outputStream.flush();

        } catch (Exception e) {
            //e.printStackTrace(writer);
        } finally {
            inputStream.close();
            outputStream.close();
            connection.disconnect();
        }
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Upload statistics servlet";
    }
}
