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
    private String fromUserID;
    private Object content;
    private String timestamp;
    private int conversationID = -1;        /* -1 means it's a lobby message */
    private static final long serialVersionUID = 133713371337L;

    public Message(int conversationID, String fromUserID, Object content) {
        this.conversationID = conversationID;
        this.fromUserID = fromUserID;
        this.content = content;
        newTime();
    }

    public Message(String fromUserID, Object content) {
        this.fromUserID = fromUserID;
        this.content = content;
        newTime();
    }

    private void newTime() {
        Date time = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");
        this.timestamp = ft.format(time);
    }

    public String getFromUserID() {
        return fromUserID;
    }

    public int getConversationID() {
        return conversationID;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public Object getContent() {
        return content;
    }
}
