package com.selenium.file;

import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

public class Main {
    public static void main(String[] args) throws Exception {

        String portProp = System.getProperty("port");
        int port;

        if( portProp==null || portProp.equalsIgnoreCase("")){
            port = 8090;
        }
        else {
            port = Integer.parseInt(portProp);
        }

        Server httpServer = new Server(port);
        WebAppContext webAppContext = new WebAppContext();

        webAppContext.setContextPath("/");
        webAppContext.setBaseResource(Resource.newClassPathResource("."));
        webAppContext.addServlet(FileUploadServlet.class.getName(), "/upload");
        webAppContext.setConfigurations(new Configuration[]{new WebXmlConfiguration(),new AnnotationConfiguration()});

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{webAppContext, new DefaultHandler()});
        httpServer.setHandler(handlers);
        httpServer.start();
    }
}
