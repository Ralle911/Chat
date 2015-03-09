package chat;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Class to hold information of a conversation.
 *
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren,
 * Jimmy Maksymiw, Lorenz Puskas & Rasmus Andersson
 */
public class Conversation implements Serializable {
    private HashSet<String> involvedUsers;
    private ChatLog conversationLog;
    private int id;
    private static int numberOfConversations = 0;

    /**
     * Constructor that takes a HashSet of involved users.
     *
     * @param involvedUsersID The user ID's to be added to the conversation.
     */
    public Conversation(HashSet<String> involvedUsersID) {
        this.involvedUsers = involvedUsersID;
        this.conversationLog = new ChatLog();
        id = ++numberOfConversations;
    }

    /**
     * Returns a HashSet of the conversation's involved users.
     *
     * @return A hashSet of the conversation's involved users.
     */
    public HashSet<String> getInvolvedUsers() {
        return involvedUsers;
    }

    /**
     * Returns the conversion's ChatLog.
     *
     * @return The conversation's ChatLog.
     */
    public ChatLog getConversationLog() {
        return conversationLog;
    }

    /**
     * Adds a message to the conversation.
     *
     * @param message The message to be added.
     */
    public void addMessage(Message message) {
        conversationLog.add(message);

    }

    /**
     * Return the conversation's ID.
     *
     * @return The conversation's ID.
     */
    public int getId() {
        return id;
    }

}
