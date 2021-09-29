package framework.servlet.handler.mvc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Method;

@AllArgsConstructor
@Getter
public class RequestMappingHandler {
    private Object controller;
    private Method method;


}
