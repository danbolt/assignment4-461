
// this overall class design is almost entirely based of the example solutions
// for Assignment 2; none of the code below was copy/pasted, but may be almost
// identically typed.


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
		if (locator == null)
		{
			perror("Media Locator is null");
		}
		
		DataSource sourceA = null;
		DataSource sourceB = null;
		
		try
		{
			sourceA = Manager.createDataSource(locator);
			sourceB = Manager.createDataSource(locator);
		}
		catch (Exception e)
		{
			perror("failed to create DataSource objects");
		}
		
		try
		{
			processor = Manager.createProcessor(sourceA);
			player = Manager.createPlayer(sourceB);
		}
		catch (NoProcessorException npe)
		{
			perror("failed to create processor for media");
		}
		catch (Exception ioe)
		{
			perror("IOException when creating processors");
		}
		
		try
		{
			//player.addControllerListener(new PlayerListener(PP, this, player, processor));
			processor.addControllerListener(this);
			
			processor.configure();
		}
		catch (Exception npe)
		{
			perror("could not configure processor");
		}
	}

	public void stop()
	{
		//
	}
	
	private void perror(String s)
	{
		System.out.println("BAD ERROR: " + s);
		System.exit(-1);
	}

	public synchronized void controllerUpdate (ControllerEvent event)
	{
		//
	}
}

class BroadcasterGUI extends Panel
{
	Component vc, cc;
	
	BroadcasterGUI (Player p)
	{
		setLayout (new BorderLayout());
		
		if ((vc = p.getVisualComponent()) != null)
		{
			add("Center", vc);
		}
	}
	
	public Dimension getPrefferedSize()
	{
		int w = 0;
		int h = 0;
		
		if (vc != null)
		{
			Dimension size = vc.getPreferredSize();
			w = size.width;
			h = size.height;
		}
		
		if (cc != null)
		{
			Dimension size = cc.getPreferredSize();
			
			if (w == 0)
			{
				w = size.width;
			}
			
			h = size.height;
		}
		
		if (w < 160)
		{
			w = 160;
		}
		
		return new Dimension(w, h);
	}
}

class PlayerListener implements ControllerListener
{
	BroadcasterGUI PP;
	ClientBroadcaster mediaSender;
	Player P;
	Processor PR;
	
	public PlayerListener (BroadcasterGUI PP, ClientBroadcaster MS, Player P, Processor PR)
	{
		this.PP = PP;
		this.mediaSender = MS;
		this.P = P;
		this.PR = PR;
	}

	public synchronized void controllerUpdate(ControllerEvent event)
	{
		if (event instanceof RealizeCompleteEvent)
		{
			PR.prefetch();
		}
		else if (event instanceof EndOfMediaEvent)
		{
			mediaSender.stop();
		}
	}
}


