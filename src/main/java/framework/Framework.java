package framework;

import framework.bean.BeanContainer;
import framework.bean.JsonBeanContainer;
import framework.was.EmbeddedTomcat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Framework {

    public static void run(Class<?> clazz) {
        try {
            BeanContainer beanContainer = new JsonBeanContainer("DispatcherServlet.json", "web.json");
            EmbeddedTomcat tomcat = new EmbeddedTomcat(beanContainer);
            tomcat.start();
        } catch (Exception e) {
            log.error("Tomcat start failed", e);
        }
    }
}
