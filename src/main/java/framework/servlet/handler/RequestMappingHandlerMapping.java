package framework.servlet.handler;

import framework.bean.BeanContainer;
import framework.bean.Component;
import framework.exception.bean.NoSuchBeanException;
import framework.exception.handler.HandlerNotFoundException;
import framework.exception.handler.NoUniqueHandlerException;
import framework.servlet.handler.mvc.RequestMappingHandler;
import framework.servlet.handler.mvc.annotation.Controller;
import framework.servlet.handler.mvc.annotation.RequestMapping;
import framework.servlet.handler.mvc.annotation.RequestMethod;
import framework.util.UrlPatternMatcher;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Component
public class RequestMappingHandlerMapping implements HandlerMapping {
    private final BeanContainer beanContainer;
    private TreeMap<String, Object> controllers = null;

    @Override
    public Object getHandler(HttpServletRequest request) throws Exception {
        init();
        String requestURI = request.getRequestURI();

        for (String prefix : controllers.keySet()) {
            if(requestURI.startsWith(prefix)) {
                Object controller = controllers.get(prefix);
                Method method = getDetailHandler(controller, prefix, request);

                if(method != null)
                    return new RequestMappingHandler(controller, method);
            }
        }

        throw new HandlerNotFoundException("No Handler for path["+requestURI+"]");
    }

    private Method getDetailHandler(Object controller, String prefix, HttpServletRequest request) {
        UrlPatternMatcher patternMatcher = new UrlPatternMatcher();
        String requestURI = request.getRequestURI();
        Method[] methods = controller.getClass().getMethods();

        for (Method method : methods) {
            if(method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                String tmp = requestMapping.value();
                if(!tmp.startsWith("/"))
                    tmp = "/"+tmp;
                if(tmp.endsWith("/"))
                    tmp = tmp.substring(0, tmp.length()-1);
                String url = prefix+tmp;

                if(patternMatcher.match(url, requestURI)) {
                    String httpMethod = request.getMethod();
                    RequestMethod[] targetMethods = requestMapping.method();
                    if(targetMethods.length == 0)
                        return method;

                    for (RequestMethod targetMethod : targetMethods) {
                        if(targetMethod.toString().equals(httpMethod))
                            return method;
                    }
                }
            }
        }

        return null;
    }

    private void init() throws NoSuchBeanException, NoUniqueHandlerException {
        if(controllers != null)
            return;

        controllers = new TreeMap<>((o1, o2) -> {
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
            Object bean = beanContainer.getBean(beanName);

            if(bean.getClass().isAnnotationPresent(Controller.class)) {
                String urlPrefix;
                if(bean.getClass().isAnnotationPresent(RequestMapping.class)) {
                    urlPrefix = bean.getClass().getAnnotation(RequestMapping.class).value();
                    if(!urlPrefix.startsWith("/"))
                        urlPrefix = "/"+urlPrefix;
                    if(urlPrefix.endsWith("/"))
                        urlPrefix = urlPrefix.substring(0, urlPrefix.length()-1);
                } else {
                    urlPrefix = "/";
                }

                if(controllers.get(urlPrefix) != null) {
                    Object bf = controllers.get(urlPrefix);
                    String bfName = bf.getClass().getName();
                    String name = bean.getClass().getName();
                    throw new NoUniqueHandlerException("Controller "+bfName+" and "+name+" have same url prefix. Change one to another prefix using @RequestMapping");
                }
                controllers.put(urlPrefix, bean);
            }
        }
    }
}
