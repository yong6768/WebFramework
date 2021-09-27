package framework.was;

import framework.bean.BeanContainer;
import framework.dispatcherServlet.DispatcherServlet;
import framework.servlet.handler.mvc.view.ThymeleafViewResolver;
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
    public final BeanContainer beanContainer;

    public EmbeddedTomcat(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    private static final int PORT = 8080;

    public void start() throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setBaseDir(System.getProperty("java.io.tmpdir"));
        tomcat.setPort(PORT);

        String contextPath = "/";
        String docBase = new File("./src/main/resources").getAbsolutePath();
        Context context = tomcat.addContext(contextPath, docBase);

        String servletName = "dispatcherServlet";
        String urlPattern = "/";

        tomcat.addServlet(contextPath, servletName, new DispatcherServlet(this.beanContainer, new ThymeleafViewResolver()));
        context.addServletMappingDecoded(urlPattern, servletName);

        tomcat.start();
        tomcat.getServer().await();
    }
}
