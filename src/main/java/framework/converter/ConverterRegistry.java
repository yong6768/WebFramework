package framework.converter;

import java.util.ArrayList;
import java.util.List;

public class ConverterRegistry {
    List<Converter> converters = new ArrayList<>();

    public ConverterRegistry addConverter(Converter converter) {
        converters.add(converter);
        return this;
    }

    public List<Converter> getConverters() {
        return this.converters;
    }
}
