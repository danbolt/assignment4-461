import java.net.*;
import java.io.*;
import java.util.*;


public class ClientConnection implements Runnable 
{
    InetAddress addr;
    InetAddress serverAddr;
    int port;
    String name;
    ArrayList<ClientMessage> participants = new ArrayList(); //List containing information about other participants

    Socket socket;
    OutputStream os;
    ObjectOutputStream out;
    InputStream is;
    ObjectInputStream in;

    public ClientConnection (InetAddress addr, InetAddress serverAddr, int port, String name) 
    {
	    this.addr = addr;
	    this.port = port;
	    this.serverAddr = serverAddr;
	    this.name = name;
    }

    public synchronized ArrayList getParticpants () 
    {
		return participants;
    }

	public synchronized ClientMessage getParticipant (int i) 
        {
		return participants.get(i);
	}

	public void addParticipantToList(ClientMessage c) 
        {
		participants.add(c);
	}

	public void wipeParticipants () 
        {
		participants = new ArrayList();
	}

	public synchronized void processMessage(ObjectOutputStream out, ObjectInputStream in) 
        {
		try 
                {
		    ClientMessage inMessage;

		    while (true) 
                    {
				out.writeObject(new ClientMessage("QUERY", "", 0)); //Query the server (this gives readObject something to do and prevents blocking)

                                inMessage = (ClientMessage)in.readObject();

		        if (inMessage.ip.equals("NONE")) 
                        {
					break;
			} 
                        else if (inMessage.ip.equals("END")) 
                        { //If server is done sending

		            break;
		        } 
                        else if (inMessage.ip.equals("NEW")) 
                        {
			        this.wipeParticipants();

		        } 
                        else if (inMessage.ip != null) 
                        {  //If new participant
			        this.addParticipantToList(inMessage);
			        System.out.println(inMessage.name);
		        }
	        }

	    } 
            catch (Exception e) 
            {
		e.printStackTrace();
	    }
	}

    public void run() 
    {
	    socket = null;

	    os = null;
	    out = null;
	    is = null;
	    in = null;

		try
                {


					      socket = new Socket(serverAddr, port);

					      os = socket.getOutputStream();
					      out = new ObjectOutputStream(os);

					      is = socket.getInputStream();
					      in = new ObjectInputStream(is);

					      out.writeObject(new ClientMessage(addr.toString().substring(1), name, port));
					      System.out.println("Sending");

					      while (true) 
                                              {

							  if (Thread.interrupted()) 
                                                          { //Thread has been told to close
							      ClientMessage end = new ClientMessage(addr.toString().substring(1), name, port);
							      end.removeThis = true;
							      out.writeObject(end);

								  ClientMessage inMessage;

								  while (true) 
                                                                  {

                                                                      inMessage = (ClientMessage)in.readObject();
								      if (inMessage.ip.equals("CONNECTIONEND")) 
                                                                      {
									      return;
								      }
							      }
							  }
							  processMessage(out, in);
					      }

		} 
                catch (Exception e) 
                {
			System.out.println("Exception");
			e.printStackTrace();
	    }
    }
}

