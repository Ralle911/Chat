package Chat;

import java.io.Serializable;

/**
 * Model class to handle messages
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Message implements Serializable {
    private Conversation to;
    private User from;
    private static final long serialVersionUID = 133713371337L;

    public Message(Conversation to, User from, Object content) {
        this.to = to;
        this.from = from;
        this.content = content;
    }

    public Conversation getTo() {
        return to;
    }

    public User getFrom() {
        return from;
    }

    public Object getContent() {
        return content;
    }
}
