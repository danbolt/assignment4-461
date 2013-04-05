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

public class ReceiverWindow extends JFrame implements ActionListener
{
	public JComponent basePanel;
	
	public ReceiverWindow(String windowTitle)
	{
		super(windowTitle);
		
		basePanel = new JPanel(false);
		basePanel.setLayout(new BorderLayout());
		add(basePanel);
		
		this.setSize(300, 300);
		// init function?
		this.setVisible(true);
	}
	
	protected void initalize()
	{
		//
	}
	
	public void actionPerformed (ActionEvent e)
	{
		//
	}
}