package br.ufmg.dcc.dcc831.rfidReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.UIManager;
import javax.swing.JList;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

public class RFIDGUI extends JFrame {
	
	private static RFIDGUI instance;
	
	private JPanel contentPane;
	private JList<String> listTagsSingle;	
	private JList<String> listTagsAutonomous;
	private JButton btnReadOnce;
	private JButton btnReadAutonomous;
	private JScrollPane scrollPaneTagsSingle;
	private JScrollPane scrollPaneTagsAutonomous;

	
	/**
	 * Create the frame.
	 */
	public RFIDGUI() {
		setTitle("DCC831 - RFID Reader");
		setResizable(false);
		setBackground(UIManager.getColor("Button.foreground"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 640, 480);
			
		contentPane = new JPanel();
		contentPane.setForeground(UIManager.getColor("Button.foreground"));
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		btnReadOnce = new JButton("Read Once");
		// Event Read Once
		btnReadOnce.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AlienClass1Reader reader = Reader.getReaderInstanceInitialized();
				SingleMode singleModeTester = new SingleMode(reader);
				Map<String, Integer> successRate;
				try {
					successRate = singleModeTester.getSucessRate();
					Vector<String> output = new Vector<String>();
					for (Map.Entry<String, Integer> entry : successRate.entrySet()){
						System.out.println(entry.getValue() + "% of Success Rate for " + entry.getKey());
						output.addElement(entry.getValue() + "% of Success Rate for " + entry.getKey());
					}
					listTagsSingle.setListData(output.toArray(new String[output.size()]));
				} catch (AlienReaderException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnReadOnce.setBounds(5, 5, 630, 25);
		contentPane.add(btnReadOnce);
		
		
		scrollPaneTagsSingle = new JScrollPane();
		scrollPaneTagsSingle.setBounds(5, 35, 630, 175);
		contentPane.add(scrollPaneTagsSingle);
		
		listTagsSingle = new JList<String>();
		scrollPaneTagsSingle.setViewportView(listTagsSingle);
		
		btnReadAutonomous = new JButton("Read Autonomous");
		// Event Read Autonomous
		btnReadAutonomous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AlienClass1Reader reader = Reader.getReaderInstanceInitialized();
				Vector<String> output = new Vector<String>();
				try {
					AutonomousMode autonomousModeTester = new AutonomousMode(reader);
					autonomousModeTester.startRead();
				} catch (IOException | AlienReaderException e1) {
					e1.printStackTrace();
				}
			}
		});
		btnReadAutonomous.setBounds(5, 240, 630, 25);
		contentPane.add(btnReadAutonomous);
		
		scrollPaneTagsAutonomous = new JScrollPane();
		scrollPaneTagsAutonomous.setBounds(5, 270, 630, 175);
		contentPane.add(scrollPaneTagsAutonomous);
		
		listTagsAutonomous = new JList<String>();
		scrollPaneTagsAutonomous.setViewportView(listTagsAutonomous);
	}
	
	public static RFIDGUI getInstance(){
		if (instance == null)
			instance = new RFIDGUI();
			instance.setVisible(true);
		return instance;
	}
}
