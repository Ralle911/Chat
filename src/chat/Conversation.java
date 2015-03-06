package chat;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Kalle Bornemark
 */
public class Conversation implements Serializable {
    private HashSet<String> involvedUsers;
    private ChatLog conversationLog;
    private int id;
    private static int numberOfConversations = 0;

    public Conversation(HashSet<String> involvedUsersID) {
        this.involvedUsers = involvedUsersID;
        id = ++numberOfConversations;
    }

    public HashSet<String> getInvolvedUsers() {
        return involvedUsers;
    }

    public void setInvolvedUsersID(HashSet<String> involvedUsersID) {
        this.involvedUsers = involvedUsersID;
    }

    public ChatLog getConversationLog() {
        return conversationLog;
    }

    public void setConversationLog(ChatLog conversationLog) {
        this.conversationLog = conversationLog;
    }

    public int getId() {
        return id;
    }

}
