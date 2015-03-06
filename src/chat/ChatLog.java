package chat;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

public class ChatLog implements Iterable<Message>, Serializable {
	private LinkedList<Message> list = new LinkedList<Message>();
	private static int MESSAGE_LIMIT = 30;
    private static final long serialVersionUID = 13371337133732526L;


    public void add(Message message) {
		if(list.size() >= MESSAGE_LIMIT) {
			list.removeLast();
		}
		list.add(message);		
	}
	
	public Iterator<Message> iterator(){

		return list.iterator();
	}
}