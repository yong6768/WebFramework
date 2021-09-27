package framework.servlet.handler.mvc.view;

public interface ViewResolver {
    View resolveViewName(String viewName) throws Exception;
}
