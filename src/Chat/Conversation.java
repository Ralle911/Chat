package Chat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Kalle Bornemark
 */
public class Conversation implements Serializable {
    private ArrayList<User> userList;
    private int id;
    private static int numberOfConversations = 0;
    private boolean toAll = false;

    public boolean isToAll() {
        return toAll;
    }

    public void setToAll(boolean toAll) {
        this.toAll = toAll;
    }

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
