package framework.bean;

import framework.converter.ConverterRegistry;

public interface WebMvcConfigurer {
    ConverterRegistry addConverter();
}
