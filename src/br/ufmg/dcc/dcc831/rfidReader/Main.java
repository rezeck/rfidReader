package br.ufmg.dcc.dcc831.rfidReader;

import com.alien.enterpriseRFID.reader.AlienReaderException;

public class Main {
	public static void main(String args[]) throws AlienReaderException{
		RFIDReaderGUI.getInstance();
	}
}