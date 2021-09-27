package framework.servlet.handler.mvc.view;

import javax.swing.text.View;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModelAndView {
    private Object view;
    private LinkedHashMap<String, Object> model = new LinkedHashMap();

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

    public ModelAndView addAttribute(String attributeName, Object attribute) {
        this.model.put(attributeName, attribute);
        return this;
    }

    public Map<String, Object> getModel() {
        return model;
    }
}
