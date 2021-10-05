package framework.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.converter.ConverterRegistry;
import framework.converter.service.StringToDoubleConverter;
import framework.converter.service.StringToIntegerConverter;
import framework.converter.service.StringToStringConverter;

@Component
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Override
    public ConverterRegistry addConverter() {
        ConverterRegistry converterRegistry = new ConverterRegistry();
        return converterRegistry
                .addConverter(new StringToIntegerConverter())
                .addConverter(new StringToDoubleConverter())
                .addConverter(new StringToStringConverter());
    }
}
