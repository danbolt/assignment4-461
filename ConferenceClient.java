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

	public ConferenceClient ()
	{
		super("Hermes");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setSize(300, 300);
		initalize();
	}
	
	public void initalize ()
	{
		this.setVisible(true);
	}
	
	public void actionPerformed (ActionEvent e)
	{
		//
	}

	public static void main (String[] args)
	{
		new ConferenceClient();
	}
}
