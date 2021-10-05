package framework.converter.service;

import framework.converter.Converter;

public class StringToStringConverter implements Converter<String, String> {
    @Override
    public String convert(String source) {
        return source;
    }
}
