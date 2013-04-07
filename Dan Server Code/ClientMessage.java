import java.net.*;
import java.io.*;

public class ClientMessage implements Serializable 
{
    public String ip;
    public String name;
    public int port;
    public boolean removeThis;

    public ClientMessage(String ip, String name, int port)
    {
        this.ip = ip;
        this.name = name;
        this.port = port;
        this.removeThis = false;
    }


    public boolean equals(ClientMessage info)
    {
        if (info.ip == this.ip && info.name == this.name && info.port == this.port) 
        {
	    return true;
        } 
        else 
        {
            return false;
        }
    }
}