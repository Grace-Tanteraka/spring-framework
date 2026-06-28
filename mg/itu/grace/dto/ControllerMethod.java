package mg.itu.grace.dto;

import java.lang.reflect.Method;

public class ControllerMethod {
    private Class<?> controllerClass;
    private Method associatedMethod;

    public ControllerMethod() {
    }

    public ControllerMethod(Class<?> controllerClass, Method associatedMethod) {
        this.controllerClass = controllerClass;
        this.associatedMethod = associatedMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public void setControllerClass(Class<?> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Method getAssociatedMethod() {
        return associatedMethod;
    }

    public void setAssociatedMethod(Method associatedMethod) {
        this.associatedMethod = associatedMethod;
    }

    
}
