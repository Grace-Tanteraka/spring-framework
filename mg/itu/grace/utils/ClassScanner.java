package mg.itu.grace.utils;

import java.util.List;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.lang.annotation.Annotation;

import jakarta.servlet.http.*;

import mg.itu.grace.dto.ControllerMethodUrlDto;

public class ClassScanner {
    public static List<ControllerMethodUrlDto> findSupportedUrl(String controllerPackage) {
        List<Class<?>> controllerClasses = findAnnotatedClasses(mg.itu.grace.annotations.Controller.class,
                controllerPackage);
        List<ControllerMethodUrlDto> supportedUrls = new ArrayList<>();
        for (Class<?> clazz : controllerClasses) {
            if (clazz.isAnnotationPresent(mg.itu.grace.annotations.Controller.class)) {
                for (java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(mg.itu.grace.annotations.UrlMapping.class)) {
                        mg.itu.grace.annotations.UrlMapping urlMapping = method
                                .getAnnotation(mg.itu.grace.annotations.UrlMapping.class);

                        supportedUrls
                                .add(new ControllerMethodUrlDto(urlMapping.url(), clazz.getName(), method.getName()));
                    }
                }
            }
        }
        return supportedUrls;
    }

    public static String formatUrl(HttpServletRequest req) {
        String baseUrl = req.getRequestURI();
        String contextPath = req.getContextPath();
        return  baseUrl.substring(contextPath.length());
    }

    public static ControllerMethodUrlDto isSupportedUrl(String url, List<ControllerMethodUrlDto> supportedUrls) throws Exception{
        for (ControllerMethodUrlDto match : supportedUrls) {
            if(url.equals(match.getUrl()) || url.equals(match.getUrl()+"/")) {
                return match;
            }
        }
        throw new Exception("No matching URL found for: " + url);
    }

    public static List<String> findAnnotatedClassNames(Class<? extends Annotation> annotationClass,
            String packageName) {
        List<String> controllerClassNames = new ArrayList<>();
        List<Class<?>> classes = findAnnotatedClasses(annotationClass, packageName);
        for (Class<?> clazz : classes) {
            controllerClassNames.add(clazz.getName());
        }

        return controllerClassNames;
    }

    public static List<Class<?>> findAnnotatedClasses(Class<? extends Annotation> annotationClass, String packageName) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        List<Class<?>> classes = findClassInPackage(packageName);
        for (Class<?> clazz : classes) {
            if (clazz.isAnnotationPresent(annotationClass)) {
                controllerClasses.add(clazz);
            }
        }

        return controllerClasses;
    }

    private static List<Class<?>> findClassInPackage(String packageName) {
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

    private static void scanDirectory(File directory, String packageName, List<Class<?>> classes)
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
