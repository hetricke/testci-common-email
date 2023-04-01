package org.apache.commons.mail;

import java.util.Map;

public class EmailConcrete extends Email {
	
	@Override
	public Email setMsg(String msg) throws EmailException {		
		return null;
	}
	
	//method added to check if headers are set appropriately
	public Map<String, String> getHeaders(){
		return headers;
	}
}
