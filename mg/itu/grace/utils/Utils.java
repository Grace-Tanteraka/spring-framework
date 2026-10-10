package mg.itu.grace.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

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

    public static Object convertStringToArray(String[] values, Class<?> componentType) {
        Object arrayInstance = java.lang.reflect.Array.newInstance(componentType, values.length);
        for (int i = 0; i < values.length; i++) {
            java.lang.reflect.Array.set(arrayInstance, i, convertStringToSimpleType(values[i], componentType));
        }
        return arrayInstance;
    }

    public static Object convertStringToType(String[] values, Class<?> targetType) {
        if (targetType.isArray()) {
            return convertStringToArray(values, targetType.getComponentType());
        } else if (Collection.class.isAssignableFrom(targetType)) {
            // Pour les collections, on suppose que l'élément est de type String par défaut
            return convertStringToCollection(values, targetType, String.class);
        } else if (isSimpleType(targetType)) {
            if (values.length > 0) {
                return convertStringToSimpleType(values[0], targetType);
            } else {
                return null; // ou une valeur par défaut selon le type
            }
        } 
        Object instance = null;
        try {
            if(values.length > 0) {
                instance = targetType.cast(values[0]);
            } 
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate type: " + targetType.getName(), e);
        }
        return instance;
    }
}
