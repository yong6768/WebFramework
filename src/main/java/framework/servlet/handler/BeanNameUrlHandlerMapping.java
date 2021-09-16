package framework.servlet.handler;

import framework.bean.BeanContainer;
import framework.exception.bean.NoSuchBeanException;
import framework.exception.handler.HandlerNotFoundException;
import framework.util.UrlPatternMatcher;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Slf4j
public class BeanNameUrlHandlerMapping implements HandlerMapping {
    private final BeanContainer beanContainer;
    private HashMap<String, Object> urlHandlerMap = null;

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
            System.out.println("beanName = " + beanName);
            if(patternMatcher.match(beanName, path)) {
                return urlHandlerMap.get(beanName);
            }
        }

        throw new HandlerNotFoundException("No Handler for path["+path+"]");
    }

    public void initUrlHandlerMap() throws NoSuchBeanException {
        urlHandlerMap = new LinkedHashMap<>();

        for (String beanName : beanContainer.getBeanNames()) {
            urlHandlerMap.put(beanName, beanContainer.getBean(beanName));
        }
    }
}
