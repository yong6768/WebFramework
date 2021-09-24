package framework.servlet.handler;

import framework.bean.GenericBeanContainer;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BeanNameUrlHandlerMappingTest {

    @Test
    void requestMapping_맵핑_순서_테스트_1() throws Exception {
        // given
        GenericBeanContainer mockBeanContainer = mock(GenericBeanContainer.class);
        when(mockBeanContainer.getBean("/hello")).thenReturn("Hello Bean 1");
        when(mockBeanContainer.getBean("/hello/**")).thenReturn("Hello Bean 2");
        when(mockBeanContainer.getBeanNames()).thenReturn(new String[]{"/hello", "/hello/**"});

        HttpServletRequest mockHttpRequest =  mock(HttpServletRequest.class);
        StringBuffer sb = new StringBuffer().append("http://localhost:8080/hello");
        when(mockHttpRequest.getRequestURL()).thenReturn(sb);

        // when
        BeanNameUrlHandlerMapping handlerMapping = new BeanNameUrlHandlerMapping(mockBeanContainer);
        Object handler = handlerMapping.getHandler(mockHttpRequest);

        // then
        assertThat(handler).isEqualTo("Hello Bean 1");
    }

    @Test
    void requestMapping_맵핑_순서_테스트_2() throws Exception {
        // given
        GenericBeanContainer mockBeanContainer = mock(GenericBeanContainer.class);
        when(mockBeanContainer.getBean("/hello/*")).thenReturn("Hello Bean 1");
        when(mockBeanContainer.getBean("/hello/**")).thenReturn("Hello Bean 2");
        when(mockBeanContainer.getBeanNames()).thenReturn(new String[]{"/hello/*", "/hello/**"});

        HttpServletRequest mockHttpRequest =  mock(HttpServletRequest.class);
        StringBuffer sb = new StringBuffer().append("http://localhost:8080/hello");
        when(mockHttpRequest.getRequestURL()).thenReturn(sb);

        // when
        BeanNameUrlHandlerMapping handlerMapping = new BeanNameUrlHandlerMapping(mockBeanContainer);
        Object handler = handlerMapping.getHandler(mockHttpRequest);

        // then
        assertThat(handler).isEqualTo("Hello Bean 2");
    }

    @Test
    void requestMapping_맵핑_순서_테스트_3() throws Exception {
        // given
        GenericBeanContainer mockBeanContainer = mock(GenericBeanContainer.class);
        when(mockBeanContainer.getBean("/hello/*")).thenReturn("Hello Bean 1");
        when(mockBeanContainer.getBean("/hello/**")).thenReturn("Hello Bean 2");
        when(mockBeanContainer.getBeanNames()).thenReturn(new String[]{"/hello/*", "/hello/**"});

        HttpServletRequest mockHttpRequest =  mock(HttpServletRequest.class);
        StringBuffer sb = new StringBuffer().append("http://localhost:8080/hello/world");
        when(mockHttpRequest.getRequestURL()).thenReturn(sb);

        // when
        BeanNameUrlHandlerMapping handlerMapping = new BeanNameUrlHandlerMapping(mockBeanContainer);
        Object handler = handlerMapping.getHandler(mockHttpRequest);

        // then
        assertThat(handler).isEqualTo("Hello Bean 1");
    }

    @Test
    void requestMapping_맵핑_순서_테스트_4() throws Exception {
        // given
        GenericBeanContainer mockBeanContainer = mock(GenericBeanContainer.class);
        when(mockBeanContainer.getBean("/hello/*/a")).thenReturn("Hello Bean 1");
        when(mockBeanContainer.getBean("/hello/*/*")).thenReturn("Hello Bean 2");
        when(mockBeanContainer.getBeanNames()).thenReturn(new String[]{"/hello/*/a", "/hello/*/*"});

        HttpServletRequest mockHttpRequest =  mock(HttpServletRequest.class);
        StringBuffer sb = new StringBuffer().append("http://localhost:8080/hello/world/a");
        when(mockHttpRequest.getRequestURL()).thenReturn(sb);

        // when
        BeanNameUrlHandlerMapping handlerMapping = new BeanNameUrlHandlerMapping(mockBeanContainer);
        Object handler = handlerMapping.getHandler(mockHttpRequest);

        // then
        assertThat(handler).isEqualTo("Hello Bean 1");
    }
}