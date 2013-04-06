import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
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
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	
	private JTabbedPane tabs = null;
	
	private JComponent clientsPanel;
	private JTable clientsTable;
	public JComponent basePanel;

	//table data for other clients
	String[] columns = {"Name", "Multicast IP", "Port"};
	String[][] rowData = {{"foo", "test", "test"}};
	
	private boolean sending;

	/* END OF TEMP TESTING CODE (LOLOLOL AGILE SCRUM PROTOTYPING ETC) */
	
	private ArrayList<ClientReceiver> receiverList = null;
	
	private ArrayList<ClientInfo> infoList = null;

	public ConferenceClient (boolean isSending)
	{
		super("Hermes ::: " + System.getProperty("user.name"));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		receiverList = new ArrayList<ClientReceiver>();
		infoList = new ArrayList<ClientInfo>();
		
		infoList.add(new ClientInfo("224.0.0.100", "finn", 9000));
		infoList.add(new ClientInfo("224.0.0.101", "jake", 9000));

		sending = isSending;

		this.setSize(300, 300);
		initalize();
	}
	
	private void fillRefreshTable (ArrayList<ClientInfo> clients)
	{
		if (clients == null)
		{
			return;
		}

		ArrayList<String[]> formattedRows = new ArrayList<String[]>();
		
		for (ClientInfo c : clients)
		{
			String[] row = {c.name, c.ip, Integer.toString(c.port)};
			formattedRows.add(row);
		}
		
		String[][] finalRows = {{"test", "test", "test"}};
		finalRows = formattedRows.toArray(finalRows);
		
		rowData = finalRows;
		
		DefaultTableModel newModel = new DefaultTableModel(rowData, columns);
		clientsTable.setModel(newModel);
	}

	public void initalize ()
	{
		tabs = new JTabbedPane();

		basePanel = new JPanel(false);
		basePanel.setLayout(new BorderLayout());
		tabs.addTab("Your stream", null, basePanel, "A preview window of your multicast video feed.");
		
		clientsPanel = new JPanel(false);
		clientsPanel.setLayout(new BorderLayout());
		clientsTable = new JTable(rowData, columns)
		{
			public boolean isCellEditable(int row, int column)
			{
				return false;
			}
		};
		clientsTable.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 2)
				{
					JTable target = (JTable)e.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					
					//System.out.println("ROW: " + clientsTable.getValueAt(row, 0));
				}
			}
		});
		clientsPanel.add(clientsTable, BorderLayout.CENTER);
		fillRefreshTable(infoList);
		tabs.addTab("Clients", null, clientsPanel, "Listing of other available clients on the network");

		add(tabs);

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
		if ("refillTable".equals(e.getActionCommand()))
		{
			fillRefreshTable(infoList);
		}
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
