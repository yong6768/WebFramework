package framework.converter;

import framework.bean.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Component
public class ConverterFactory {
    Map<Class, Converter> converters = new HashMap();

    public Converter getConverter(Class clazz) {
        return converters.get(clazz);
    }

    public <S, T> void addConverter(Converter<S, T> converter) throws NoSuchMethodException {
        Type genericType = converter.getClass().getGenericInterfaces()[0];
        ParameterizedType pType = (ParameterizedType) genericType;

        Class<T> target = (Class<T>) pType.getActualTypeArguments()[1];
        this.converters.put(target, converter);
    }
}
