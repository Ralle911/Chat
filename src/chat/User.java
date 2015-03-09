package chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to hold information of a user.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1273274782824L;
    private ArrayList<Conversation> conversations;
    private String id;

    /**
     * Constructor to create a User with given ID.
     *
     * @param id A string with the user ID.
     */
    public User(String id) {
        this.id = id;
        conversations = new ArrayList<>();
    }

    /**
     * Returns an ArrayList with the user's conversations
     *
     * @return The user's conversations.
     */
    public ArrayList<Conversation> getConversations() {
        return conversations;
    }

    /**
     * Adds a new conversation to the user.
     *
     * @param conversation The conversation to be added.
     */
    public void addConversation(Conversation conversation) {
        conversations.add(conversation);
    }

    /**
     * Returns the user's ID.
     *
     * @return The user's ID.
     */
    public String getId() {
        return id;
    }
}
