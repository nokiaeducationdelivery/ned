package org.ned.server.nedadminconsole.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ned.server.nedadminconsole.datasource.PostgresConnection;
import org.ned.server.nedadminconsole.shared.NedServerResponses;

public class NedFileUploadServlet extends HttpServlet {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private PostgresConnection connection = null;

    private PostgresConnection getPosgresConnection() {
        if (connection == null) {
            connection = new PostgresConnection();
        }
        return connection;
    }

    private void disconnectPostgres() {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("text/html");
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (!isMultipart) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(
                    NedServerResponses.ERROR_MULTIPART_CONTENT);
            resp.flushBuffer();
            return; // send response
        }      
        
        FileItemFactory factory = new DiskFileItemFactory();
        List<?> items = null;
        Iterator<?> iter = null;
        FileItem item = null;
        String name = null;
        String libraryId = null;
        String contentId = null;
        String file = null;
        String contentType = null;
        File uploadedFile = null;

        ServletFileUpload upload = new ServletFileUpload(factory);
        try {
            items = upload.parseRequest(req);
        } catch (FileUploadException ex) {
            ex.printStackTrace();
        }

        // now the downloading begins
        if (items == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().print(NedServerResponses.ERROR_BAD_REQUEST);
            resp.flushBuffer();
            return;
        }
        
        // first get only parameters
            iter = items.iterator();
            while (iter.hasNext()) {
                item = (FileItem) iter.next();

                if (item.getFieldName().equals("libId")) {
                    libraryId = item.getString();
                } else if (item.getFieldName().equals("contentId")) {
                    contentId = item.getString();
                }
            }

        iter = items.iterator();
        while (iter.hasNext()) {
            item = (FileItem) iter.next();
            if (!item.isFormField()) {
                name = item.getName();
                int slashindex = name.lastIndexOf('\\');
                if (slashindex > -1) {
                    file = name.substring(slashindex + 1, name.length());
                } else {
                    file = name.substring(name.lastIndexOf('/') + 1,
                            name.length());
                }
                
                contentType = getMediaType(name);
                if (contentType == null || contentType.isEmpty() ) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    resp.getWriter().print(
                            NedServerResponses.ERROR_WRONG_FILE_TYPE);
                    resp.flushBuffer();
                    disconnectPostgres();
                    return;
                }

                // FILE PATH CONSISTS OF
                // ROOT - TOMCAT_PATH\webapps\ROOT
                // BASEROOT - PASSED FROM CLIENT - librartId(signifies the catalogue instance)
                // nokiaecd\videos
                // FILENAME - GET FROM CHOSEN FILE

                String directory = createDirectory(libraryId);
                
                File fDir = new File(directory);
                fDir.getAbsolutePath();
                if (!fDir.exists()) {
                    fDir.mkdirs();
                }

                file = createFilePath(directory, file);
                String uf = directory + file;
                uploadedFile = new File(uf);
                
                // //////////////////////////////////////////////
                InputStream uploadedStream = item.getInputStream();
                FileOutputStream fos = new FileOutputStream(uploadedFile);
                byte[] myarray = new byte[1024];
                int i = 0;
                while ((i = uploadedStream.read(myarray)) != -1) {
                    fos.write(myarray, 0, i);
                }
                fos.flush();
                fos.close();
                uploadedStream.close();

                // update database
                try {
                    getPosgresConnection().updateContentData(file,
                            contentType, contentId);
                } catch (Exception ex) {
                    // TODO delete file
                    Logger.getLogger(NedFileUploadServlet.class.getSimpleName()).log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                    resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    resp.getWriter().print(
                            NedServerResponses.ERROR_DATABASE_UPDATE);
                    resp.flushBuffer();
                    disconnectPostgres();
                    return;
                }
            }
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.getWriter().print(contentType);
        resp.flushBuffer();
        disconnectPostgres();
    }

    private String createDirectory(String libId){ 
        ServletContext context = getServletConfig().getServletContext();
        String path = context.getRealPath("/");
        
        boolean serveronwindows = !path.startsWith("/");
        
        String root = "";
        int idx = path.lastIndexOf("NEDAdminConsole");
        root = path.substring(0, idx);
        root += "ROOT\\";     
        
        String directory = root + libId + "\\nokiaecd\\videos\\";
        
        if (!serveronwindows) {
            directory = directory.replace('\\', '/');
        }
        
        return directory;
    }
    
    /**
     * if file exist in directory, method changes file name
     * 
     * @return
     */
    private String createFilePath(String dir, String fileName) {

        int nextFileIdx = 1;
        int idx = fileName.indexOf(".");
        String name = fileName.substring(0, idx);
        String ext = fileName.substring(idx);

        String newFileName = fileName;

        while (new File(dir + newFileName).exists()) {
            newFileName = name + "_" + nextFileIdx + ext;
            nextFileIdx++;
        }

        return newFileName;
    }

    private String getExtension(String file) {
        int idx = file.indexOf(".");
        if (idx < file.length()) {
            return file.substring(idx + 1);
        }
        return "";
    }

    private String getMediaType(String fileName) {
        String retval = null;
        try {
            retval = getPosgresConnection().getMediaType(getExtension(fileName));
        } catch (Exception ex) {
        }
        return retval;
    }

}
