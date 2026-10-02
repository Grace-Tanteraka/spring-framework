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

    public Object execute(Object context, Map<String, String[]> parameterMap) {
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
                    String[] paramValues = parameterMap.get(paramName);
                    if (paramValues != null && paramValues.length > 0) {
                        if (parameterTypes[i].isArray()) {
                            Class<?> componentType = parameterTypes[i].getComponentType();
                            parameters[i] = mg.itu.grace.utils.Utils.convertStringToArray(paramValues, componentType);
                        } else if (paramValues.length == 1) {
                            String value = paramValues[0];
                            Class<?> paramType = parameterTypes[i];
                            parameters[i] = mg.itu.grace.utils.Utils.convertStringToSimpleType(value, paramType);
                        } else if (Collection.class.isAssignableFrom(parameterTypes[i])) {
                            Class<?> elementType = String.class; // Par défaut si pas de générique spécifié

                            Type genericType = parameter.getParameterizedType();
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType paramType = (ParameterizedType) genericType;
                                Type actualArg = paramType.getActualTypeArguments()[0]; // Récupère le premier type <..>
                                if (actualArg instanceof Class<?>) {
                                    elementType = (Class<?>) actualArg;
                                }
                            }
                            parameters[i] = mg.itu.grace.utils.Utils.convertStringToCollection(paramValues,
                                    parameterTypes[i], elementType);
                        } else {
                            parameters[i] = paramValues[0];
                        }
                    } else {
                        parameters[i] = null;
                    }
                }
            }

            return associatedMethod.invoke(controllerInstance, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
