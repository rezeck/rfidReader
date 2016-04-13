package br.ufmg.dcc.dcc831.rfidReader;

import java.util.HashMap;
import java.util.Map;

import com.alien.enterpriseRFID.reader.AlienClass1Reader;
import com.alien.enterpriseRFID.reader.AlienReaderException;
import com.alien.enterpriseRFID.tags.Tag;

public class SingleMode {

	private AlienClass1Reader reader;

	
	public SingleMode(AlienClass1Reader r){
		if (r == null){
			return;
		}
		reader = r;
	}
	
	public Map<String, Integer> getSucessRate() throws AlienReaderException{
		int readAttempts = 20;
		reader.open();
		Map<String, Integer> counter = new HashMap<String, Integer>();
		for (int m = 0; m < readAttempts; m++){
			Tag[] tagList = reader.getTagList();
			for (int i = 0; i < tagList.length; i++){
				Tag tag = tagList[i];
				int oldCounter = 0;
				if (counter.containsKey(tag.getTagID()))
					oldCounter = counter.get(tag.getTagID());
				counter.put(tag.getTagID(), oldCounter + 1);
			}
		}
		
		// calculate success rate
		for (Map.Entry<String, Integer> entry : counter.entrySet()){
			String tagID = entry.getKey();
			int readTimes = entry.getValue();
			int sucessRate = readTimes * 100 / readAttempts;
			counter.put(tagID, sucessRate);
		}
		reader.close();
		
		return counter;
	}
	
	public Tag[] getTagList() throws AlienReaderException{
		reader.open();
		Tag[] tagList = reader.getTagList();
		reader.close();
		return tagList;
	}

	public AlienClass1Reader getReader(){
		return reader;
	}
}