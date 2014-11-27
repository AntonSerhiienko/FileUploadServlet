package com.selenium.file;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

/**
 * @author a.sergienko
 */

@WebServlet(urlPatterns = "/upload")
@MultipartConfig(fileSizeThreshold = 2, // 2 bytes
        maxFileSize = 1024 * 1024 * 10,      // 10MB
        maxRequestSize = 1024 * 1024 * 50)   // 50MB
public class FileUploadServlet extends HttpServlet {

    private static final String SAVE_DIR = "uploadFiles";

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {

        // gets absolute path of the web application
        String appPath = new File(".").getCanonicalPath();

        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + SAVE_DIR;
        makeDirIfNotThere(savePath);

        String sessionFolder = request.getParameter("session");
        if(sessionFolder != null && !sessionFolder.equalsIgnoreCase("")){
            savePath = savePath + File.separator + sessionFolder;
            makeDirIfNotThere(savePath);
        }

        java.io.PrintWriter out = response.getWriter();
        OutputStream fileWriter = null;
        InputStream filecontent = null;

        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            try {
                fileWriter = new FileOutputStream(new File(savePath + File.separator
                        + fileName));
                filecontent = part.getInputStream();
                int read = 0;
                final byte[] bytes = new byte[1024];

                while ((read = filecontent.read(bytes)) != -1) {
                    fileWriter.write(bytes, 0, read);
                }
            } catch (Exception e) {

            } finally {
                if (fileWriter != null) {
                    fileWriter.close();
                }
                if (filecontent != null) {
                    filecontent.close();
                }
            }
            out.println(savePath + File.separator + fileName);
        }
        return;
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    /**
     * Creates the save directory if it does not exists
    */
    private void makeDirIfNotThere(String path){
        File fileSaveDir = new File(path);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
    }

}
