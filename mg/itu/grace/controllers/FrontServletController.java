package mg.itu.grace.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.context.ApplicationContext;

import mg.itu.grace.utils.ClassScanner;
import mg.itu.grace.web.ModelAndView;
import mg.itu.grace.annotations.Controller;
import mg.itu.grace.dto.ControllerMethod;
import mg.itu.grace.dto.UrlMethod;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class FrontServletController extends HttpServlet {
    private ClassScanner classScanner;
    private List<Class<?>> controllerClasses;
    private Map<UrlMethod, ControllerMethod> urlMethodMap = new HashMap<>();
    private String viewsBasePath = "";
    private String viewsExtension = "";
    private Object applicationContext;

    public void init() throws ServletException {
        ServletContext context = this.getServletContext();
        classScanner = (ClassScanner) context.getAttribute("classScanner");
        controllerClasses = (List<Class<?>>) context.getAttribute("controllerClasses");
        urlMethodMap = (Map<UrlMethod, ControllerMethod>) context.getAttribute("urlMethodMap");

        viewsBasePath = (String) context.getAttribute("viewsBasePath");
        viewsExtension = (String) context.getAttribute("viewsExtension");

        applicationContext = context.getAttribute("SPRING_CONTEXT");
    }

    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        processHandler(req, resp);
    }

    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        processHandler(req, resp);
    }

    protected void processHandler(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {

        String url = req.getRequestURL().toString();

        if (url.endsWith(".jsp")) {
            req.getServletContext().getNamedDispatcher("jsp").forward(req, resp);
            return;
        }

        String[] endPathUsingDefault = { ".html", ".css", ".js", ".png", ".jpg", ".gif", ".ico", ".woff", ".woff2",
                ".ttf", ".eot", ".svg", ".mp4", ".webm", ".ogg", ".mp3", ".wav", ".pdf", ".json", ".xml",
                ".txt", ".csv", ".zip", ".tar", ".gz", ".rar", ".7z" };
        for (String end : endPathUsingDefault) {
            if (url.endsWith(end)) {
                req.getServletContext().getNamedDispatcher("default").forward(req, resp);
                return;
            }
        }

        PrintWriter out = resp.getWriter();

        // out.println("Base URL: " + baseUrl);
        url = classScanner.formatUrl(req);
        ControllerMethod match = null;
        try {
            UrlMethod urlMethod = new UrlMethod(url, req.getMethod());
            match = classScanner.validateUrlMethod(urlMethod, urlMethodMap);

            Object result = match.execute(applicationContext);
            if (result instanceof ModelAndView) {
                ModelAndView modelAndView = (ModelAndView) result;
                String viewName = modelAndView.getViewName();
                String viewPath = viewsBasePath + viewName + viewsExtension;

                for (Map.Entry<String, Object> entry : modelAndView.getAttributes().entrySet()) {
                    req.setAttribute(entry.getKey(), entry.getValue());
                }

                req.getRequestDispatcher(viewPath).forward(req, resp);
            } else {
                String toPrint = urlMethod.toString() + " -> " + match.getControllerClass().getName() + " ("
                        + match.getAssociatedMethod().getName() + ")";
                out.println(toPrint);
                if(result != null) {
                    out.println("This is the result: " + result.toString());
                }
            }
        } catch (Exception e) {
            out.println(e.getMessage() + "\n");
            out.println("Supported URLs:");

            // Print all supported URLs
            for (UrlMethod supportedUrl : urlMethodMap.keySet()) {
                ControllerMethod method = urlMethodMap.get(supportedUrl);
                String toPrint = supportedUrl.toString() + " -> " + method.getControllerClass().getName() + " ("
                        + method.getAssociatedMethod().getName() + ")";
                out.println(toPrint);
            }
        }
    }
}