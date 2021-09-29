package framework.servlet.handler;

import framework.bean.BeanContainer;
import framework.bean.Component;
import framework.exception.bean.NoSuchBeanException;
import framework.exception.handler.HandlerNotFoundException;
import framework.util.UrlPatternMatcher;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class BeanNameUrlHandlerMapping implements HandlerMapping {
    private final BeanContainer beanContainer;
    private TreeMap<String, Object> urlHandlerMap = null;

    public BeanNameUrlHandlerMapping(BeanContainer beanContainer) {
        this.beanContainer = beanContainer;
    }

    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        if(urlHandlerMap == null) {
            initUrlHandlerMap();
        }
        URL url = new URL(request.getRequestURL().toString());
        String path = url.getPath();

        UrlPatternMatcher patternMatcher = new UrlPatternMatcher();
        for(String beanName: urlHandlerMap.keySet()) {
            if(patternMatcher.match(beanName, path)) {
                return urlHandlerMap.get(beanName);
            }
        }

        throw new HandlerNotFoundException("No Handler for path["+path+"]");
    }

    public void initUrlHandlerMap() throws NoSuchBeanException {
        urlHandlerMap = new TreeMap<>((o1, o2) -> {
            Stream<String> stream1 = Arrays.stream(o1.split("/"));
            Stream<String> stream2 = Arrays.stream(o2.split("/"));

            int count1 = (int) stream1.filter(token -> !token.equals("*") && !token.equals("**")).count();
            int count2 = (int) stream2.filter(token -> !token.equals("*") && !token.equals("**")).count();
            if (count1 != count2)
                return count2 - count1;

            stream1 = Arrays.stream(o1.split("/"));
            stream2 = Arrays.stream(o2.split("/"));
            count1 = (int) stream1.filter(token -> token.equals("**")).count();
            count2 = (int) stream2.filter(token -> token.equals("**")).count();
            if (count1 != count2)
                return count1 - count2;

            stream1 = Arrays.stream(o1.split("/"));
            stream2 = Arrays.stream(o2.split("/"));
            count1 = (int) stream1.filter(token -> token.equals("*")).count();
            count2 = (int) stream2.filter(token -> token.equals("*")).count();
            if (count1 != count2)
                return count1 - count2;

            return o1.compareTo(o2);
        });

        for (String beanName : beanContainer.getBeanNames()) {
            urlHandlerMap.put(beanName, beanContainer.getBean(beanName));
        }
    }
}
