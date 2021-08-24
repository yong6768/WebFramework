package framework.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import framework.exception.bean.BeanNotValidException;
import framework.exception.bean.BeansException;
import framework.exception.bean.CircularReferenceBeanException;
import framework.exception.bean.NoUniqueBeanException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class JsonBeanContainer extends GenericBeanContainer {

    private HashMap<String, BeanConfig.Bean> beanNameToBeanInfoMapper = new HashMap<>();
    private HashMap<String, List<BeanConfig.Bean>> beanNameToDependenciesMapper = new HashMap<>();
    private Stack<String> circularCheck = new Stack<>();

    public JsonBeanContainer (String path) throws IOException, BeansException {
        byte[] data = getClass().getClassLoader().getResourceAsStream(path).readAllBytes();
        initializeBean(data);
    }

    private void initializeBean(byte[] data) throws IOException, BeansException {
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

        validateBeanConfig(beans);

        registerBeans(beans);
    }

    private void registerBeans(BeanConfig.Bean[] beans) throws BeansException {
        for(BeanConfig.Bean bean: beans) {
            registerAndGetBean(bean.getName());
        }
    }

    private Object registerAndGetBean(String beanName) throws BeansException {
        if(circularCheck.contains(beanName)) {
            circularCheck.push(beanName);
            List<String> beanCallChain = circularCheck.subList(circularCheck.indexOf(beanName), circularCheck.size());
            throw new CircularReferenceBeanException(String.join("->", beanCallChain));
        }
        circularCheck.push(beanName);

        try {
            Object bean =super.getBean(beanName);
            circularCheck.pop();
            return bean;
        } catch (Exception e) {}

        // reflection 예외를  BeanNotValidException 으로 변경
        try {
            Constructor constructor = getConstructor(beanName);
            List parameter = getParameter(beanName);

            Object bean = constructor.newInstance(parameter.toArray());
            super.registerBean(beanName, bean);
            return bean;
        } catch (CircularReferenceBeanException crbException) {
            throw crbException;
        } catch (BeansException beansException) {
            throw beansException;
        } catch (Exception e) {
            throw new BeanNotValidException(e);
        } finally {
            circularCheck.pop();
        }
    }

    private Constructor getConstructor(String beanName) throws ClassNotFoundException, BeanNotValidException {
        BeanConfig.Bean beanInfo = beanNameToBeanInfoMapper.get(beanName);

        // 생성자가 하나인 경우 해당 생성자 사용
       Constructor<?>[] constructors = Class.forName(beanInfo.getClassPath()).getConstructors();
       if(constructors.length == 1)
           return constructors[0];

       // 생성자가 여러 개인 경우 @Authwired가 붙은 생성자 사용
        List<Constructor<?>> autowiredConstructor = Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
                .collect(Collectors.toList());

        if(autowiredConstructor.size() != 1)
            throw new BeanNotValidException("Bean must have only 1 constructor or only 1 @Autowired annotated constructor");

        return autowiredConstructor.get(0);
    }

    private List getParameter(String beanName) throws BeansException {
        BeanConfig.Bean beanInfo = beanNameToBeanInfoMapper.get(beanName);

        List<Object> parameters = new ArrayList<>();
        for(String parameterBeanName: beanInfo.getDependencies()) {
            parameters.add(registerAndGetBean(parameterBeanName));
        }
        return parameters;
    }

    private void validateBeanConfig(BeanConfig.Bean[] beans) throws NoUniqueBeanException {
        // Bean 이름 중복 검증
        Map<String, Long> beanNameCount = Arrays.stream(beans).collect(Collectors.groupingBy(
                f -> f.name, Collectors.counting()
        ));
        for (String beanName : beanNameCount.keySet()) {
            if(beanNameCount.get(beanName) > 1)
                throw new NoUniqueBeanException("Bean["+beanName+"] is declared many times");
        }

        // 순환 참조 검증

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
