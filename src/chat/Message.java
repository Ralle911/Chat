package chat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Model class to handle messages
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Message implements Serializable {
    private String fromUserID;
    private Object content;
    private String timestamp;
    private int conversationID = -1;    /* -1 means it's a lobby message */
    private static final long serialVersionUID = 133713371337L;

    /**
     * Constructor that creates a new message with given conversation ID, String with information who sent it, and its content.
     *
     * @param conversationID The conversation ID.
     * @param fromUserID A string with information who sent the message.
     * @param content The message's content.
     */
    public Message(int conversationID, String fromUserID, Object content) {
        this.conversationID = conversationID;
        this.fromUserID = fromUserID;
        this.content = content;
        newTime();
    }

    /**
     * Creates a new timestamp for the message.
     */
    private void newTime() {
        Date time = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        this.timestamp = ft.format(time);
    }

    /**
     * Returns a string containing sender ID.
     *
     * @return A string with the sender ID.
     */
    public String getFromUserID() {
        return fromUserID;
    }

    /**
     * Returns an int with the conversation ID.
     *
     * @return An int with the conversation ID.
     */
    public int getConversationID() {
        return conversationID;
    }

    /**
     * Returns the message's timestamp.
     *
     * @return The message's timestamp.
     */
    public String getTimestamp() {
        return this.timestamp;
    }

    /**
     * Returns the message's content.
     *
     * @return The message's content.
     */
    public Object getContent() {
        return content;
    }
}
