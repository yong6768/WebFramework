package framework.bean;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Config {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
