package framework.was;

import framework.bean.BeanContainer;
import framework.dispatcherServlet.DispatcherServlet;
import framework.exception.bean.BeansException;
import framework.servlet.handler.mvc.view.ThymeleafViewResolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

@Slf4j
public class EmbeddedTomcat {
    public final BeanContainer beanContainer;

    public EmbeddedTomcat(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    private static final int PORT = 8080;

    public void start() throws LifecycleException, BeansException {
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
