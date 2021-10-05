package framework.bean;

import framework.Framework;
import framework.exception.bean.BeansException;
import org.junit.jupiter.api.Test;
import web.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

class AnnotationBeanContainerTest {

    @Test
    void initTest() throws BeansException, InvocationTargetException, IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        BeanContainer beanContainer = new AnnotationBeanContainer(Framework.class.getPackage(), Application.class.getPackage());

        String[] beanNames = beanContainer.getBeanNames();
        for (String beanName : beanNames) {
            System.out.println("beanName = " + beanName);
        }
    }
}