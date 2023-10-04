package exceptions;

public abstract class FunkoNotStoragedException extends FunkoException{
    public FunkoNotStoragedException(String message) {
        super(message);
    }
}
