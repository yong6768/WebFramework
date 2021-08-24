package framework.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.exception.bean.BeanNotValidException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class JsonBeanContainer extends GenericBeanContainer {

    private HashMap<String, BeanConfig.Bean> beanNameToBeanInfoMapper = new HashMap<>();
    private HashMap<String, List<BeanConfig.Bean>> beanNameToDependenciesMapper = new HashMap<>();

    public JsonBeanContainer (String path) throws IOException, BeanNotValidException {
        byte[] data = getClass().getClassLoader().getResourceAsStream(path).readAllBytes();
        initializeBean(data);
    }

    private void initializeBean(byte[] data) throws IOException, BeanNotValidException {
        ObjectMapper mapper = new ObjectMapper();
        BeanConfig beanConfig = mapper.readValue(data, BeanConfig.class);
        BeanConfig.Bean[] beans = beanConfig.getBeans();

        for(BeanConfig.Bean bean: beans) {
            beanNameToBeanInfoMapper.put(bean.getName(), bean);
        }

        for(BeanConfig.Bean bean: beans) {
            List<BeanConfig.Bean> dependencies = Arrays.stream(bean.getDependencies())
                    .map(beanName -> beanNameToBeanInfoMapper.get(beanName)).collect(Collectors.toList());

            beanNameToDependenciesMapper.put(bean.getName(), dependencies);
        }

        registerBeans(beans);
    }

    private void registerBeans(BeanConfig.Bean[] beans) throws BeanNotValidException {
        for(BeanConfig.Bean bean: beans) {
            registerAndGetBean(bean.getName());
        }
    }

    private Object registerAndGetBean(String beanName) throws BeanNotValidException {
        try {
            return super.getBean(beanName);
        } catch (Exception e) {}

        // reflection 예외를  BeanNotValidException 으로 변경
        try {
            Constructor constructor = getConstructor(beanName);
            List parameter = getParameter(beanName);

            Object bean = constructor.newInstance(parameter.toArray());
            super.registerBean(beanName, bean);
            return bean;
        } catch (Exception e) {
            throw new BeanNotValidException(e);
        }
    }

    private Constructor getConstructor(String beanName) throws ClassNotFoundException, BeanNotValidException {
        BeanConfig.Bean beanInfo = beanNameToBeanInfoMapper.get(beanName);
        Constructor<?>[] constructors = Class.forName(beanInfo.getClassPath()).getConstructors();
        if(constructors.length != 1)
            throw new BeanNotValidException("Bean must have only 1 constructor");

        return constructors[0];
    }

    private List getParameter(String beanName) throws BeanNotValidException {
        BeanConfig.Bean beanInfo = beanNameToBeanInfoMapper.get(beanName);

        List<Object> parameters = new ArrayList<>();
        for(String parameterBeanName: beanInfo.getDependencies()) {
            parameters.add(registerAndGetBean(parameterBeanName));
        }
        return parameters;
    }

    @Getter
    public static class BeanConfig {
        private Bean[] beans;

        @Getter
        private static class Bean {
            private String name;
            @JsonProperty("class")
            private String classPath;
            private String[] dependencies;
        }
    }
}
