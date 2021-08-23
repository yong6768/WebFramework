package framework.bean.factory;

import framework.exception.bean.BeansException;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;
    <T> T getBean(Class<T> requiredType) throws BeansException;
    boolean containsBean(String name);
}
