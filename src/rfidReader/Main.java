package rfidReader;

import java.lang.System;

import com.alien.enterpriseRFID.reader.*;
import com.alien.enterpriseRFID.tags.*;


public class Main {
	
	public Main() throws AlienReaderException {

		  AlienClass1Reader reader = new AlienClass1Reader();
		  //reader.setConnection("COM1");

		  // To connect to a networked reader instead, use the following:
		
		  reader.setConnection("150.164.10.41", 23);
		  reader.setUsername("alien");
		  reader.setPassword("password");
		
		  // Leitor Alien ALR9650: 150.164.10.41 (alien9650.winet.dcc.ufmg.br) Porta 23
		  // Leitor Alien ALR9900: 150.164.10.42 (alien9900.winet.dcc.ufmg.br) Porta 23
		  
 		  // Open a connection to the reader
		  reader.open();

		  Tag tagList[] = reader.getTagList();
		  if (tagList == null) {
		    System.out.println("No Tags Found");
		  } else {
		    System.out.println("Tag(s) found:");
		    for (int i=0; i<tagList.length; i++) {
		      Tag tag = tagList[i];
		      System.out.println("ID:" + tag.getTagID() +
		                         ", Discovered:" + tag.getDiscoverTime() +
		                         ", Last Seen:" + tag.getRenewTime() +
		                         ", Antenna:" + tag.getAntenna() +
		                         ", Reads:" + tag.getRenewCount()
		                         );
		    }
		  }

		  // Close the connection
		  reader.close();
		}
	
	public static final void main(String args[]){
		System.out.println("Teste");
		try {
		    new Main();
		  } catch(AlienReaderException e) {
		    System.out.println("Error: " + e.toString());
		  }
	}
}
