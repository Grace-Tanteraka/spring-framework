package mg.itu.grace.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import mg.itu.grace.utils.ClassScanner;
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

    public void init() throws ServletException {
        classScanner = new ClassScanner();
        String longPackageName = getInitParameter("controller-base-package");
        if (longPackageName == null || longPackageName.isEmpty()) {
            controllerClasses = classScanner.findControllerClasses("ALL", urlMethodMap);
        } else {
            String[] packageName = longPackageName.split(";");
            for (String pkg : packageName) {
                controllerClasses = classScanner.findControllerClasses(pkg, urlMethodMap);
            }
        }
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
        

        String[] endPathUsingDefault = {".html", ".css", ".js", ".png", ".jpg", ".gif", ".ico", ".woff", ".woff2", ".ttf", ".eot", ".svg", ".mp4", ".webm", ".ogg", ".mp3", ".wav", ".pdf", ".jsp", ".json", ".xml", ".txt", ".csv", ".zip", ".tar", ".gz", ".rar", ".7z"};
        for (String end : endPathUsingDefault) {
            if (url.endsWith(end)) {
                req.getServletContext().getNamedDispatcher("default").forward(req, resp);
                return;
            }
        }

        PrintWriter out = resp.getWriter();

        //out.println("Base URL: " + baseUrl);
        url = classScanner.formatUrl(req);
        ControllerMethod match = null;
        try {
            UrlMethod urlMethod = new UrlMethod(url, req.getMethod());
            match = classScanner.validateUrlMethod(urlMethod, urlMethodMap);
            String toPrint = urlMethod.toString() + " -> " + match.getControllerClass().getName() + " (" + match.getAssociatedMethod().getName() + ")"; 
            out.println(toPrint);
        } catch (Exception e) {
            out.println(e.getMessage() + "\n");
            out.println("Supported URLs:");

            // Print all supported URLs
            for (UrlMethod supportedUrl : urlMethodMap.keySet()) {
                ControllerMethod method = urlMethodMap.get(supportedUrl);
                String toPrint = supportedUrl.toString() + " -> " + method.getControllerClass().getName() + " (" + method.getAssociatedMethod().getName() + ")";
                out.println(toPrint);
            }
        }
    }
}