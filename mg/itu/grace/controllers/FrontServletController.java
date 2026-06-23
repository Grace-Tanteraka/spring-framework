package mg.itu.grace.controllers;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import mg.itu.grace.utils.ClassScanner;
import mg.itu.grace.annotations.Controller;

import mg.itu.grace.dto.ControllerMethodUrlDto;
import java.util.List;

public class FrontServletController extends HttpServlet {
    private List<ControllerMethodUrlDto> supportedUrls = new java.util.ArrayList<>();

    public void init() throws ServletException {
        String longPackageName = getInitParameter("controller-base-package");
        if (longPackageName == null || longPackageName.isEmpty()) {
            supportedUrls = ClassScanner.findSupportedUrl("ALL");
        } else {
            String[] packageName = longPackageName.split(";");
            for (String pkg : packageName) {
                supportedUrls.addAll(ClassScanner.findSupportedUrl(pkg));
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
        url = ClassScanner.formatUrl(req);
        ControllerMethodUrlDto match = null;
        try {
            match = ClassScanner.isSupportedUrl(url, supportedUrls); 
            out.println(match.toString());
        } catch (Exception e) {
            out.println(e.getMessage());
            out.println("Supported URLs:");
            for (ControllerMethodUrlDto supportedUrl : supportedUrls) {
                out.println(supportedUrl.toString());
            }
        }
    }
}