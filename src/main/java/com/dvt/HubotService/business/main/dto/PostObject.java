package com.dvt.HubotService.business.main.dto;

import java.util.HashMap;
import java.util.Map.Entry;

public class PostObject extends HashMap<String,Object> {
	
	public Entry getEntryFromMap(String key){
		for (Entry entry : this.entrySet()) {
			if(key.equals(entry.getKey())){
				return entry;
			}
		}
		return null;
	} 
}
