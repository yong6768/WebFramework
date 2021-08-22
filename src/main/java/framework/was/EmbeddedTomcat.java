package framework.was;

import framework.dispatcherServlet.DispatcherServlet;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class EmbeddedTomcat {

    private static final int PORT = 8080;

    public void start() throws LifecycleException {
//        Tomcat tomcat = new Tomcat();
//        tomcat.setPort(PORT);
//        tomcat.setBaseDir("temp");
//
//        String contextPath = "/";
//        String docBase = new File(".").getAbsolutePath();
//        Context context = tomcat.addContext(contextPath, docBase);
//
//        String urlPattern = "/";
//        String servletName = "dispatcher";
//        tomcat.addServlet(urlPattern, servletName, new DispatcherServlet());
//        context.addServletMappingDecoded("/*", servletName);
//
//
//
////
////        Connector connector = tomcat.getConnector();
////        connector.setURIEncoding("UTF-8");
////        tomcat.addWebapp("/", new File("/").getAbsolutePath());
////
//
//
//
//        log.info("Tomcat started, Port: {}", PORT);
//
//
//        tomcat.start();
//        tomcat.getServer().await();

        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir("temp");
        tomcat.setPort(8080);

        String contextPath = "/";
        String docBase = new File(".").getAbsolutePath();

        Context context = tomcat.addContext(contextPath, docBase);

        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                PrintWriter writer = resp.getWriter();

                writer.println("<html><title>Welcome</title><body>");
                writer.println("<h1>Have a Great Day!</h1>");
                writer.println("</body></html>");
            }
        };

        String servletName = "Servlet1";
        String urlPattern = "/go";

        tomcat.addServlet(contextPath, servletName, new DispatcherServlet());
        context.addServletMappingDecoded(urlPattern, servletName);

        tomcat.start();
        tomcat.getServer().await();
    }
}
