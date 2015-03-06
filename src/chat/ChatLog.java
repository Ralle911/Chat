package chat;
import java.util.Iterator;
import java.util.LinkedList;

public class ChatLog implements Iterable<Message> {
	private LinkedList<Message> list = new LinkedList<Message>();
	private static int MESSAGE_LIMIT = 30;
	
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