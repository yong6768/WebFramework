package framework.servlet.handler.mvc.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ModelAndView {
    private Object view;
    private LinkedHashMap<String, Object> model = new LinkedHashMap();
    private boolean needRender = true;

    public Object getView() {
        return view;
    }

    public ModelAndView setView(View view) {
        this.view = view;
        return this;
    }

    public String getViewName() {
        if(this.view instanceof String) {
            return (String)this.view;
        }
        return null;
    }

    public ModelAndView setViewName(String viewName) {
        this.view = viewName;
        return this;
    }

    public ModelAndView setNeedRender(boolean b) {
        this.needRender = b;
        return this;
    }

    public boolean needRender() {
        return this.needRender;
    }

    public ModelAndView addAttribute(String attributeName, Object attribute) {
        this.model.put(attributeName, attribute);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
