package mg.itu.grace.dto;

import java.util.Objects;

public class UrlMethod {
    String url;
    String method;

    public UrlMethod(String url, String method) {
        this.url = url;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        UrlMethod urlMethod = (UrlMethod) obj;
        return url.equals(urlMethod.url) && method.toLowerCase().equals(urlMethod.method.toLowerCase());
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, method.toLowerCase());
    }

    @Override
    public String toString(){
        return url+" ["+method+"]";
    }
}