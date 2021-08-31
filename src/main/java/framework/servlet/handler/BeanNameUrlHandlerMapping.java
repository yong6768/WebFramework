package framework.servlet.handler;

import framework.bean.BeanContainer;
import framework.exception.bean.NoSuchBeanException;
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

        return urlHandlerMap.get(path);
    }

    public void initUrlHandlerMap() throws NoSuchBeanException {
        urlHandlerMap = new LinkedHashMap<>();

        for (String beanName : beanContainer.getBeanNames()) {
            urlHandlerMap.put(beanName, beanContainer.getBean(beanName));
        }
    }
}
