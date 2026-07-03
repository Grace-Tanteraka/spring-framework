package mg.itu.grace.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import mg.itu.grace.dto.ControllerMethod;
import mg.itu.grace.dto.UrlMethod;
import mg.itu.grace.utils.ClassScanner;

@WebListener
public class ContextListner implements ServletContextListener {
    private ClassScanner classScanner = new ClassScanner();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        List<Class<?>> controllerClasses = new ArrayList<>();
        Map<UrlMethod, ControllerMethod> urlMethodMap = new HashMap<>();
        ServletContext context = sce.getServletContext();
        String longPackageName = context.getInitParameter("controller-base-package");
        try {
            if (longPackageName == null || longPackageName.isEmpty()) {
                controllerClasses.addAll(classScanner.findControllerClasses("ALL", urlMethodMap));
            } else {
                String[] packageNames = longPackageName.split(";");
                for (String pkg : packageNames) {
                    controllerClasses.addAll(classScanner.findControllerClasses(pkg.trim(), urlMethodMap));
                }
            }

            context.setAttribute("classScanner", classScanner);
            context.setAttribute("controllerClasses", controllerClasses);
            context.setAttribute("urlMethodMap", urlMethodMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute("controllerClasses");
        sce.getServletContext().removeAttribute("urlMethodMap");
        sce.getServletContext().removeAttribute("classScanner");
    }
}
