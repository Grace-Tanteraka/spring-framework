package mg.itu.grace.exception;

import mg.itu.grace.dto.UrlMethod;

public class DuplicatedUrlException extends Exception {

    public DuplicatedUrlException(UrlMethod urlMethod, String className, String methodName){
        String message = "Duplicate URL mapping found for: " + urlMethod
                + " in class: " + className + " method: " + methodName;
        super(message);
    }
}
