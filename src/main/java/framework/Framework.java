package framework;

import framework.bean.AnnotationBeanContainer;
import framework.bean.BeanContainer;
import framework.bean.JsonBeanContainer;
import framework.was.EmbeddedTomcat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Framework {

    public static void run(Class<?> clazz) {
        try {
//            BeanContainer beanContainer = new JsonBeanContainer("DispatcherServlet.json", "web.json");
            BeanContainer beanContainer = new AnnotationBeanContainer(Framework.class.getPackage(), clazz.getPackage());
            EmbeddedTomcat tomcat = new EmbeddedTomcat(beanContainer);
            tomcat.start();
        } catch (Exception e) {
            log.error("Tomcat start failed", e);
        }
    }
}
