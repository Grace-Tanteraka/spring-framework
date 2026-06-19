package mg.itu.grace.dto;

public class ControllerMethodUrlMatch {
    private String url;
    private String controllerClassName;
    private String methodName;

    public ControllerMethodUrlMatch() {
    }

    public ControllerMethodUrlMatch(String url, String controllerClassName, String methodName) {
        this.url = url;
        this.controllerClassName = controllerClassName;
        this.methodName = methodName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getControllerClassName() {
        return controllerClassName;
    }

    public void setControllerClassName(String controllerClassName) {
        this.controllerClassName = controllerClassName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(url)
        .append(" ").append(controllerClassName)
        .append("->").append(methodName);
        return sb.toString();
    }

}
