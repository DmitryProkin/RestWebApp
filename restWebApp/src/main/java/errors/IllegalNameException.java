package errors;

public class IllegalNameException extends Exception {
    private static final long serialVersionUID = -6647544772732631047L;
    public static IllegalNameException DEFAULT_INSTANCE = new
            IllegalNameException("ERROR: fields cannot be null or empty");
    public IllegalNameException(String message) {
        super(message);
    }
}
