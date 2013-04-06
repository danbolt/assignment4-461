import java.net.*;
import java.io.*;
import java.util.*;

public class Tester {
    public static void main(String [] args) {
		try{
        ClientConnection c = new ClientConnection(InetAddress.getByName("224.0.0.1"), InetAddress.getLocalHost(), 8000, "Bob");
        Thread t = new Thread(c);
        t.start();
        Thread.sleep(2000);
        System.out.println("interrupt!");
        t.interrupt();
	} catch (Exception e) {

	}

    }
}