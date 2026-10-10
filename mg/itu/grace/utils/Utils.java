package mg.itu.grace.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.Map;

import java.lang.reflect.*;

public class Utils {
    public static Object convertStringToSimpleType(String value, Class<?> targetType) {
        if (targetType.equals(String.class)) {
            return value;
        } else if (targetType.equals(Integer.class) || targetType.equals(int.class)) {
            return Integer.parseInt(value);
        } else if (targetType.equals(Double.class) || targetType.equals(double.class)) {
            return Double.parseDouble(value);
        } else if (targetType.equals(Boolean.class) || targetType.equals(boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (targetType.equals(Long.class) || targetType.equals(long.class)) {
            return Long.parseLong(value);
        } else if (targetType.equals(Float.class) || targetType.equals(float.class)) {
            return Float.parseFloat(value);
        } else if (targetType.equals(Short.class) || targetType.equals(short.class)) {
            return Short.parseShort(value);
        } else if (targetType.equals(Byte.class) || targetType.equals(byte.class)) {
            return Byte.parseByte(value);
        } else if (targetType.equals(Character.class) || targetType.equals(char.class)) {
            if (value.length() != 1) {
                throw new IllegalArgumentException("Cannot convert string to char: " + value);
            }
            return value.charAt(0);
        }
        throw new IllegalArgumentException("Unsupported type: " + targetType.getName());
    }

    public static boolean isSimpleType(Class<?> clazz) {
        return clazz.isPrimitive() || clazz.equals(String.class) || clazz.equals(Integer.class)
                || clazz.equals(Double.class) || clazz.equals(Boolean.class) || clazz.equals(Long.class)
                || clazz.equals(Float.class) || clazz.equals(Short.class) || clazz.equals(Byte.class)
                || clazz.equals(Character.class);
    }

    public static Object convertStringToCollection(String[] values, Class<?> collectionType, Class<?> elementType) {
        Collection<Object> collectionInstance;
        if (Set.class.isAssignableFrom(collectionType)) {
            collectionInstance = new HashSet<>();
        } else if (Vector.class.isAssignableFrom(collectionType)) {
            collectionInstance = new Vector<>();
        } else {
            // Par défaut pour List, Collection, etc.
            collectionInstance = new ArrayList<>();
        }
        for (String value : values) {
            collectionInstance.add(convertStringToSimpleType(value, elementType));
        }
        return collectionInstance;
    }

    public static boolean isAnObject(Class<?> clazz) {
        return !isSimpleType(clazz) && !clazz.isArray() && !Collection.class.isAssignableFrom(clazz);
    }

    public static Object convertStringToArray(String[] values, Class<?> componentType) {
        Object arrayInstance = java.lang.reflect.Array.newInstance(componentType, values.length);
        for (int i = 0; i < values.length; i++) {
            java.lang.reflect.Array.set(arrayInstance, i, convertStringToSimpleType(values[i], componentType));
        }
        return arrayInstance;
    }

    public static Object convertStringToType(String[] values, Class<?> targetType) throws Exception {
        Object instance = null;
        if (targetType.isArray()) {
            instance = convertStringToArray(values, targetType.getComponentType());
        } else if (Collection.class.isAssignableFrom(targetType)) {
            // Pour les collections, on suppose que l'élément est de type String par défaut
            instance = convertStringToCollection(values, targetType, String.class);
        } else if (isSimpleType(targetType)) {
            if (values.length > 0) {
                instance = convertStringToSimpleType(values[0], targetType);
            } else {
                System.out.println("NULL ny entana eh");
                throw new NullPointerException("Impossible de changer une valeur nulle");
                // instance = null; // ou une valeur par défaut selon le type
            }
        } else {
            try {
                if (values.length > 0) {
                    instance = targetType.cast(values[0]);
                }
            } catch (Exception e) {
                throw new RuntimeException("Cannot instantiate type: " + targetType.getName() + "\n" + e.getMessage(),
                        e);
            }
        }
        return instance;
    }

    /* public static Object buildObject(Class<?> targetType, Map<String, String[]> parameterMap, String prefix)
            throws Exception {
        Object instance = null;

        if (targetType.isAnnotationPresent(mg.itu.grace.annotations.Model.class) || isAnObject(targetType)) {
            instance = targetType.getDeclaredConstructor().newInstance();
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String paramName = entry.getKey();

                String start = prefix + ".";
                if (!paramName.startsWith(start)) {
                    continue;
                }

                String fieldName = paramName.substring(start.length());
                String[] parts = fieldName.split("\\.");
                Field targetField = targetType.getDeclaredField(parts[0]);

                targetField.setAccessible(true);

                String[] paramValues = entry.getValue();
                if (targetField.getType().isAnnotationPresent(mg.itu.grace.annotations.Model.class)
                        || isAnObject(targetField.getType())) {
                    Class<?> nestedType = targetField.getType();
                    String newPrefix = start + parts[0];
                    Object nestedObject = buildObject(nestedType, parameterMap, newPrefix);
                    targetField.set(instance, nestedObject);
                } else {
                    System.out.println("------------------tsy object lo le izy");
                    Object fieldValue = convertStringToType(paramValues, targetField.getType());
                    targetField.set(instance, fieldValue);
                }
            }
        } else {
            String[] paramValues = parameterMap.get(prefix);
            instance = convertStringToType(paramValues, targetType);
        }

        return instance;
    } */

    public static Object buildObject(Class<?> targetType, Map<String, String[]> parameterMap, String prefix)
            throws Exception {
        Object instance = targetType.getDeclaredConstructor().newInstance();

        for (Field targetField : targetType.getDeclaredFields()) {
            String fieldName = targetField.getName();
            
            Method setterMethod = targetType.getDeclaredMethod("set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1), targetField.getType());


            String fieldPrefix = (prefix != null && !prefix.isEmpty()) ? prefix + "." + fieldName : fieldName;

            if (targetField.getType().isAnnotationPresent(mg.itu.grace.annotations.Model.class) || isAnObject(targetField.getType())) {
                Object nestedObject = buildObject(targetField.getType(), parameterMap, fieldPrefix);
                
                if (nestedObject != null) {
                    setterMethod.invoke(instance, nestedObject);
                }
            } else {
                String[] paramValues = parameterMap.get(fieldPrefix);
                if (paramValues != null && paramValues.length > 0) {
                    Object fieldValue = convertStringToType(paramValues, targetField.getType());
                    setterMethod.invoke(instance, fieldValue);
                }
            }
        }

        return instance;
    }
}
