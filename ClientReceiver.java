import java.io.*;
import java.awt.*;
import java.net.*;
import java.awt.event.*;
import java.util.Vector;

import javax.media.*;
import javax.media.rtp.*;
import javax.media.rtp.event.*;
import javax.media.rtp.rtcp.*;
import javax.media.protocol.*;
import javax.media.protocol.DataSource;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.Format;
import javax.media.format.FormatChangeEvent;
import javax.media.control.BufferControl;


/**
 * MediaReceiver to receive RTP transmission using the RTPManagers.
 */
public class ClientReceiver implements ReceiveStreamListener, SessionListener,ControllerListener
{
	public ClientReceiver (/* FIND CUTE ARGUMENTS FOR THIS*/)
	{
		//
	}
	
	protected boolean initalize ()
	{
		return false;
	}
	
	protected void close ()
	{
		//
	}
	
	// interface update functions
	
	public synchronized void update (SessionEvent evt)
	{
		//
	}
	
	public synchronized void update (ReceiveStreamEvent evt)
	{
		//
	}
	
	public synchronized void controllerUpdate(ControllerEvent ce)
	{
		//
	}
}
