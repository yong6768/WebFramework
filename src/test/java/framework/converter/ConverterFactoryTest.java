package framework.converter;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ConverterFactoryTest {

    @Test
    void converter_test() throws NoSuchMethodException {
        ConverterFactory factory = new ConverterFactory();
        factory.addConverter(new StringToIntConverter());
        factory.addConverter(new StringToDoubleConverter());

        Converter converter = factory.getConverter(Integer.class);
        Assertions.assertThat(converter.convert("100")).isEqualTo(100);

        converter = factory.getConverter(Double.class);
        Assertions.assertThat(converter.convert("100.0")).isEqualTo(100.0);
    }

    static class StringToIntConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String source) {
            return Integer.valueOf(source);
        }
    }

    static class StringToDoubleConverter implements Converter<String, Double> {
        @Override
        public Double convert(String source) {
            return Double.valueOf(source);
        }
    }
}