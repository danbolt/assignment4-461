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

public class ConferenceClient extends JFrame implements ActionListener
{
	ClientBroadcaster stream = null;
	ClientReceiver receiver = null;
	
	public JComponent basePanel;
	
	/* FOR TEMPORARY TESTING REMOVE THIS LATER */
	
	private boolean sending;
	
	/* END OF TEMP TESTING CODE (LOLOLOL AGILE SCRUM PROTOTYPING ETC) */

	public ConferenceClient (boolean isSending)
	{
		super("Hermes");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		sending = isSending;

		this.setSize(300, 300);
		initalize();
	}

	public void initalize ()
	{
		basePanel = new JPanel(false);
		basePanel.setLayout(new BorderLayout());
		add(basePanel);

		if (sending)
		{
			stream = new ClientBroadcaster(new MediaLocator("file:samples/test-mpeg.mpg"), "224.0.0.100", 9000, 0.5f, this);
			stream.start();
		}
		else
		{
			String[] sessions = new String[2];
			sessions[0] = "224.0.0.100" + '/' + 9000;
			sessions[1] = "224.0.0.100" + '/' + (9000+2);
			
			int bufferSize = 350;

			receiver = new ClientReceiver(sessions, bufferSize);
			if (!(receiver.initalize()))
			{
				System.out.println("FAILED to initialize the sessions.");
				System.exit(-1);
			}

			//receiver.p.start();

		}

		this.setVisible(true);
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
