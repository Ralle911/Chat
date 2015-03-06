package chat;

import java.io.Serializable;
import java.util.HashSet;

/**
 * @author Kalle Bornemark
 */
public class Conversation implements Serializable {
    private HashSet<String> involvedUsersID;
    private ChatLog conversationLog;
    private int id;
    private static int numberOfConversations = 0;

    public Conversation(HashSet<String> involvedUsersID) {
        this.involvedUsersID = involvedUsersID;
        id = ++numberOfConversations;
    }

    public HashSet<String> getInvolvedUsersID() {
        return involvedUsersID;
    }

    public void setInvolvedUsersID(HashSet<String> involvedUsersID) {
        this.involvedUsersID = involvedUsersID;
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
