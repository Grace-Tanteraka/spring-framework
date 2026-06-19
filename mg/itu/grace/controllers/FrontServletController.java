package mg.itu.grace.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import mg.itu.grace.utils.ClassScanner;
import mg.itu.grace.annotations.Controller;
import java.util.List;

public class FrontServletController extends HttpServlet {
    private List<String> controllerClassNames = new java.util.ArrayList<>();

    public void init() throws ServletException {
        String longPackageName = getInitParameter("controller-base-package");
        if (longPackageName == null || longPackageName.isEmpty()) {
            controllerClassNames = ClassScanner.findControllerClassNames(Controller.class, "ALL");
        } else {
            String[] packageName = longPackageName.split(";");
            for (String pkg : packageName) {
                controllerClassNames.addAll(ClassScanner.findControllerClassNames(Controller.class, pkg));
            }
        }
    }

    protected void doGet(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    protected void doPost(
            HttpServletRequest req,
            HttpServletResponse resp) throws ServletException, IOException {
        handleRequest(req, resp);
    }

    protected void handleRequest(
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
        out.println("Handling request for URL: " + url);
        for (String string : controllerClassNames) {
            out.println("Found controller: " + string);
        }
        if(controllerClassNames.isEmpty()) {
            out.println("No controllers found in the specified packages.");
        }
        //out.println("Welcome to the Request Handler!");
    }
}