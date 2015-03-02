package Chat;

import java.io.Serializable;

/**
 * @author Kalle Bornemark
 */
public class Message implements Serializable {
    private String to;
    private String from;
    private String message;

    public Message(String to, String from, String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public Message(String from, String message) {
        this.from = from;
        this.message = message;
    }
    
    public Message(String message) {
    	this.message = message;
    }

    public String toString() {
        return from + ": " + message;
    }
}
