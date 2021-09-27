package framework.bean;

import framework.Framework;
import framework.exception.bean.BeanNotValidException;
import framework.exception.bean.BeanStoreException;
import org.junit.jupiter.api.Test;
import web.Application;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationBeanContainerTest {

    @Test
    void initTest() throws BeanNotValidException, InvocationTargetException, IOException, InstantiationException, IllegalAccessException, BeanStoreException, ClassNotFoundException {
        BeanContainer beanContainer = new AnnotationBeanContainer(Framework.class.getPackage(), Application.class.getPackage());

        String[] beanNames = beanContainer.getBeanNames();
        for (String beanName : beanNames) {
            System.out.println("beanName = " + beanName);
        }
    }
}