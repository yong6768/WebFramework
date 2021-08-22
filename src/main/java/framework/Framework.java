package framework;

import framework.was.EmbeddedTomcat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Framework {

    public static void run(Class<?> clazz) {
        try {
            EmbeddedTomcat tomcat = new EmbeddedTomcat();
            tomcat.start();
        } catch (Exception e) {
            log.error("Tomcat start failed", e);
        }
    }
}
