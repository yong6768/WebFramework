package framework.bean;

import com.google.common.reflect.ClassPath;
import framework.exception.bean.BeanNotValidException;
import framework.exception.bean.BeansException;
import framework.exception.bean.NoSuchBeanException;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AnnotationBeanContainer extends GenericBeanContainer {
    private Map<String, Object> beanNameToObject = new HashMap<>();

    public AnnotationBeanContainer(Package... root) throws IOException, ClassNotFoundException, BeansException, InstantiationException, IllegalAccessException, InvocationTargetException {
        List<String> classes = searchPackage(root);
        setBeanCandidates(classes);
//        checkCircularReference();
        registerBean();
    }

    private List<String> searchPackage(Package... packages) throws IOException {
        ClassPath classPath = ClassPath.from(ClassLoader.getSystemClassLoader());

        List<String> classNames = new ArrayList<>();
        for (Package pkg : packages) {
            List<String> filteredClassName = classPath.getAllClasses().stream()
                    .filter(classInfo -> classInfo.getPackageName().startsWith(pkg.getName()))
                    .map(classInfo -> classInfo.getName())
                    .collect(Collectors.toList());

            classNames.addAll(filteredClassName);
        }

        return classNames;
    }

    private void setBeanCandidates(List<String> classes) throws ClassNotFoundException, BeanNotValidException {
        for (String className : classes) {
            Class clazz = Class.forName(className);

            if(clazz.isAnnotation())
                continue;

            if(!hasComponentAnnotation(clazz))
                continue;

            String beanName = getBeanName(clazz);
            beanNameToObject.put(beanName, clazz);

            Method[] methods = clazz.getMethods();
            for (Method method : methods) {
                if(!method.isAnnotationPresent(Bean.class)) {
                    continue;
                }

                Bean bean = method.getDeclaredAnnotation(Bean.class);
                if(bean == null)
                    continue;
                beanName = getBeanName(method);
                beanNameToObject.put(beanName, method);
            }
        }
    }

    private void registerBean() throws InvocationTargetException, InstantiationException, BeansException, IllegalAccessException {
        for (String beanName : beanNameToObject.keySet()) {
            registerAndGetBean(beanName);
        }
    }

    private Object registerAndGetBean(String beanName) throws BeansException, IllegalAccessException, InvocationTargetException, InstantiationException {
        try {
            Object bean = super.getBean(beanName);
            return bean;
        } catch (NoSuchBeanException e) {
            Object beanSeed = beanNameToObject.get(beanName);
            Object bean;
            
            if(beanSeed instanceof Class) {
                Class clazz = (Class)beanSeed;
                Constructor constructor = getConstructor(clazz);
                Class[] parameterTypes = constructor.getParameterTypes();

                List<Object> childParameters = new ArrayList<>();
                for (Class parameterType : parameterTypes) {
                    String childBeanName = getBeanName(parameterType);
                    childParameters.add(registerAndGetBean(childBeanName));
                }

                bean = constructor.newInstance(childParameters.toArray());

            } else if(beanSeed instanceof Method) {
                Method method = (Method)beanSeed;
                Class[] parameterTypes = method.getParameterTypes();

                Class<?> declaringClass = method.getDeclaringClass();
                String parentBeanName = getBeanName(declaringClass);

                Object parentBean = registerAndGetBean(parentBeanName);

                List<Object> childParameters = new ArrayList<>();
                for (Class parameterType : parameterTypes) {
                    String childBeanName = getBeanName(parameterType);
                    childParameters.add(registerAndGetBean(childBeanName));
                }

                bean = method.invoke(parentBean, childParameters.toArray());
            } else {
                throw new BeanNotValidException("beenSeed is not Class and Method, "+beanName);
            }
            super.registerBean(beanName, bean);
            return bean;
        }
    }

    private String getBeanName(Class clazz) {
        Component component = (Component) clazz.getAnnotation(Component.class);
        String beanName = component != null ? component.name() : null;
        if(beanName == null || beanName.equals("")) {
            beanName = clazz.getName();
        }
        return beanName;
    }

    private String getBeanName(Method method) {
        Bean bean = method.getAnnotation(Bean.class);
        String beanName = bean != null ? bean.name() : null;
        if(beanName == null || beanName.equals("")) {
            beanName = method.getReturnType().getName();
        }
        return beanName;
    }

    private Constructor getConstructor(Class clazz) throws BeanNotValidException {
        Constructor constructor;
        Constructor[] constructors = clazz.getConstructors();
        if(constructors.length == 1) {
            constructor = constructors[0];
        } else {
            List<Constructor> autowiredConstructors = Arrays.stream(constructors)
                    .filter(it -> it.isAnnotationPresent(Autowired.class))
                    .collect(Collectors.toList());

            if(autowiredConstructors.size() != 1) {
                throw new BeanNotValidException("Bean must have only 1 constructor or only 1 @Autowired annotated constructor, "+clazz);
            }

            constructor = autowiredConstructors.get(0);
        }

        return constructor;
    }

    private List<String> getDependencies(String beanName) throws BeanNotValidException {
        Object o = beanNameToObject.get(beanName);

        if(o.getClass().isAnnotation())
            return new ArrayList<>();

        if(o instanceof Class) {
            Class clazz = (Class) o;
            Constructor constructor = getConstructor(clazz);
            Class[] parameterTypes = constructor.getParameterTypes();
            return Arrays.stream(parameterTypes)
                    .map(parameterType -> getBeanName(parameterType))
                    .collect(Collectors.toList());
        }

        if( o instanceof Method) {
            Method method = (Method) o;
            Class[] parameterTypes = method.getParameterTypes();
            return Arrays.stream(parameterTypes)
                    .map(parameterType -> getBeanName(parameterType))
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

//    private void checkCircularReference() throws BeanNotValidException, CircularReferenceBeanException {
//        for (String beanName : beanNameToObject.keySet()) {
//            doCheckCircularReference(beanName, new ArrayList<>());
//        }
//    }
//
//    private void doCheckCircularReference(String beanName, List<String> check) throws CircularReferenceBeanException, BeanNotValidException {
//        System.out.println("beanName = " + beanName);
//        if(check.contains(beanName)) {
//            check.add(beanName);
//            throw new CircularReferenceBeanException(String.join("->", check));
//        }
//        check.add(beanName);
//        Object o = beanNameToObject.get(beanName);
//        String nextBeanName = getBeanName(o);
//
//        for (String dependency: getDependencies(nextBeanName)) {
//            doCheckCircularReference(dependency, check);
//        }
//
//        check.remove(check.size()-1);
//    }

    private boolean hasComponentAnnotation(Class clazz) {
        if(clazz.getTypeName() == Component.class.getTypeName()) {
            return true;
        }

        if(clazz.getTypeName() == Retention.class.getTypeName() ||
                clazz.getTypeName() == Documented.class.getTypeName() ||
                clazz.getTypeName() == Target.class.getTypeName()
        ) {
            return false;
        }

        List<? extends Class<? extends Annotation>> annotationTypes = Arrays.stream(clazz.getDeclaredAnnotations())
                .map(annotation -> annotation.annotationType())
                .collect(Collectors.toList());

        for (Class<? extends Annotation> annotationType : annotationTypes) {
            if(hasComponentAnnotation(annotationType)) {
                return true;
            }
        }

        return false;
    }
}
