package mg.itu.grace.utils;

import java.util.List;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import jakarta.servlet.http.*;
import mg.itu.grace.dto.ControllerMethod;
import mg.itu.grace.annotations.*;

public class ClassScanner {
    public String formatUrl(HttpServletRequest req) {
        String baseUrl = req.getRequestURI();
        String contextPath = req.getContextPath();
        return baseUrl.substring(contextPath.length());
    }

    public ControllerMethod isSupportedUrl(String url, Map<String, ControllerMethod> supportedUrls) throws Exception {
        if (!supportedUrls.containsKey(url))
            throw new Exception("No matching URL found for: " + url);
        return supportedUrls.get(url);
    }

    public List<Class<?>> findControllerClasses(String packageName, Map<String, ControllerMethod> methodUrlsmap) {
        List<Class<?>> list = new ArrayList<>();

        if (packageName == null || packageName.isEmpty() || packageName.equalsIgnoreCase("ALL")) {
            packageName = "";
        }

        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            URL resource = classLoader.getResource(path);

            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    scanDirectoryForController(directory, packageName, list, methodUrlsmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void scanDirectoryForController(File directory, String packageName, List<Class<?>> classes,
            Map<String, ControllerMethod> methodUrlsmap)
            throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, subPackage, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                Class<?> clazz = Class.forName(fullClassName);

                if (clazz.isAnnotationPresent(Controller.class)) {
                    classes.add(clazz);
                    Method[] belongedMethods = clazz.getDeclaredMethods();
                    for (Method meth : belongedMethods) {
                        if (meth.isAnnotationPresent(UrlMapping.class)) {
                            UrlMapping urlMapping = meth.getAnnotation(UrlMapping.class);
                            methodUrlsmap.put(urlMapping.url(), new ControllerMethod(clazz, meth));
                        }
                    }
                }
            }
        }
    }

    public List<String> findAnnotatedClassNames(Class<? extends Annotation> annotationClass,
            String packageName) {
        List<String> controllerClassNames = new ArrayList<>();
        List<Class<?>> classes = findAnnotatedClasses(annotationClass, packageName);
        for (Class<?> clazz : classes) {
            controllerClassNames.add(clazz.getName());
        }

        return controllerClassNames;
    }

    public List<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotationClass, String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        List<Class<?>> classes = findClassInPackage(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                controllerClasses.add(clazz);
            }
        }

        return controllerClasses;
    }

    private List<Class<?>> findClassInPackage(String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        if (packageName == null || packageName.isEmpty() || packageName.equalsIgnoreCase("ALL")) {
            packageName = "";
        }

        try {
            String path = packageName.replace('.', '/');
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

            URL resource = classLoader.getResource(path);

            if (resource != null) {
                File directory = new File(resource.getFile());
                if (directory.exists()) {
                    scanDirectory(directory, packageName, classes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classes;
    }


    private void scanDirectory(File directory, String packageName, List<Class<?>> classes)
            throws ClassNotFoundException {
        File[] files = directory.listFiles();
        if (files == null)
            return;

        for (File file : files) {
            if (file.isDirectory()) {
                String subPackage = packageName.isEmpty() ? file.getName() : packageName + "." + file.getName();
                scanDirectory(file, subPackage, classes);
            } else if (file.getName().endsWith(".class")) {
                String className = file.getName().substring(0, file.getName().length() - 6);
                String fullClassName = packageName.isEmpty() ? className : packageName + "." + className;

                classes.add(Class.forName(fullClassName));
            }
        }
    }
}
