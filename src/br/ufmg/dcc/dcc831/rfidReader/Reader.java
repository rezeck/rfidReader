package br.ufmg.dcc.dcc831.rfidReader;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;

public class Reader {
	
	private static AlienClass1Reader readerInstance;
	
	private Reader(){
		
	}
	
	public static AlienClass1Reader getReaderInstanceInitialized(){
		if (readerInstance == null){
			readerInstance = new AlienClass1Reader(Constants.READER2_IP_ADDRESS, Constants.READER2_PORT);
			readerInstance.setUsername(Constants.READER2_USERNAME);
			readerInstance.setPassword(Constants.READER2_PASSWORD);
		}
		return readerInstance;
	}

}
