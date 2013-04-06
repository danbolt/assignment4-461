import java.net.*;
import java.io.*;
import java.util.*;


public class ClientConnection implements Runnable {
    InetAddress addr;
    InetAddress serverAddr;
    int port;
    ArrayList<ClientInfo> participants = new ArrayList(); //List containing information about other participants

    public ClientConnection (InetAddress addr, InetAddress serverAddr, int port) {
	    this.addr = addr;
	    this.port = port;
	    this.serverAddr = serverAddr;
    }

    public synchronized ArrayList getParticpants () {
		return participants;
	}

	public synchronized ClientInfo getParticipant (int i) {
		return participants.get(i);
	}

	public void addParticipant(ClientInfo c) {
		participants.add(c);
	}

	public void wipeParticipants () {
		participants = new ArrayList();
	}

	public synchronized void  processMessage(ObjectInputStream in) {
		try {
		    ClientInfo inMessage = (ClientInfo)in.readObject();

		    while (true) {

		        if (inMessage.ip.equals("END")) { //If server is done sending
		            break;
		        } else if (inMessage.ip.equals("NEW")) {
			        this.wipeParticipants();
		        } else if (inMessage.ip != null) {  //If new participant
			        this.addParticipant(inMessage);
		        }
	        }
	    } catch (Exception e) {
			e.printStackTrace();
		}
	}

    public void run() {
	    Socket socket = null;

		OutputStream os = null;
		ObjectOutputStream out = null;
		InputStream is = null;
		ObjectInputStream in = null;

		try{

					      serverAddr = InetAddress.getLocalHost();
					      socket = new Socket(addr, port);

					      os = socket.getOutputStream();
					      out = new ObjectOutputStream(os);

					      is = socket.getInputStream();
					      in = new ObjectInputStream(is);

					      while (true) {
							  processMessage(in);
					      }


					      //in.close();
					      //out.close();
					      //socket.close();
		} catch (Exception e) {
					      System.out.println("Exception");
					      e.printStackTrace();
	    }
    }
}

