import java.net.*;
import java.io.*;


public class SampleClient {
    public static void main (String [] args) {
		InetAddress addr;
		int port = 8000;

		Socket socket = null;

		OutputStream os = null;
		ObjectOutputStream out = null;
		InputStream is = null;
		ObjectInputStream in = null;

        try{
		  //addr = InetAddress.getByName("127.0.0.1");
		  addr = InetAddress.getLocalHost();
	      socket = new Socket(addr, port);

	      os = socket.getOutputStream();
	      out = new ObjectOutputStream(os);

	      is = socket.getInputStream();
	      in = new ObjectInputStream(is);

	      ClientInfo outMessage = new ClientInfo("127.0.0.1", "Dan", 8000);

	      out.writeObject(outMessage);


	      ClientInfo inMessage;
	      while (true) {
			  inMessage = (ClientInfo)in.readObject();

			  if (inMessage.ip.equals("END")) {
			      break;
		      }

			  System.out.println("Client: " + inMessage.ip + "/" + inMessage.port + " " + inMessage.name);
	      }

          in.close();
	      out.close();
	      socket.close();
        } catch (Exception e) {
	      System.out.println("Exception");
	      e.printStackTrace();
        }
    }
}