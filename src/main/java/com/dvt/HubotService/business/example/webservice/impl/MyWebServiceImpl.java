package com.dvt.HubotService.business.example.webservice.impl;

import com.dvt.HubotService.business.example.webservice.MyWebService;

public class MyWebServiceImpl implements MyWebService {

	@Override
	public String SayHello(String name) {
		return "HelloWorld!! " + name;
	}
	
}
