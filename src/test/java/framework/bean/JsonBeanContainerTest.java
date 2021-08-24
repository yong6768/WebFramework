package framework.bean;

import framework.exception.bean.BeanNotValidException;
import framework.exception.bean.NoSuchBeanException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import web.controller.TestHelloWorldController;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonBeanContainerTest {

    @Test
    void 테스트_컨트롤러_호출() throws IOException, BeanNotValidException {
        JsonBeanContainer beanContainer = new JsonBeanContainer("beans.json");

        TestHelloWorldController helloWorldController = beanContainer.getBean("/hello-world", TestHelloWorldController.class);
        helloWorldController.helloWorld();
    }
}