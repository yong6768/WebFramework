package framework.bean;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import framework.exception.bean.BeanNotValidException;
import framework.exception.bean.BeansException;
import framework.exception.bean.CircularReferenceBeanException;
import framework.exception.bean.NoUniqueBeanException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import testmvc.jsonBeanContainer.controller.AutowiredCheckController;
import testmvc.jsonBeanContainer.controller.TestHelloWorldController;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class JsonBeanContainerTest {

    private static final String prefix = "jsonBeanContainer";

    @Test
    void 테스트_컨트롤러_호출() throws IOException, BeansException {
        JsonBeanContainer beanContainer = new JsonBeanContainer(prefix+"/beans.json");

        TestHelloWorldController helloWorldController = beanContainer.getBean("/hello-world", TestHelloWorldController.class);
        helloWorldController.helloWorld();
    }

    @Test
    void 빈_이름_오류_검증() {
        BeanNotValidException beanNotValidException = assertThrows(
                BeanNotValidException.class,
                () -> new JsonBeanContainer(prefix + "/notValidBeanName.json")
        );
        System.out.println("beanNotValidException = " + beanNotValidException);
    }

    @Test
    void Json_파일_오류_검증() {
        assertThrows(
                UnrecognizedPropertyException.class,
                () -> new JsonBeanContainer(prefix+"/notValidJsonFile.json")
        );
    }

    @Test
    void Autowired_생성자_호출() throws IOException, BeansException {
        JsonBeanContainer beanContainer = new JsonBeanContainer(prefix+"/autowiredCheck.json");

        AutowiredCheckController autowiredCheckController = beanContainer.getBean("/hello-world", AutowiredCheckController.class);
        autowiredCheckController.helloWorld();
    }

    @Test
    void Autowired_여러개_검증() throws IOException, BeanNotValidException {
        assertThrows(
                BeanNotValidException.class,
                () -> new JsonBeanContainer(prefix+"/manyAutowiredCheck.json")
        );
    }

    @Test
    void Bean_순환참조_검증() throws IOException, BeansException {
        CircularReferenceBeanException circularReferenceBeanException = assertThrows(
                CircularReferenceBeanException.class,
                () -> new JsonBeanContainer(prefix + "/circularTest.json")
        );
    }

    @Test
    void Bean_이름중복_검증() throws IOException, BeansException {
        assertThrows(
                NoUniqueBeanException.class,
                () -> new JsonBeanContainer(prefix+"/dupBeanName.json")
        );
    }

    @Test
    void JSON_파일_여러개() throws IOException, BeansException {
        JsonBeanContainer beanContainer = new JsonBeanContainer(prefix+"/beans1.json", prefix+"/beans2.json");

        assertThat(beanContainer.getBean("/hello-world")).isNotNull();
        assertThat(beanContainer.getBean("helloService")).isNotNull();
        assertThat(beanContainer.getBean("worldService")).isNotNull();
    }
}