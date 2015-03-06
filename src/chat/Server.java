package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Model class for the server.
 * 
 * @author Emil Sandgren, Kalle Bornemark, Erik Sandgren, Jimmy Maksymiw, Lorenz
 *         Puskas & Rasmus Andersson
 */
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private ArrayList<ConnectedClient> connectedClients;
	private ArrayList<User> registeredUsers;
	private ArrayList<Conversation> allConversations;

	public Server(int port) {
		registeredUsers = new ArrayList<>();
		connectedClients = new ArrayList<>();
		allConversations = new ArrayList<>();
		try {
			serverSocket = new ServerSocket(port);
			new Thread(this).start();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public void writeToAll(Object message) {
		for (ConnectedClient client : connectedClients) {
			client.write(message);
		}
	}

	public void sendMessage(Message message) {
		if (message.getConversationID() == -1) {
			writeToAll(message);
		}
	}

	public void sendConversation(Conversation con) {
		HashSet<String> users = con.getInvolvedUsers();
        for (String s : users) {
            for (ConnectedClient c : connectedClients) {
                if (c.getUser().getId().equals(s)) {
                    c.write(con);
                }
            }
        }


//		Iterator<String> itr = users.iterator();
//		while (itr.hasNext()) {
//			String user = itr.next();
//			for (ConnectedClient client : connectedClients) {
//				if (client.getUser().getId() == user) {
//					client.write(con);
//				}
//			}
//		}
	}

	public void sendConnectedClients() {
		ArrayList<User> connectedUsers = new ArrayList<>();
		for (ConnectedClient client : connectedClients) {
			connectedUsers.add(client.getUser());
		}
		writeToAll(connectedUsers);
	}

	public void run() {
		System.out.println("Server started");
		while (true) {
			try {
				Socket socket = serverSocket.accept();
				ConnectedClient client = new ConnectedClient(socket, this);
				connectedClients.add(client);
			} catch (IOException e) {
				System.err.println(e);
			}
		}
	}

	private class ConnectedClient implements Runnable {
		private Thread client = new Thread(this);
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private Server server;
		private User user;
		private Socket socket;

		public ConnectedClient(Socket socket, Server server) {
			this.socket = socket;
			this.server = server;
			try {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
			}
			client.start();
		}

		public User getUser() {
			return user;
		}

		public User getUser(String id) {
			for (User user : registeredUsers) {
				if (user.getId().equals(id)) {
					return user;
				}
			}
			return null;
		}

		public void write(Object object) {
			try {
				oos.writeObject(object);
			} catch (IOException e) {
			}
		}

		public void removeConnectedClient() {
			for (int i = 0; i < connectedClients.size(); i++) {
				if (connectedClients.get(i).getUser().getId()
						.equals(this.getUser().getId())) {
					connectedClients.remove(i);
					System.out.println("Client removed from connectedClients");
				}
			}
		}

		public void disconnectClient() {
			removeConnectedClient();
			sendConnectedClients();
			writeToAll("[Server: Client disconnected: " + user.getId() + "]");
			try {
				socket.close();
			} catch (Exception e) {
			}
		}

		/**
		 * Checks if an object (User or Conversation) already exists in the
		 * server. If not, adds the object to servers list and returns the
		 * object. Else returns the given object.
		 * 
		 * @param user
		 *            User or Conversation object.
		 * @return The object that's already saved on the server, or the given
		 *         object.
		 */
		public boolean isUserInDatabase(User user) {
			for (User u : registeredUsers) {
				if (u.getId().equals(user.getId())) {
					return true;
				}
			}
			return false;
		}

		public boolean isUserOnline(String id) {
			for (ConnectedClient client : connectedClients) {

				if (client.getUser().getId().equals(id) && client != this) {
					return true;
				}
			}
			return false;
		}

		public void updateConversation(HashSet<String> participants) {
			boolean exists = false;
			System.out.println("1");
			Conversation conversation = null;
			for (Conversation con : user.getConversations()) {
				if (con.getInvolvedUsers().equals(participants)) {
					conversation = con;
					exists = true;
				}

			}
			if (!exists) {
				conversation = new Conversation(participants);
			}
			addConversation(conversation);
			allConversations.add(conversation);
			sendConversation(conversation);
			System.out.println("2");

		}

		public void addConversation(Conversation con) {

            for (User user : registeredUsers) {

			    for (String ID : con.getInvolvedUsers()) {

					if (ID.equals(user.getId())) {
						user.addConversations(con);
					}
				}
			}
		}

		public void run() {
			Object object = null;
			Message message;
			User usr = null;
			try {
				object = ois.readObject();
				usr = (User) object;
				user = usr;

				while (isUserOnline(usr.getId())) {
					write("[Server: Client named " + usr.getId()
							+ " already connected - pick another one!]");
					object = ois.readObject();
					usr = (User) object;
				}

				user = usr;
				if (!isUserInDatabase(usr)) {
					registeredUsers.add(usr);
				} else {
					usr = getUser(usr.getId());
				}
				user = usr;

				oos.writeObject(user);
				server.writeToAll("[Server: Client connected: " + user.getId()
						+ "]");
				sendConnectedClients();

				while (!Thread.interrupted()) { /*
												 * Loops and checks for incoming
												 * Objects from User
												 */
					object = ois.readObject();
					if (object instanceof Message) {
						message = (Message) object;
						server.sendMessage(message);
					} else if (object instanceof Conversation) {
						Conversation con = (Conversation) object;
						oos.writeObject(con);
					} else if (object instanceof HashSet) {
						HashSet<String> participants = (HashSet<String>) object;
						updateConversation(participants);
					}
				}
			} catch (IOException e) {
				disconnectClient();
			} catch (ClassNotFoundException e2) {
			}
		}
	}
}