package framework.bean;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
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
    void 테스트_컨트롤러_호출() throws IOException, BeanNotValidException, NoSuchBeanException {
        JsonBeanContainer beanContainer = new JsonBeanContainer("beans.json");

        TestHelloWorldController helloWorldController = beanContainer.getBean("/hello-world", TestHelloWorldController.class);
        helloWorldController.helloWorld();
    }

    @Test
    void 빈_이름_오류_검증() {
        assertThrows(
                BeanNotValidException.class,
                () -> new JsonBeanContainer("notValidBeanName.json")
        );
    }

    @Test
    void Json_파일_오류_검증() {
        assertThrows(
                UnrecognizedPropertyException.class,
                () -> new JsonBeanContainer("notValidJsonFile.json")
        );
    }
}