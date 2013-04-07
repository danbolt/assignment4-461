import java.io.*;
import java.net.*;
import java.security.*;
import java.util.*;

public class ChatServer 
{

  private static int port;

  public ArrayList<ClientMessage> clients = new ArrayList();

  public synchronized void addClient (ClientMessage client) 
  {
	  clients.add(client);
  }

  public synchronized void removeClient (ClientMessage client) 
  {
	  for (int i = 0; i < clients.size(); i++) 
          {
		  if (clients.get(i).ip.equals(client.ip) && clients.get(i).name.equals(client.name) && clients.get(i).port == client.port) 
                  {
			  clients.remove(i);
		  }
	  }

  }

  public synchronized ClientMessage getClient (int i) 
  {
	  return clients.get(i);
  }

  public synchronized boolean differentClient (ClientMessage info) 
  {
	  boolean different = true;

	  for (int i = 0; i < clients.size(); i++) 
          {
		  if (info.ip == clients.get(i).ip && info.name == clients.get(i).name && info.port == clients.get(i).port) 
                  {
			  different = false;
		  }
	  }

	  return different;
  }

  public ChatServer (int port) 
  {
	 this.port = port;

    try
    {
      ServerSocket listener = new ServerSocket(port);
      Socket server;

      while(true)
      {

        server = listener.accept();
        doComms conn_c= new doComms(server, this);
        Thread t = new Thread(conn_c);
        t.start();
      }
    } 
    catch (IOException ioe) 
    {
      System.out.println("IOException on socket listen: " + ioe);
      ioe.printStackTrace();
    }
  }

  // Listen for incoming connections and handle them
  public static void main(String[] args) 
  {
	  int p = 8000;
	  try 
          {
		  p = Integer.parseInt(args[0]);
	  } 
          catch (Exception e) 
          {
		  e.printStackTrace();
	  }
      ChatServer c = new ChatServer (p);
  }

}

class doComms implements Runnable 
{
    private Socket server;
    ChatServer c;
    ArrayList<ClientMessage> list = new ArrayList(); //Local list of clients

    OutputStream os;
	ObjectOutputStream out;
	InputStream is;
	ObjectInputStream in;

    doComms(Socket server, ChatServer chat) 
    {
      this.server=server;
      c = chat;
    }
    public void updateList () 
    {
		this.list = new ArrayList();
		for (int i = 0; i < c.clients.size(); i++) 
                {
			list.add(c.clients.get(i));
		}
    }

    public void run () 
    {


      try 
      {
        os = server.getOutputStream();
		out = new ObjectOutputStream(os);
		is = server.getInputStream();
		in = new ObjectInputStream(is);

        ClientMessage message;
        boolean listChanged;


		while (true) 
                {
			//Check for altered clients list
			listChanged = false;
			for (int i = 0; i < c.clients.size(); i++) 
                        {
				if (list.size() != c.clients.size() || !c.clients.get(i).equals(list.get(i))) 
                                {
					listChanged = true;
					this.updateList();
					break;
				}
			}

			if (listChanged) 
                        {
				out.writeObject(new ClientMessage("NEW", "" , 0));
				for (int i = 0; i < list.size(); i++) 
                                {
			            out.writeObject(list.get(i));
			            System.out.println("New client - sending data");
			            System.out.println(list.get(i).name);
				}
				out.writeObject(new ClientMessage("END", "" , 0));
			}


			//Receive from client
			message = (ClientMessage)in.readObject();

			if (message.removeThis == true) 
                        {
				c.removeClient(message);

				out.writeObject(new ClientMessage("CONNECTIONEND", "", 0));
				break;
			} 
                        else if (message.ip.equals("QUERY")) 
                        {
				out.writeObject(new ClientMessage("NONE", "", 0));
			} 
                        else if (c.differentClient(message)) 
                        {  //If new client (new client defined as having different ip/port/name than current clients)

			    //Add new client
			    c.addClient(message);
			    System.out.println("Client added");
		        }



		}

		in.close();
		out.close();
		server.close();
		return;
	  } 
          catch (Exception e) 
          {
        e.printStackTrace();
      }
    }
}