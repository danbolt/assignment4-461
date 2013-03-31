
import java.awt.event.*;
import java.awt.*;

import javax.media.*;
import javax.media.protocol.*;
import javax.media.rtp.RTPManager;
import javax.media.rtp.SendStream;
import javax.media.rtp.SessionAddress;
import javax.media.rtp.rtcp.SourceDescription;
import javax.media.format.*;
import javax.media.control.TrackControl;
import javax.media.control.QualityControl;
import java.io.*;
import java.net.InetAddress;

public class ClientBroadcaster implements ControllerListener
{
	private MediaLocator locator = null;
	private String ipAddress = null;
	
	private RTPManager managers[] = null;
	
	private int port;
	
	private float mediaQuality;
	
	Player player = null;
	Processor processor = null;
	
	ConferenceClient rootApplication = null;
	
	public ClientBroadcaster (MediaLocator locator, String ip, int portBase, float quality, ConferenceClient parent)
	{
		this.locator = locator;
		ipAddress = ip;
		port = portBase - 1;
		mediaQuality = quality;
		rootApplication = parent;
		managers = new RTPManager[2];
		managers[0] = null;
		managers[1] = null;
	}
	
	public synchronized void start ()
	{
		//
	}
	
	public void stop()
	{
		//
	}
	
	private void perror(String s)
	{
		System.out.println("PERROR: " + s);
		System.exit(-1);
	}

	public synchronized void controllerUpdate (ControllerEvent event)
	{
		//
	}
}