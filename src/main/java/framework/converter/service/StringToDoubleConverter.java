package framework.converter.service;

import framework.converter.Converter;

public class StringToDoubleConverter implements Converter<String, Double> {
    @Override
    public Double convert(String source) {
        return Double.valueOf(source);
    }
}
