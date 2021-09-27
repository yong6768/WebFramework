package framework.servlet.handler.mvc;

import framework.bean.Component;
import framework.exception.handler.HandlerNotSupportException;
import framework.servlet.handler.HandlerAdapter;
import framework.servlet.handler.mvc.view.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SimpleControllerHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!supports(handler)) {
            throw new HandlerNotSupportException(this.getClass().getName()+" is not support handler["+handler.getClass().getName()+"]");
        }

        Controller controller = (Controller)handler;
        return controller.handleRequest(request, response);
    }
}
