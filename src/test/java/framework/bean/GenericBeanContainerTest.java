package framework.bean;

import framework.exception.bean.BeanStoreException;
import framework.exception.bean.BeansException;
import framework.exception.bean.NoSuchBeanException;
import framework.exception.bean.NoUniqueBeanException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class GenericBeanContainerTest {

    BeanContainer beanContainer = null;

    @BeforeEach
    void beforeEach() {
        beanContainer = new GenericBeanContainer();
    }

    @AfterEach
    void afterEach() {
        beanContainer.clear();
    }

    @Test
    void 빈_등록_조회() throws BeanStoreException, NoSuchBeanException {
        // given
        String beanName1 = "StringBean";
        String beanName2 = "IntegerBean";
        String bean1 = "StringBean";
        Integer bean2 = Integer.valueOf(10);

        //when
        beanContainer.registerBean(beanName1, bean1);
        beanContainer.registerBean(beanName2, bean2);

        //then
        assertThat(beanContainer.getBeanCount()).isEqualTo(3);
        assertThat(beanContainer.getBeanNames()).contains(beanName1, beanName2);
        assertThat(beanContainer.getBean(beanName1)).isEqualTo(bean1);
        assertThat(beanContainer.getBean(beanName2)).isEqualTo(bean2);
    }

    @Test
    void 빈_등록_이름중복_검증() {
        // given
        String bean1 = "StringBean";
        Integer bean2 = Integer.valueOf(10);

        // then
        assertThrows(
                BeanStoreException.class,
                () -> {
                    beanContainer.registerBean("StringBean", bean1);
                    beanContainer.registerBean("StringBean", bean2);
                }
        );
    }

    @Test
    void 빈_등록_이름_널_검증() {
        // given
        String bean1 = "StringBean";

        // then
        assertThrows(
                IllegalArgumentException.class,
                () -> beanContainer.registerBean(null, bean1)
        );
    }

    @Test
    void 빈_타입_검색() throws BeansException {
        // given
        String bean1 = "StringBean";
        Integer bean2 = Integer.valueOf(1);

        // when
        beanContainer.registerBean("StringBean", bean1);
        beanContainer.registerBean("IntegerBean", bean2);

        // then
        assertThat(beanContainer.getBean(String.class)).isEqualTo(bean1);
    }

    @Test
    void 빈_타입_검색_중복타입_검증() throws BeanStoreException {
        // given
        String bean1 = "StringBean1";
        String bean2 = "StringBean2";

        // when
        beanContainer.registerBean("bean1", bean1);
        beanContainer.registerBean("bean2", bean2);

        // then
        assertThrows(
                NoUniqueBeanException.class,
                () -> beanContainer.getBean(String.class)
        );
    }

}