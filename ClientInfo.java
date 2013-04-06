import java.net.*;
import java.io.*;

public class ClientInfo implements Serializable {
    public String ip;
    public String name;
    public int port;

    public ClientInfo(String ip, String name, int port){
	        this.ip = ip;
	        this.name = name;
	        this.port = port;
    }


    public boolean equals(ClientInfo info) {
		if (info.ip == this.ip && info.name == this.name && info.port == this.port) {
			return true;
		} else {
			return false;
		}
	}
}