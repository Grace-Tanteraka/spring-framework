package mg.itu.grace.web;

import java.util.Map;
import java.util.HashMap;

public class ModelAndView {
    private String viewName;
    private Map<String, Object> attributes;

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }
}
