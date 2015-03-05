package chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model class to handle messages
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Message implements Serializable {
    private Conversation to;
    private User from;
    private Object content;
    private String timestamp;
    private static final long serialVersionUID = 133713371337L;

    public Message(Conversation to, User from, Object content) {
        this.to = to;
        this.from = from;
        this.content = content;
        newTime();
    }

    private void newTime() {
        Date time = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        this.timestamp = ft.format(time);
    }

    public String getTimestamp() {
        return this.timestamp;
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
