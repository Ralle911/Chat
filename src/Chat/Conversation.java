package Chat;

import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class Conversation {
    private ArrayList<User> userList;
    private int id;
    private static int numberOfConversations = 0;

    public Conversation(ArrayList<User> userList) {
        this.userList = userList;
        id = ++numberOfConversations;
    }

    public int getId() {
        return id;
    }

    public ArrayList<User> getUserList() {
        return userList;
    }
}
