package Chat;

/**
 * @author Kalle Bornemark
 */
public class Message {
    private String to;
    private String from;
    private String message;

    public Message(String to, String from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public String toString() {
        return from + ": " + message;
    }
}
