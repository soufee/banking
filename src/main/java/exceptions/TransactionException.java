package exceptions;

public class TransactionException extends RuntimeException {
    public TransactionException(String s) {
        super(s);
    }
}
