/**
 * Represents an error caused by invalid user input that Suki cannot act on.
 */
public class SukiException extends Exception {
    public SukiException(String message) {
        super(message);
    }
}
