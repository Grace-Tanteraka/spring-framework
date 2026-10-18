package mg.itu.grace.dto;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.lang.reflect.Type;
import java.lang.reflect.Parameter;

import mg.itu.grace.annotations.Param;
import mg.itu.grace.utils.Utils;

import jakarta.servlet.http.Part;

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

    public Object execute(Object context, Map<String, String[]> parameterMap, Collection<Part> parts) throws Exception{
        try {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            Class<?>[] parameterTypes = associatedMethod.getParameterTypes();
            Object[] parameters = new Object[parameterTypes.length];

            for (int i = 0; i < parameterTypes.length; i++) {
                Parameter parameter = associatedMethod.getParameters()[i];
                if (parameterTypes[i].getName().equals("org.springframework.context.ApplicationContext")
                        && context != null) {
                    parameters[i] = context;
                } else  {
                    Param annotation = parameter.getAnnotation(mg.itu.grace.annotations.Param.class);
                    String paramName = (annotation != null) ? annotation.value() : parameter.getName();
                    parameters[i] = Utils.buildObject(parameterTypes[i], parameterMap, paramName, parts);
                }
            }

            return associatedMethod.invoke(controllerInstance, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
