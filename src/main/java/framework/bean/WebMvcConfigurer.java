package framework.bean;

import framework.converter.ConverterRegistry;
import framework.servlet.interceptor.InterceptorRegistry;

public interface WebMvcConfigurer {
    default ConverterRegistry addConverter() {
        return null;
    }

    default InterceptorRegistry addInterceptor() {
        return null;
    }
}
