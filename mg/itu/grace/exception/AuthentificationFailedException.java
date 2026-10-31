package mg.itu.grace.exception;

public class AuthentificationFailedException extends Exception {
    public AuthentificationFailedException (){
        super("L'authentification a echouée");
    }
}
