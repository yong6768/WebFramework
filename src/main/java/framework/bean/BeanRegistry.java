package framework.bean;

import framework.exception.bean.BeanStoreException;
import framework.exception.bean.NoSuchBeanException;

public interface BeanRegistry {
    void registerBean(String name, Object bean) throws BeanStoreException;
    void removeBean(String beanName) throws NoSuchBeanException;
    Object getBean(String beanName) throws NoSuchBeanException;
    boolean containsBean(String beanName);
    String[] getBeanNames();
    int getBeanCount();
    void clear();
}
