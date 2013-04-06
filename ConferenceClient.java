import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.media.ControllerListener;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.rtp.ReceiveStream;

import java.util.ArrayList;

public class ConferenceClient extends JFrame implements ActionListener
{
	ClientBroadcaster stream = null;
	ClientReceiver receiver = null;

	public JComponent basePanel;
	
	/* FOR TEMPORARY TESTING REMOVE THIS LATER */
	
	private boolean sending;
	
	/* END OF TEMP TESTING CODE (LOLOLOL AGILE SCRUM PROTOTYPING ETC) */
	
	private ArrayList<ClientReceiver> receiverList = null;

	public ConferenceClient (boolean isSending)
	{
		super("Hermes ::: " + System.getProperty("user.name"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		receiverList = new ArrayList<ClientReceiver>();
		
		sending = isSending;

		this.setSize(300, 300);
		initalize();
	}

	public void initalize ()
	{
		basePanel = new JPanel(false);
		basePanel.setLayout(new BorderLayout());
		add(basePanel);

		this.setVisible(true);
		
		String ipA = "224.0.0.100";
		int portA = 9000;
		
		String ipB = "224.0.0.200";
		int portB = 10000;
		
		if (sending)
		{
			stream = new ClientBroadcaster(new MediaLocator("file:samples/test-mpeg.mpg"), ipA, portA, 0.5f, this);
			stream.start();
	
			String[] sessions = new String[2];
			sessions[0] = ipB + '/' + portB;
			sessions[1] = ipB + '/' + (portB+2);
			
			int bufferSize = 350;
	
			receiver = new ClientReceiver(sessions, bufferSize);
			if (!(receiver.initalize()))
			{
				System.out.println("FAILED to initialize a session");
				System.exit(-1);
			}
			
			receiverList.add(receiver);
		}
		else
		{
			stream = new ClientBroadcaster(new MediaLocator("file:samples/test-mpeg.mpg"), ipB, portB, 0.5f, this);
			stream.start();
	
			String[] sessions = new String[2];
			sessions[0] = ipA + '/' + portA;
			sessions[1] = ipA + '/' + (portA+2);
			
			int bufferSize = 350;
	
			receiver = new ClientReceiver(sessions, bufferSize);
			if (!(receiver.initalize()))
			{
				System.out.println("FAILED to initialize a session");
				System.exit(-1);
			}
			
			receiverList.add(receiver);
		}
	}
	
	public void actionPerformed (ActionEvent e)
	{
		//
	}

	public static void main (String[] args)
	{
		if ("send".equals(args[0]))
		{
			new ConferenceClient(true);
		}
		else if ("recv".equals(args[0]))
		{
			new ConferenceClient(false);
		}
	}
}
