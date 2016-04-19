package br.ufmg.dcc.dcc831.rfidReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import javax.swing.JList;

import com.alien.enterpriseRFID.notify.Message;
import com.alien.enterpriseRFID.notify.MessageListener;
import com.alien.enterpriseRFID.notify.MessageListenerService;
import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.tags.Tag;

public class AutonomousMode implements MessageListener{
	
	private AlienClass1Reader reader;
	private MessageListenerService service;
	
	private String localAddress;
	private int localPort;
	
	public AutonomousMode(AlienClass1Reader r) throws UnknownHostException, SocketException {
		if (r == null)
			return;
		reader = r;
		
		localPort = Constants.LOCAL_NOTIFY_PORT;
		
		// Set local address to return messages
		for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();){
		    NetworkInterface intf = en.nextElement();
		    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ){
		    	InetAddress ip = enumIpAddr.nextElement();
		    	if (!ip.isSiteLocalAddress() && ip.getHostAddress().contains(".")){
		    		localAddress = ip.getHostAddress();
		    		break;
		    	}
		    }
		    if (localAddress != null)
		    	break;
		}
		
		
		
		service = new MessageListenerService(localPort);
	}

	@Override
	public void messageReceived(Message message) {
		System.out.println("Uma leitura foi recebida...");
		RFIDGUI gui = RFIDGUI.getInstance();
		JList<String> list = gui.getListTagsAutonomous();
		Tag[] tagList = message.getTagList();
		String[] tagListString = new String[tagList.length];
		for (int i = 0; i < tagList.length; i++){
			tagListString[i] = tagList[i].getTagID();
			System.out.println(tagList[i].getTagID());
		}
		list.setListData(tagListString);
	}
	
	public void startRead() throws IOException, AlienReaderException{
		System.out.println("Messages will be sent to " + localAddress + ":" + localPort);
		service.setMessageListener(this);
		System.out.println("Starting socket on port " + localPort);
		service.startService();
		reader.open();
		reader.setNotifyAddress(localAddress, localPort);
		reader.setNotifyFormat(AlienClass1Reader.XML_FORMAT);
		reader.setNotifyTrigger("TrueFalse");
		reader.setNotifyMode(AlienClass1Reader.ON);
		
		// Set up AutoMode
		reader.autoModeReset();
		reader.setAutoStopTimer(1000); // Read for 1 second
		reader.setAutoMode(AlienClass1Reader.ON);

		// Wait for messages...
		long startTime = System.currentTimeMillis();
		do {
			  try {
				Thread.sleep(Constants.AUTONOMOUS_MODE_READER_INTERVAL);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} while(service.isRunning() && (System.currentTimeMillis()-startTime) < Constants.AUTONOMOUS_MODE_RUN_TIME);		

		System.out.println("Stopping socket...");
		service.stopService();
		
		// Reset reader
		reader.open();
		reader.autoModeReset();
		reader.setNotifyMode(AlienClass1Reader.OFF);
		reader.close();		
	}
	
}
