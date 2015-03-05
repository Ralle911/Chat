package chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1273274782824L;
    private ArrayList<Conversation> conversations;
    private String id;

    public User(String id) {
        this.id = id;
        conversations = new ArrayList<>();
    }

    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    public void addConversations(Conversation conversation) {
        conversations.add(conversation);
    }

    public String getId() {
        return id;
    }
}
