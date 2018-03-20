package com.dvt.HubotService.business.main.slot.global;

import java.util.List;

import com.dvt.HubotService.business.main.dto.HubotInterface;
import com.dvt.HubotService.business.main.dto.QueryBO;

public interface UserDefinedSlotForCondition {
	public List<String> dispose(HubotInterface myHif, QueryBO qbo);
}
