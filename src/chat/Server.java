package chat;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
	private ArrayList<Conversation> conversationList;

	public Server(int port) {
		registeredUsers = new ArrayList<>();
		connectedClients = new ArrayList<>();
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
		if (message.getTo().isToAll()) {
			writeToAll(message);
		} else {
			Conversation conversation = message.getTo();
			for (User user : conversation.getUserList()) {
				for (ConnectedClient client : connectedClients) {
					if (client.getUser() == user) {
						client.write(message);
					}
				}
			}
		}
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
			server.writeToAll("Client connected: " + socket.getInetAddress());
		}

		public User getUser() {
			return user;
		}

		public void write(Object object) {
			try {
				oos.writeObject(object);
			} catch (IOException e) {
			}
		}

        public void removeConnectedClient() {
            for (int i = 0; i < connectedClients.size(); i++) {
                if (connectedClients.get(i).getUser().getId().equals(this.getUser().getId())) {
                    connectedClients.remove(i);
                    System.out.println("Client removed from connectedClients");
                }
            }
        }

        public void disconnectClient() {
            removeConnectedClient();
            sendConnectedClients();
            writeToAll("Client disconnted: " + user.getId());
            try {
                socket.close();
            } catch (Exception e) {}
        }

		/**
		 * Checks if an object (User or Conversation) already exists in the
		 * server. If not, adds the object to servers list and returns the
		 * object. Else returns the given object.
		 *
		 * @param object
		 *            User or Conversation object.
		 * @return The object that's already saved on the server, or the given
		 *         object.
		 */
		public Object checkObject(Object object) {
			if (object instanceof User) {
				User user = (User) object;
				for (User u : registeredUsers) {
					if (u.getId().equals(user.getId())) {
                        this.user = u;
						return u;
					}
				}
				this.user = user;
				registeredUsers.add(user);
			} else if (object instanceof Conversation) {
				Conversation con = (Conversation) object;
				for (Conversation c : conversationList) {
					if (c.getId() == con.getId()) {
						return c;
					}
				}
			}
			return object;
		}

		public void run() {
			Object object;
			Message message;
			try {
				object = ois.readObject();
				object = checkObject(object);
				if (object instanceof User) {
					User user = (User) object;
					oos.writeObject(user);
					sendConnectedClients();
				}
				while (!Thread.interrupted()) {
					object = ois.readObject();
					if (object instanceof Message) {
						message = (Message) object;
						server.sendMessage(message);
					} else if (object instanceof Conversation) {
						Conversation con = (Conversation) object;
						oos.writeObject(con);
					}
				}
			} catch (IOException e) {
//                System.out.println("Client killed");
                disconnectClient();
//                writeToAll("TJENA LOL");
            } catch (ClassNotFoundException e2) {}
		}
	}

}