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

class ReceiverGUI extends Panel
{
	Component vc, cc;
	
	ReceiverGUI (Player pl)
	{
		setLayout(new BorderLayout());
		if ((vc = pl.getVisualComponent()) != null)
		{
			add("Center", vc);
		}
	}
	
	public Dimension getPreferredSize()
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
			h += size.height;
		}
		
		if (w < 160)
		{
			w = 160;
		}
		
		return new Dimension(w, h);
	}
}


/// THE NEXT CLASS WAS NOT WRITTEN BY DANIEL SAVAGE
/// IT WAS COPIED FROM THE EXAMPLE SOLUTION FOR
/// ASSIGNMENT 2 PROVIDED BY THE LAB INSTRUCTOR
///
/// IT'S PURPOSE IS ALMOST ENTIRELY FOR STRING PARSING,
/// AND WAS CONSIDERED BY THE STUDENT TO BE TRIVIAL IN NATURE

    /**
     * A utility class to parse the session addresses.
     */
    class SessionLabel {

	public String addr = null;
	public int port;
	public int ttl = 1;

	SessionLabel(String session) throws IllegalArgumentException {

	    int off;
	    String portStr = null, ttlStr = null;

	    if (session != null && session.length() > 0) {
		while (session.length() > 1 && session.charAt(0) == '/')
		    session = session.substring(1);

		// Now see if there's a addr specified.
		off = session.indexOf('/');
		if (off == -1) {
		    if (!session.equals(""))
			addr = session;
		} else {
		    addr = session.substring(0, off);
		    session = session.substring(off + 1);
		    // Now see if there's a port specified
		    off = session.indexOf('/');
		    if (off == -1) {
			if (!session.equals(""))
			    portStr = session;
		    } else {
			portStr = session.substring(0, off);
			session = session.substring(off + 1);
			// Now see if there's a ttl specified
			off = session.indexOf('/');
			if (off == -1) {
			    if (!session.equals(""))
				ttlStr = session;
			} else {
			    ttlStr = session.substring(0, off);
			}
		    }
		}
	    }

	    if (addr == null)
		throw new IllegalArgumentException();

	    if (portStr != null) {
		try {
		    Integer integer = Integer.valueOf(portStr);
		    if (integer != null)
			port = integer.intValue();
		} catch (Throwable t) {
		    throw new IllegalArgumentException();
		}
	    } else
		throw new IllegalArgumentException();

	    if (ttlStr != null) {
		try {
		    Integer integer = Integer.valueOf(ttlStr);
		    if (integer != null)
			ttl = integer.intValue();
		} catch (Throwable t) {
		    throw new IllegalArgumentException();
		}
	    }
	}
    }
    
/// COPIED CLASS ENDS HERE
