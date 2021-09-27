package framework.bean;

import framework.exception.bean.BeanStoreException;
import framework.exception.bean.BeansException;
import framework.exception.bean.NoSuchBeanException;
import framework.exception.bean.NoUniqueBeanException;
import framework.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class GenericBeanContainer implements BeanContainer {

    private final ConcurrentHashMap<String, Object> beanContainer = new ConcurrentHashMap<>();

    public GenericBeanContainer() {
        beanContainer.put(BeanContainer.class.getName(), this);
    }

    @Override
    public void registerBean(String name, Object bean) throws BeanStoreException {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(bean, "bean must not be null");
        if(containsBean(name)) {
            throw new BeanStoreException("Bean name is already registered, Bean: ["+name+"]");
        }

        log.info("bean is registered, name: {}, bean: {}-{}", name, bean.getClass().getName(), bean.hashCode());
        beanContainer.put(name, bean);
    }

    @Override
    public void removeBean(String beanName) throws NoSuchBeanException {
        Assert.notNull(beanName, "beanName must not be null");
        if(!containsBean(beanName)) {
            throw new NoSuchBeanException("Bean is not registered, Bean: ["+beanName+"]");
        }

        log.info("bean is removed, name: {}", beanName);
        beanContainer.remove(beanName);
    }

    @Override
    public Object getBean(String name) throws NoSuchBeanException {
        Assert.notNull(name, "name must not be null");

        Object bean = beanContainer.get(name);
        if(bean == null) {
            throw new NoSuchBeanException("Bean is not registered, Bean: ["+name+"]");
        }

        return bean;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws NoSuchBeanException {
        Assert.notNull(name, "name must not be null");
        Assert.notNull(requiredType, "requiredType must not be null");

        Object bean = this.getBean(name);
        if(!requiredType.isInstance(bean))
            throw new NoSuchBeanException("Bean is not registered, Bean: ["+name+"], type: ["+requiredType+"]");
        return (T)bean;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException {
        Assert.notNull(requiredType, "requiredType must not be null");

        T targetBean = null;
        Iterator<Object> beanIterator = beanContainer.elements().asIterator();
        while(beanIterator.hasNext()) {
            Object bean = beanIterator.next();
            if(requiredType.isInstance(bean)) {
                // 중복 타입이 있을 경우 오류
                if(targetBean != null)
                    throw new NoUniqueBeanException();

                targetBean = (T)bean;
            }
        }

        if(targetBean == null) {
            throw new NoSuchBeanException("Bean is not registered, BeanType: ["+requiredType.getName()+"]");
        }

        return targetBean;
    }

    @Override
    public boolean containsBean(String name) {
        return beanContainer.containsKey(name);
    }

    @Override
    public String[] getBeanNames() {
        List<String> list = new ArrayList<>();
        Iterator<String> stringIterator = beanContainer.keys().asIterator();
        while(stringIterator.hasNext()) {
            list.add(stringIterator.next());
        }
        return list.toArray(new String[0]);
    }

    @Override
    public int getBeanCount() {
        return beanContainer.size();
    }

    @Override
    public void clear() {
        beanContainer.clear();
    }
}
