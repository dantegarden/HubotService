package com.dvt.HubotService.business.main.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface SampleService {
	public List<String> generateSample(File sampleDefinedFile) throws IOException;
	
	public List<String> generateSampleV2(File sampleDefinedFile) throws IOException;
}
