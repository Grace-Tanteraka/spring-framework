package mg.itu.grace.context;

import java.lang.reflect.Method;
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

//@WebListener
public class ContextListner implements ServletContextListener {
    private ClassScanner classScanner = new ClassScanner();
    private String viewsBasePath = "/WEB-INF/views/";
    private String viewsExtension = ".jsp";

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        Object springContext = null;
        try {
            Class<?> utilsClass = Class.forName("org.springframework.web.context.support.WebApplicationContextUtils");
            Method getContextMethod = utilsClass.getMethod("getRequiredWebApplicationContext", ServletContext.class);
            
            // springContext est un simple java.lang.Object !
            springContext = getContextMethod.invoke(null, context);
            
        } catch (ClassNotFoundException e) {
            System.err.println("Spring Framework is not available in the classpath.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        context.setAttribute("SPRING_CONTEXT", springContext);

        List<Class<?>> controllerClasses = new ArrayList<>();
        Map<UrlMethod, ControllerMethod> urlMethodMap = new HashMap<>();
        String longPackageName = context.getInitParameter("controller-base-package");
        viewsBasePath = context.getInitParameter("viewsBasePath") != null ? context.getInitParameter("viewsBasePath")
                : viewsBasePath;
        viewsExtension = context.getInitParameter("viewsExtension") != null ? context.getInitParameter("viewsExtension")
                : viewsExtension;
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
            context.setAttribute("viewsBasePath", viewsBasePath);
            context.setAttribute("viewsExtension", viewsExtension);
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
