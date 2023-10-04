package exceptions;

public abstract class FunkoNotFoundException extends FunkoException{
    public FunkoNotFoundException(String message) {
        super(message);
    }
}
