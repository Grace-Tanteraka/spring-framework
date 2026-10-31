package mg.itu.grace.exception;

import mg.itu.grace.dto.UrlMethod;

public class UnsupportedUrlException extends Exception {
    public UnsupportedUrlException(UrlMethod urlMethod){
        super("No matching URL found for: " + urlMethod);
    }
}
