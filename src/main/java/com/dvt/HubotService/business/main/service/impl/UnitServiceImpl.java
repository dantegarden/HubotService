package com.dvt.HubotService.business.main.service.impl;

import org.springframework.stereotype.Service;

import com.dvt.HubotService.business.main.service.UnitService;
import com.dvt.HubotService.commons.utils.HttpUtil;
@Service
public class UnitServiceImpl implements UnitService{

	@Override
	public String utterance(String accessToken, String scene_id,
			String session_id, String query) {
		// 请求URL
        String talkUrl = "https://aip.baidubce.com/rpc/2.0/solution/v1/unit_utterance";
        try {
            // 请求参数
            String params = "{\"scene_id\":%s,\"session_id\":\"%s\",\"query\":\"" + query + "\"}";
            params = String.format(params, scene_id, session_id);
            String result = HttpUtil.post(talkUrl, accessToken, "application/json", params);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}

}
