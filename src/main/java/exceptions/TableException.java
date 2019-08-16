package exceptions;

public class TableException extends RuntimeException {

    public TableException(String msg, Exception e) {
        super(msg, e);
    }

    public TableException(String msg) {
        this(msg, null);
    }

    public TableException(Exception e) {
        this(e.getMessage());
    }
}
