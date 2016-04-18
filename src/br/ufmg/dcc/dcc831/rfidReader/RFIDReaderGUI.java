package br.ufmg.dcc.dcc831.rfidReader;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderException;

public class RFIDReaderGUI extends JFrame{
	
	private static RFIDReaderGUI instance;
	
	private JFrame frame;
	private JLabel readRateValue;
	private JLabel successRateValue;
	private JList<String> listTagsSingle;	
	private JList<String> listTagsAutonomous;
	private JButton btnReadOnce;
	private JButton btnReadAutonomous;
	private JScrollPane scrollPaneTagsSingle;
	private JScrollPane scrollPaneTagsAutonomous;
	private JLabel lblReadRate;
	private JLabel lblSuccessRate;
	
	
	private RFIDReaderGUI() {
		// Instance all objects
		frame = new JFrame();
		listTagsSingle = new JList<String>();		
		listTagsAutonomous = new JList<String>();
		btnReadOnce = new JButton("Read Once");
		scrollPaneTagsSingle = new JScrollPane();
		scrollPaneTagsAutonomous = new JScrollPane();
		btnReadAutonomous = new JButton("Read Autonomous");
		lblReadRate = new JLabel("Read rate: ");
		readRateValue = new JLabel("0");
		lblSuccessRate = new JLabel("Success rate: ");
		successRateValue = new JLabel("0");		
		
		// Set frame
		frame.setTitle("DCC831 - RFID Reader");		
		frame.setResizable(false);
		frame.setBackground(UIManager.getColor("Button.foreground"));
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		//frame.getContentPane().setLayout(null);
		
		// Set Single Mode area
		btnReadOnce.setBounds(12, 20, 300, 25);
		frame.getContentPane().add(btnReadOnce);
		scrollPaneTagsSingle.setBounds(12, 57, 300, 344);
		frame.getContentPane().add(scrollPaneTagsSingle);
		scrollPaneTagsSingle.setViewportView(listTagsSingle);
		listTagsSingle.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		// Set Autonomous Mode area
		btnReadAutonomous.setBounds(326, 20, 300, 25);
		frame.getContentPane().add(btnReadAutonomous);		
		scrollPaneTagsAutonomous.setBounds(326, 57, 300, 344);
		frame.getContentPane().add(scrollPaneTagsAutonomous);
		scrollPaneTagsAutonomous.setViewportView(listTagsAutonomous);
		
		
		// Read Rate label
		lblReadRate.setForeground(Color.WHITE);
		lblReadRate.setFont(new Font("Dialog", Font.BOLD, 12));
		lblReadRate.setBounds(326, 413, 87, 15);
		frame.getContentPane().add(lblReadRate);
		
		// Success Rate	label
		lblSuccessRate.setForeground(Color.WHITE);
		lblSuccessRate.setFont(new Font("Dialog", Font.BOLD, 12));
		lblSuccessRate.setBounds(326, 431, 114, 15);
		frame.getContentPane().add(lblSuccessRate);
		
		// Read Rate value
		readRateValue.setForeground(Color.WHITE);
		readRateValue.setBounds(446, 413, 70, 15);
		frame.getContentPane().add(readRateValue);
		
		// Read Rate value
		successRateValue.setForeground(Color.WHITE);
		successRateValue.setBounds(446, 431, 70, 15);
		frame.getContentPane().add(successRateValue);		
		
		// Event Read Once
		btnReadOnce.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AlienClass1Reader reader = Reader.getReaderInstanceInitialized();
				SingleMode singleModeTester = new SingleMode(reader);
				Map<String, Integer> successRate;
				try {
					successRate = singleModeTester.getSucessRate();
					for (Map.Entry<String, Integer> entry : successRate.entrySet()){
						System.out.println(entry.getValue() + "% of Success Rate for " + entry.getKey());
						
					}					
				} catch (AlienReaderException e1) {
					e1.printStackTrace();
				}
			}
			
		});
		
		
		// Event Read Autonomous
		btnReadAutonomous.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				AlienClass1Reader reader = Reader.getReaderInstanceInitialized();
				try {
					AutonomousMode autonomousModeTester = new AutonomousMode(reader);
					autonomousModeTester.startRead();
				} catch (IOException | AlienReaderException e1) {
					e1.printStackTrace();
				}
			}
		});

		frame.setBounds(100, 100, 640, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static RFIDReaderGUI getInstance(){
		if (instance == null)
			instance = new RFIDReaderGUI();
		return instance;
	}

}