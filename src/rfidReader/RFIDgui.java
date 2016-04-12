package rfidReader;

import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.Color;

import javax.swing.AbstractListModel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.lang.System;
import java.net.Inet4Address;
import java.net.InetAddress;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerService;
import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;

import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class RFIDgui implements MessageListener{
	
	private String ipAddress;

	private JFrame frame;
	private AlienClass1Reader reader = new AlienClass1Reader();
	private MessageListenerService service = new MessageListenerService(4000);
	String listTag[] = new String[]{};
	
	JLabel readValue = new JLabel("0");
	JLabel successValue = new JLabel("0");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					RFIDgui window = new RFIDgui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public RFIDgui() throws AlienReaderException, Exception{
		InetAddress localHost = Inet4Address.getLocalHost();
		this.ipAddress = localHost.getHostName();
		System.out.println(this.ipAddress);
		alienReaderInitialize();
		initialize();
	}
	
	/**
	 * A single Message has been received from a Reader.
	 *
	 * @param message the notification message received from the reader
	 */
	public void messageReceived(Message message){
	  System.out.println("\nMessage Received:");
	  if (message.getTagCount() == 0) {
	    System.out.println("(No Tags)");
	  } else {
		listTag = new String[message.getTagCount()];
		
	    for (int i = 0; i < message.getTagCount(); i++) {
	      Tag tag = message.getTag(i);
	      listTag[i] = tag.toLongString() + "ok";
	      System.out.println("Read rate: " + tag.getRenewCount() + "/sec");
	      System.out.println(tag.toLongString());
	      System.out.println();
	    }
	  }
	}
	
	/**
	 * Initialize the contents of the Alien Reader.
	 * @throws AlienReaderConnectionException 
	 * @throws AlienReaderTimeoutException 
	 * @throws AlienReaderNotValidException 
	 * @throws AlienReaderConnectionRefusedException 
	 */
	private void alienReaderInitialize() throws AlienReaderConnectionRefusedException, AlienReaderNotValidException, AlienReaderTimeoutException, AlienReaderConnectionException, Exception{
		 // To connect to a networked reader instead, use the following:
		
		 // Reader Alien ALR9650: 150.164.10.41 (alien9650.winet.dcc.ufmg.br) Port 23
		 // Reader Alien ALR9900: 150.164.10.42 (alien9900.winet.dcc.ufmg.br) Port 23
		  service.setMessageListener(this);
		  service.startService();
		  System.out.println("Message Listener has Started");

		  reader.setConnection("150.164.10.42", 23);
		  reader.setUsername("alien");
		  reader.setPassword("password");
		  System.out.println("Configuring Reader");
		  
		 

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws Exception{
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBackground(UIManager.getColor("Button.foreground"));
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);
		
		JButton btnReadOnce = new JButton("Read Once");
		JButton btnReadAutonomous = new JButton("Read Autonomous");
		JScrollPane scrollPane = new JScrollPane();
		JList<String> listRFIDSingle = new JList<String>();
		JScrollPane scrollPane_1 = new JScrollPane();
		JList<String> listRFIDAutonomous = new JList<String>();
				
		btnReadOnce.addActionListener(new ActionListener() { // Read one-shot
			public void actionPerformed(ActionEvent e) {
				btnReadAutonomous.setEnabled(false);
				// Open a connection to the reader
				try {
					reader.open();
				} catch (AlienReaderNotValidException
						| AlienReaderTimeoutException
						| AlienReaderConnectionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Ask the reader to read tags and print them
				Tag tagList[];
				try {
					tagList = reader.getTagList();
					String[] list = new String[tagList.length];
					for (int i = 0; i < tagList.length; i++){
						list[i] = tagList[i].toLongString();
					}
					listRFIDSingle.setListData(list);
				} catch (AlienReaderException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				// Close the connection
				reader.close();
				btnReadAutonomous.setEnabled(true);
			}
		});
		btnReadOnce.setBounds(12, 20, 300, 25);
		frame.getContentPane().add(btnReadOnce);
		
		scrollPane.setBounds(12, 57, 300, 344);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setViewportView(listRFIDSingle);
		listRFIDSingle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		
		scrollPane_1.setBounds(326, 57, 300, 344);
		frame.getContentPane().add(scrollPane_1);
		
		
		scrollPane_1.setViewportView(listRFIDAutonomous);
		
		
		btnReadAutonomous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 // Set up Notification
				  // Use your local ip
				 btnReadOnce.setEnabled(false);
				  try {
						reader.open();

						// Setup with your ip address
						reader.setNotifyAddress(ipAddress, 4000);
						reader.setNotifyFormat(AlienClass1Reader.XML_FORMAT); // Make sure service can decode it.
						reader.setNotifyTrigger("TrueFalse"); // Notify whether there's a tag or not
						reader.setNotifyMode(AlienClass1Reader.ON);
						
						// Set up AutoMode
						reader.autoModeReset();
						reader.setAutoStopTimer(1000); // Read for 1 second
						reader.setAutoMode(AlienClass1Reader.ON);
						
						// Close the connection and spin while messages arrive
						reader.close();
						long runTime = 10000; // milliseconds
						long startTime = System.currentTimeMillis();
						do {
							  try {
								Thread.sleep(1000);
								listRFIDAutonomous.setListData(listTag);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						} while(service.isRunning() && (System.currentTimeMillis()-startTime) < runTime);
						  
						  // Reconnect to the reader and turn off AutoMode and TagStreamMode.
						  System.out.println("\nResetting Reader");
						  reader.open();
						  reader.autoModeReset();
						  reader.setNotifyMode(AlienClass1Reader.OFF);
						  reader.close();
				} catch (AlienReaderException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				btnReadOnce.setEnabled(true);
			}
		});
		btnReadAutonomous.setBounds(326, 20, 300, 25);
		frame.getContentPane().add(btnReadAutonomous);
		
		JLabel lblReadRate = new JLabel("Read rate: ");
		lblReadRate.setForeground(Color.WHITE);
		lblReadRate.setFont(new Font("Dialog", Font.BOLD, 12));
		lblReadRate.setBounds(326, 413, 87, 15);
		frame.getContentPane().add(lblReadRate);
		
		JLabel lblSuccessRate = new JLabel("Success rate: ");
		lblSuccessRate.setForeground(Color.WHITE);
		lblSuccessRate.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSuccessRate.setBounds(326, 431, 114, 15);
		frame.getContentPane().add(lblSuccessRate);
		
		readValue.setForeground(Color.WHITE);
		readValue.setBounds(446, 413, 70, 15);
		frame.getContentPane().add(readValue);
		
		successValue.setForeground(Color.WHITE);
		successValue.setBounds(446, 431, 70, 15);
		frame.getContentPane().add(successValue);
		
		/*
		listRFIDSingle.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Tag tag = listRFIDSingle.getSelectedValue();
				
				String list = "ID:" + tag.getTagID() +
                        "\nDiscovered:" + tag.getDiscoverTime() +
                        "\nLast Seen:" + tag.getRenewTime() +
                        "\nAntenna:" + tag.getAntenna() +
                        "\nCRC:" + tag.getCRC() +
                        "\nReads:" + tag.getRenewCount();
				
				JOptionPane.showMessageDialog(null, list);
			}
		});
		*/
		
		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
