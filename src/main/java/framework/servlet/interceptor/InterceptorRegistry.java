package framework.servlet.interceptor;

import java.util.ArrayList;
import java.util.List;

public class InterceptorRegistry {
    List<InterceptorRegistryDetail> list = new ArrayList<>();

    InterceptorRegistryDetail addInterceptor(HandlerInterceptor interceptor) {
        InterceptorRegistryDetail detail = new InterceptorRegistryDetail(this, interceptor);
        return detail;
    }

    public List<InterceptorRegistryDetail> getInterceptorRegistryDetails() {
        return list;
    }

    public static class InterceptorRegistryDetail {
        InterceptorRegistry interceptorRegistry;
        HandlerInterceptor interceptor;
        List<String> pathPatterns = new ArrayList<>();
        List<String> excludePathPatterns = new ArrayList<>();

        public InterceptorRegistryDetail(InterceptorRegistry interceptorRegistry, HandlerInterceptor interceptor) {
            this.interceptorRegistry = interceptorRegistry;
            this.interceptor = interceptor;
        }

        public InterceptorRegistry and() {
            return interceptorRegistry;
        }

        public InterceptorRegistryDetail addPathPatterns(String... path) {
            for (String s : path) {
                pathPatterns.add(s);
            }
            return this;
        }

        public InterceptorRegistryDetail addExcludePatterns(String... path) {
            for (String s : path) {
                excludePathPatterns.add(s);
            }
            return this;
        }
    }
}
