package com.dvt.HubotService.business.main.service;

import com.dvt.HubotService.business.main.dto.AuthDTO;

public interface AuthService {
	
	public String getAuth();
	public String getAuth(String ak, String sk);
	public String getAuthToken();
}
