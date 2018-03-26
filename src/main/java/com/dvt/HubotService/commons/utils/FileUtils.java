package com.dvt.HubotService.commons.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dvt.HubotService.commons.vo.DownloadRecord;


/**
 * @author Acore
 *
 */
public class FileUtils extends org.apache.commons.io.FileUtils{
	
	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	
	/**
     * 是否存在
     * @param file
     * @return
     */
    public static boolean isExists(File file){
        return file!=null&&file.exists();
    }
	 /**
     * 输出文件
     * @param file
     * @throws IOException 
     */
    public static void outputFile(File file,OutputStream stream) throws IOException{
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            bis = new BufferedInputStream(new FileInputStream(file));
            bos = new BufferedOutputStream(stream);
            while ((bytesRead = bis.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            bos.flush();
        } finally {
            bis.close();
            bos.close();
        }
    }
	/**
     * 
     * @param file
     * @param fileName
     * @throws IOException
     */
    public static  void downloadFile(File file,String fileName,HttpServletRequest request,HttpServletResponse response) throws IOException{
        // 弹出下载对话框(以附件形式打开文件流)
        String agent = (String) request.getHeader("USER-AGENT");
        if (agent != null && agent.toUpperCase().indexOf("FIREFOX") >-1) {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" +  
                    new String(fileName.getBytes("utf-8"),"ISO8859_1"));
        } else {
            response.setHeader( 
                    "Content-Disposition", 
                    "attachment; filename=" +
                            toUtf8String(fileName));
        }
 
        OutputStream stream=response.getOutputStream();
        outputFile(file, stream);
    }
    /**
     * @param request
     * @param response
     * @param fileName
     * @return
     * @throws IOException
     */
    public static OutputStream initDownload(HttpServletRequest request,HttpServletResponse response,String fileName) throws IOException{
    	 // 弹出下载对话框(以附件形式打开文件流)
        String agent = (String) request.getHeader("USER-AGENT");
        if (agent != null && agent.toUpperCase().indexOf("FIREFOX") >-1) {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" +  
                    new String(fileName.getBytes("utf-8"),"ISO8859_1"));
        } else {
            response.setHeader( 
                    "Content-Disposition", 
                    "attachment; filename=" +
                            toUtf8String(fileName));
        }
        response.setCharacterEncoding("UTF-8");
        return response.getOutputStream();
    }
    /**
     * 转码
     * @param s
     * @return
     */
    private static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i=0;i<s.length();i++) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) k += 256;
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
 
    }
	/**
     * 关闭资源
     * @param is
     * @param os
     */
    public static void close(InputStream is,OutputStream os){
        if(is!=null){
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if(os!=null){
            try {
                os.close();
            } catch (IOException e) {
            }
        }
    }
    
    /**
     * 写字符串到文件
     * */
    public static void writeStr2File(String input,String filepath){
    	FileWriter fw = null;
    	BufferedWriter bw = null;
    	File file = new File(filepath);
    	try {
    		if(!file.exists()){
        		file.createNewFile();
        	}
    		fw = new FileWriter(filepath, true);
    		bw = new BufferedWriter(fw);
    		if(input.indexOf(System.getProperty("line.separator"))>-1){
    			String[] x = input.split(System.getProperty("line.separator"));
    			for (int i = 0; i < x.length; i++) {
					bw.write(x[i]);
					bw.write(System.getProperties().getProperty("line.separator"));
				}
    		}else{
    			bw.write(input);
    		}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(bw!=null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fw!=null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    	
    }
    
    /**
     * 下载文件
     * */
    public static void downloadFile(File file,HttpServletRequest request, HttpServletResponse response){
    	//声明本次下载状态的记录对象
    	DownloadRecord downloadRecord = new DownloadRecord(file.getName(), file.getAbsolutePath(), request);
    	//用于记录以完成的下载的数据量，单位是byte
    	long downloadedLength = 0l;
    	
    	BufferedInputStream bis = null;  
        BufferedOutputStream bos = null; 
        
        try {
        	//设置响应头和客户端保存文件名
        	response.setCharacterEncoding("utf-8");
        	response.setContentType("application/octet-stream;charset=UTF-8");
        	response.setHeader("Content-Disposition", "attachment; filename=\"" + new String(file.getName().getBytes("utf-8"),"iso-8859-1") + "\"");
        	response.addHeader("Content-Length", "" + file.length());  
        	if(logger.isDebugEnabled()){
                logger.debug("创建输入流读取文件...");
            }
        	
        	bis = new BufferedInputStream(new FileInputStream(file));
        	bos = new BufferedOutputStream(response.getOutputStream());
        	
        	//定义缓冲池大小，开始读写
            byte[] buff = new byte[2048];  
            int bytesRead;  
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
              bos.write(buff, 0, bytesRead);  
            }
            
            //刷新缓冲，写出
            bos.flush();
            if(logger.isDebugEnabled()){
                logger.debug("文件下载成功。");
            }
            
		} catch (Exception e) {
			logger.error("文件下载失败"+e.getMessage());
		} finally {
			if(bis != null){
	            try {
	                bis.close();
	            } catch (IOException e) {
	                logger.error("关闭输入流失败，"+e.getMessage());
	                e.printStackTrace();
	            }  
	        }
	        if(bis != null){
	            try {
	                bos.close();
	            } catch (IOException e) {
	                logger.error("关闭输出流失败，"+e.getMessage());
	                e.printStackTrace();
	            }
	        }
		}
    	
    }
    
    /**
     * 读取文件内容，作为字符串返回
     */
    public static String readFileAsString(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } 

        if (file.length() > 1024 * 1024 * 1024) {
            throw new IOException("File is too large");
        } 

        StringBuilder sb = new StringBuilder((int) (file.length()));
        // 创建字节输入流  
        FileInputStream fis = new FileInputStream(filePath);  
        // 创建一个长度为10240的Buffer
        byte[] bbuf = new byte[10240];  
        // 用于保存实际读取的字节数  
        int hasRead = 0;  
        while ( (hasRead = fis.read(bbuf)) > 0 ) {  
            sb.append(new String(bbuf, 0, hasRead));  
        }  
        fis.close();  
        return sb.toString();
    }

    /**
     * 根据文件路径读取byte[] 数组
     */
    public static byte[] readFileByBytes(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath);
        } else {
            ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
            BufferedInputStream in = null;

            try {
                in = new BufferedInputStream(new FileInputStream(file));
                short bufSize = 1024;
                byte[] buffer = new byte[bufSize];
                int len1;
                while (-1 != (len1 = in.read(buffer, 0, bufSize))) {
                    bos.write(buffer, 0, len1);
                }

                byte[] var7 = bos.toByteArray();
                return var7;
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                } catch (IOException var14) {
                    var14.printStackTrace();
                }

                bos.close();
            }
        }
    }
}
