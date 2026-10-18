package mg.itu.grace.utils;

import jakarta.servlet.ServletContext;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = new Properties();

    public static void init(ServletContext context) {
        try (InputStream input = context.getResourceAsStream("/WEB-INF/config.properties")) {
            if (input != null) {
                properties.load(input);
                System.out.println("config.properties chargé depuis WEB-INF");
            } else {
                System.out.println("Attention : /WEB-INF/config.properties introuvable.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}