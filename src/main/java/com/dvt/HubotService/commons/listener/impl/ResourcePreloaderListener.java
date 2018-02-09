package com.dvt.HubotService.commons.listener.impl;

import org.springframework.context.ApplicationContext;

import com.dvt.HubotService.commons.listener.AbstractResourcePreloaderListener;

public class ResourcePreloaderListener extends AbstractResourcePreloaderListener{
	
	@Override
	protected void preload(ApplicationContext applicationContext) {
		initDataDict(applicationContext);
	}

	private void initDataDict(ApplicationContext applicationContext) {
		System.out.println("~~~~perload~~~~");
	}


}
