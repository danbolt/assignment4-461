
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

	BroadcasterGUI PP = null;
	
	Player player = null;
	Processor processor = null;

	ConferenceClient rootApplication = null;
	
	DataSource output = null;
	
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
			player.addControllerListener(new PlayerListener(PP, this, player, processor));
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
		synchronized (this)
		{
			if (player != null)
			{
				player.stop();
				player.close();
				player = null;
			}
			
			if (processor != null)
			{
				processor.stop();
				processor.close();
				processor = null;
				
				for (int i = 0; i < managers.length; i++)
				{
					if (managers[i] != null)
					{
						managers[i].removeTargets("Session ended.");
						managers[i].dispose();
						managers[i] = null;
					}
				}
			}
		}
		
		if (PP != null)
		{
			rootApplication.basePanel.remove(PP);
			PP = null;
		}
	}
	
	private void perror(String s)
	{
		System.out.println("BAD ERROR: " + s);
		System.exit(-1);
	}
	
	void setImageQuality(Player p, float value)
	{
		Control[] cs = p.getControls();
		QualityControl qc = null;
		VideoFormat jpegFmt = new VideoFormat(VideoFormat.JPEG);
		
		for (int i = 0; i < cs.length; i++)
		{
			if (cs[i] instanceof QualityControl && cs[i] instanceof Owned)
			{
				Object owner = ((Owned)cs[i]).getOwner();

				if (owner instanceof Codec)
				{
					Format fmts[] = ((Codec)owner).getSupportedOutputFormats(null);
					
					for (int j = 0; j < fmts.length; j++)
					{
						if (fmts[j].matches(jpegFmt))
						{
							qc = (QualityControl)cs[i];
							qc.setQuality(value);
							System.out.println("-- setting JPEG quality at " + value + " on " + qc);
							break;
						}
					}
				}
				
				if (qc != null)
				{
					break;
				}
			}
		}
	}

	public synchronized void controllerUpdate (ControllerEvent event)
	{
		if (processor == null)
		{
			return;
		}
		
		System.out.println("ControllerEvent: " + event.toString());
		if (event instanceof ConfigureCompleteEvent)
		{
			TrackControl[] tracks = processor.getTrackControls();
			
			if (tracks == null || tracks.length < 1)
			{
				perror("couldn't find any tracks in the processor");
			}
			
			boolean programmed = false;
			
			for (int i = 0; i < tracks.length; i++)
			{
				Format format = tracks[i].getFormat();
				
				if (tracks[i].isEnabled() && format instanceof VideoFormat && !programmed)
				{
					Dimension size = ((VideoFormat)format).getSize();
					float frameRate = ((VideoFormat)format).getFrameRate();
					int w = (size.width % 8 == 0 ? size.width : (int)(size.width / 8) * 8);
					int h = (size.height % 8 == 0 ? size.height : (int)(size.height) * 8);

					VideoFormat jpegFormat = new VideoFormat(VideoFormat.JPEG_RTP, new Dimension(w, h), Format.NOT_SPECIFIED, Format.byteArray, frameRate);
					
					tracks[i].setFormat(jpegFormat);
					System.out.println("Video will be transmitted as:");
					System.out.println("	" + jpegFormat);
					
					programmed = true;
				}
			}
			
			if (!programmed)
			{
				perror("Couldn't find any video tracks available");
			}	
			
			ContentDescriptor cd = new ContentDescriptor(ContentDescriptor.RAW_RTP);
			processor.setContentDescriptor(cd);
			
			processor.realize();
			player.realize();
		}
		else if (event instanceof RealizeCompleteEvent)
		{
			//SET THE JPEG QUALITY
			setImageQuality(processor, mediaQuality);
			
			output = processor.getDataOutput();

			PushBufferDataSource pbds = (PushBufferDataSource)output;
			PushBufferStream pbss[] = pbds.getStreams();
			
			managers = new RTPManager[pbss.length];
			SessionAddress localAddr;
			SessionAddress destAddr;
			InetAddress ipAddr;
			SendStream sendStream;
			int lport;
			int dport;
			SourceDescription srcDesList[];
			
			for (int i = 0; i < pbss.length; i++)
			{
				try
				{
					managers[i] = RTPManager.newInstance();

					dport = port+1 + 2*i;
					lport = port-1 - 2*i;
					ipAddr = InetAddress.getByName(ipAddress);

					localAddr = new SessionAddress(InetAddress.getLocalHost(), lport);
					destAddr = new SessionAddress(ipAddr, dport, 1);
					
					managers[i].initialize(localAddr);
					managers[i].addTarget(destAddr);
					
					System.out.println("Created RTP session: " + ipAddress + ":" + dport);
					
					sendStream = managers[i].createSendStream(output, i);
					sendStream.start();
				}
				catch (Exception e)
				{
					//
				}
			}
		}
		else if (event instanceof PrefetchCompleteEvent)
		{
			PP = new BroadcasterGUI(player);
			rootApplication.basePanel.add("Center", PP);
			rootApplication.validate();
			
			processor.start();
			player.start();
		}
		else if (event instanceof EndOfMediaEvent)
		{
			this.stop();
		}
		else if (event instanceof ControllerErrorEvent)
		{
			processor = null;
			perror(((ControllerErrorEvent)event).getMessage());
		}
		else if (event instanceof ControllerClosedEvent)
		{
			//
		}
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


