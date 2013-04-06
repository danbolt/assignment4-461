import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class ChatServer {

  private static int port=8000, maxConnections=0;
  public ArrayList<ClientInfo> clients = new ArrayList();


  public synchronized void addClient (ClientInfo client) {
	  clients.add(client);
  }

  public synchronized boolean removeClient (ClientInfo client) {
	  return clients.remove(client);
  }

  public synchronized ClientInfo getClient (int i) {
	  return clients.get(i);
  }

  public synchronized boolean differentClient (ClientInfo info) {
	  boolean different = true;

	  for (int i = 0; i < clients.size(); i++) {
		  if (info.ip == clients.get(i).ip && info.name == clients.get(i).name && info.port == clients.get(i).port) {
			  different = false;
		  }
	  }

	  return different;
  }

  public ChatServer () {
	 int i=0;

    try{
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while((i++ < maxConnections) || (maxConnections == 0)){
        //doComms connection;

        server = listener.accept();
        doComms conn_c= new doComms(server, this);
        Thread t = new Thread(conn_c);
        t.start();
      }
    } catch (IOException ioe) {
      System.out.println("IOException on socket listen: " + ioe);
      ioe.printStackTrace();
    }
  }

  // Listen for incoming connections and handle them
  public static void main(String[] args) {
      ChatServer c = new ChatServer ();
  }

}

class doComms implements Runnable {
    private Socket server;
    ChatServer c;
    ArrayList<ClientInfo> list = new ArrayList();

    doComms(Socket server, ChatServer chat) {
      this.server=server;
      c = chat;
    }

    public void run () {


      try {
        OutputStream os = server.getOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(os);
		InputStream is = server.getInputStream();
		ObjectInputStream in = new ObjectInputStream(is);

        ClientInfo message;
        boolean listChanged;


		while (true) {
			//Check for altered clients list
			listChanged = false;
			for (int i = 0; i < c.clients.size(); i++) {
				if (list.size() != c.clients.size() || !c.clients.get(i).equals(list.get(i))) {
					listChanged = true;
					break;
				}
			}

			if (listChanged) {
				out.writeObject(new ClientInfo("NEW", "" , 0));
				for (int i = 0; i < list.size(); i++) {
			        out.writeObject(list.get(i));
				}
				out.writeObject(new ClientInfo("END", "" , 0));
			}


			//Receive from client
			message = (ClientInfo)in.readObject();

			if (c.differentClient(message)) {  //If new client (new client defined as having different ip/port/name than current clients)

			    //Add new client
			    c.addClient(message);
		    }



		}

        //server.close();
      } catch (Exception e) {
        System.out.println("Exception!");
        e.printStackTrace();
      }
    }
}