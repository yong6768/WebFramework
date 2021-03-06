package framework.servlet.handler.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import framework.bean.Component;
import framework.converter.Converter;
import framework.converter.ConverterFactory;
import framework.servlet.handler.HandlerAdapter;
import framework.servlet.handler.mvc.annotation.RequestBody;
import framework.servlet.handler.mvc.annotation.RequestMapping;
import framework.servlet.handler.mvc.annotation.RequestParam;
import framework.servlet.handler.mvc.annotation.ResponseBody;
import framework.servlet.handler.mvc.view.ModelAndView;
import framework.servlet.handler.mvc.view.View;
import lombok.RequiredArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    private final ObjectMapper objectMapper;
    private final ConverterFactory converterFactory;

    @Override
    public boolean supports(Object handler) {
        if(!(handler instanceof RequestMappingHandler))
            return false;

        if(!((RequestMappingHandler) handler).getMethod().isAnnotationPresent(RequestMapping.class))
            return false;

        return true;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        RequestMappingHandler handlerMapping = (RequestMappingHandler) handler;
        Object controller = handlerMapping.getController();
        Method method = handlerMapping.getMethod();

        Object[] parameters = getParameter(method, request);
        Object ret = method.invoke(controller, parameters);

        if(method.isAnnotationPresent(ResponseBody.class)) {
            response.getWriter().print(ret);
            return new ModelAndView().setNeedRender(false);
        } else {
            return new ModelAndView().setViewName(ret.toString());
        }
    }

    private Object[] getParameter(Method method, HttpServletRequest request) throws IOException {
        List<Object> parameters = new ArrayList<>();

        Class<?>[] parameterTypes = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for(int i=0; i<parameterTypes.length; i++) {
            Class<?> parameterType = parameterTypes[i];
            Annotation[] parameterAnnotation = parameterAnnotations[i];

            treatHttpServletRequest(parameters, parameterType, request);
            treatRequestBody(parameters, parameterType, parameterAnnotation, request);
            treatRequestParam(parameters, parameterType, parameterAnnotation, request);
        }

        return parameters.toArray();
    }

    private void treatHttpServletRequest(List<Object> parameters, Class parameterType, HttpServletRequest request) {
        if(!parameterType.equals(HttpServletRequest.class))
            return;

        parameters.add(request);
    }

    private void treatRequestBody(List<Object> parameters, Class parameterType, Annotation[] parameterAnnotations, HttpServletRequest request) throws IOException {
        if(Arrays.stream(parameterAnnotations).filter(it -> it.annotationType().equals(RequestBody.class)).count() == 0)
            return;

        Object o = objectMapper.readValue(request.getInputStream(), parameterType);
        parameters.add(o);
    }

    private void treatRequestParam(List<Object> parameters, Class parameterType, Annotation[] parameterAnnotations, HttpServletRequest request) {
        RequestParam requestParam = getRequestParam(parameterAnnotations);
        if(requestParam == null)
            return;

        String key = requestParam.value();
        String parameter = request.getParameter(key);

        Converter converter = converterFactory.getConverter(primitiveTypeToWrapper(parameterType));
        parameters.add(converter.convert(parameter));
    }

    private RequestParam getRequestParam(Annotation[] parameterAnnotations) {
        for (Annotation parameterAnnotation : parameterAnnotations) {
            if(parameterAnnotation.annotationType().equals(RequestParam.class)) {
                return (RequestParam) parameterAnnotation;
            }
        }

        return null;
    }

    private Class primitiveTypeToWrapper(Class clazz) {
        if(clazz.equals(int.class)) {
            return Integer.class;
        }

        if(clazz.equals(double.class)) {
            return Integer.class;
        }

        return clazz;
    }
}
