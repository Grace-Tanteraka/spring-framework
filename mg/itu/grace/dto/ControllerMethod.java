package mg.itu.grace.dto;

import java.lang.reflect.Method;
import org.springframework.context.ApplicationContext;

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

    public Object execute(Object context) {
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Class<?>[] parameterTypes = associatedMethod.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                if (parameterTypes[i].getName().equals("org.springframework.context.ApplicationContext") && context != null) {
                    parameters[i] = context;
                } else {
                    parameters[i] = null;
                }
            }

            return associatedMethod.invoke(controllerInstance, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
