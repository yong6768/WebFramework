package framework.servlet.handler.mvc.view;

public class ThymeleafViewResolver implements ViewResolver {
    @Override
    public View resolveViewName(String viewName) throws Exception {
        return new ThymeleafView(viewName);
    }
}
