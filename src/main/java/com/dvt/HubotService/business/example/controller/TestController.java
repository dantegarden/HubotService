package com.dvt.HubotService.business.example.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dvt.HubotService.business.example.service.TestService;
import com.dvt.HubotService.commons.GlobalConstants;
import com.dvt.HubotService.commons.utils.XmlRpcUtils;
import com.dvt.HubotService.commons.utils.XmlUtils;
import com.dvt.HubotService.commons.vo.DownloadRecord;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;



@Controller
@RequestMapping("/test")
public class TestController {
	private static final Logger logger = LoggerFactory.getLogger(TestController.class);
	
	@Autowired
	private TestService testService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping
	public String init(HttpServletRequest request) {
		System.out.println("test initting");
		return "server start successfully!";
	}
	
	
}
