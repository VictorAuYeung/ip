package walter;

/**
 * Represents an exception specific to the Walter application.
 */
public class WalterException extends Exception {
    /**
     * Constructs a new {@code WalterException} with the specified detail message.
     *
     * @param message The detail message.
     */
    public WalterException(String message) {
        super(message);
    }
}
