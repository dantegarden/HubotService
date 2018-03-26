package com.dvt.HubotService.commons.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.google.common.collect.Lists;

public class ZipUtil {
	@Test
	public void testMethod() throws IOException, ServletException{
		
	}
	/**
	 * 将多个文件打成zip包
	 * @param files 文件列表
	 * @param zipFile 目标zip文件对象
	 * **/
	 public static void zipFile(List<File> files, File zipFile) throws IOException,ServletException{
		 FileOutputStream outStream = null;
		 ZipOutputStream toZip = null;
		 try {
			 if(zipFile.exists()){
				 zipFile.delete();
			 }
			 outStream = new FileOutputStream(zipFile);
		     toZip = new ZipOutputStream(outStream);
			 int size = files.size();
			 for(int i = 0; i < size; i++){
				 File file = (File) files.get(i);
				 zipFile(file, toZip);
			 }
		} catch (IOException e) {
			throw e;
		} finally{
			if(toZip!=null){
				toZip.close();
			}
			if(outStream!=null){
				outStream.close();
			}
		}
	 }
	
	 public static void zipFile(File inputFile, ZipOutputStream outputstream) throws IOException,ServletException{
	       try{
	           if(inputFile.exists())
	           {
	              if(inputFile.isFile())
	              {
	                 FileInputStream inStream = new FileInputStream(inputFile);
	                 BufferedInputStream bInStream = new BufferedInputStream(inStream);
	                 ZipEntry entry = new ZipEntry(inputFile.getName());
	                 outputstream.putNextEntry(entry);
	                  
	                 final int MAX_BYTE = 10 * 1024 *1024;    //最大的流为10M
	                 long streamTotal = 0;                      //接受流的容量
	                 int streamNum = 0;                      //流需要分开的数量
	                 int leaveByte = 0;                      //文件剩下的字符数
	                 byte[] inOutbyte;                          //byte数组接受文件的数据
	                 
	                 streamTotal = bInStream.available();                        //通过available方法取得流的最大字符数
	                 streamNum = (int)Math.floor(streamTotal / MAX_BYTE);    //取得流文件需要分开的数量
	                 leaveByte = (int)streamTotal % MAX_BYTE;                //分开文件之后,剩余的数量
	                 
	                 if (streamNum > 0) 
	                 {
	                     for(int j = 0; j < streamNum; ++j)
	                     {
	                         inOutbyte = new byte[MAX_BYTE];
	                         //读入流,保存在byte数组
	                         bInStream.read(inOutbyte, 0, MAX_BYTE);
	                         outputstream.write(inOutbyte, 0, MAX_BYTE);  //写出流
	                      }
	                 }
	                 //写出剩下的流数据
	                 inOutbyte = new byte[leaveByte];
	                 bInStream.read(inOutbyte, 0, leaveByte);
	                 outputstream.write(inOutbyte);
	                 outputstream.closeEntry();     //Closes the current ZIP entry and positions the stream for writing the next entry
	                 bInStream.close();    //关闭
	                 inStream.close();
	            }
	         }
	         else
	         {
	             throw new ServletException("文件不存在！");
	         }
	     }
	     catch(IOException e)
	     {
	         throw e;
	     }
	}
	 
	/**
	 * 递归压缩文件夹
	 * 
	 * @param srcRootDir
	 *            压缩文件夹根目录的子路径
	 * @param file
	 *            当前递归压缩的文件或目录对象
	 * @param zos
	 *            压缩文件存储对象
	 * @throws Exception
	 */
	private static void zip(String srcRootDir, File file, ZipOutputStream zos)
			throws Exception {
		if (file == null) {
			return;
		}

		// 如果是文件，则直接压缩该文件
		if (file.isFile()) {
			int count, bufferLen = 1024;
			byte data[] = new byte[bufferLen];

			// 获取文件相对于压缩文件夹根目录的子路径
			String subPath = file.getAbsolutePath();
			int index = subPath.indexOf(srcRootDir);
			if (index != -1) {
				subPath = subPath.substring(srcRootDir.length()
						+ File.separator.length());
			}
			ZipEntry entry = new ZipEntry(subPath);
			zos.putNextEntry(entry);
			BufferedInputStream bis = new BufferedInputStream(
					new FileInputStream(file));
			while ((count = bis.read(data, 0, bufferLen)) != -1) {
				zos.write(data, 0, count);
			}
			bis.close();
			zos.closeEntry();
		}
		// 如果是目录，则压缩整个目录
		else {
			// 压缩目录中的文件或子目录
			File[] childFileList = file.listFiles();
			for (int n = 0; n < childFileList.length; n++) {
				childFileList[n].getAbsolutePath().indexOf(
						file.getAbsolutePath());
				zip(srcRootDir, childFileList[n], zos);
			}
		}
	}

	/**
	 * 对文件或文件目录进行压缩
	 * 
	 * @param srcPath
	 *            要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath
	 *            压缩文件保存的路径。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param zipFileName
	 *            压缩文件名
	 * @throws Exception
	 */
	public static void zip(String srcPath, String zipPath, String zipFileName)
			throws Exception {
		if (StringUtils.isEmpty(srcPath) || StringUtils.isEmpty(zipPath)
				|| StringUtils.isEmpty(zipFileName)) {
			throw new RuntimeException("参数不对");
		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try {
			File srcFile = new File(srcPath);

			// 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory() && zipPath.indexOf(srcPath) != -1) {
				throw new RuntimeException("压缩文件不能保存在源文件路径下");
			}

			// 判断压缩文件保存的路径是否存在，如果不存在，则创建目录
			File zipDir = new File(zipPath);
			if (!zipDir.exists() || !zipDir.isDirectory()) {
				zipDir.mkdirs();
			}

			// 创建压缩文件保存的文件对象
			String zipFilePath = zipPath + File.separator + zipFileName;
			File zipFile = new File(zipFilePath);
			if (zipFile.exists()) {
				// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
				SecurityManager securityManager = new SecurityManager();
				securityManager.checkDelete(zipFilePath);
				// 删除已存在的目标文件
				zipFile.delete();
			}

			cos = new CheckedOutputStream(new FileOutputStream(zipFile),
					new CRC32());
			zos = new ZipOutputStream(cos);

			// 如果只是压缩一个文件，则需要截取该文件的父目录
			String srcRootDir = srcPath;
			if (srcFile.isFile()) {
				int index = srcPath.lastIndexOf(File.separator);
				if (index != -1) {
					srcRootDir = srcPath.substring(0, index);
				}
			}
			// 调用递归压缩方法进行目录或文件压缩
			zip(srcRootDir, srcFile, zos);
			zos.flush();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 对文件目录进行压缩
	 * 
	 * @param srcPath
	 *            要压缩的源文件目录路径
	 *            压缩后的文件名是 目录名.zip
	 *            默认压缩到源文件目录路径的父目录
	 */
	public static void zip(String srcPath)
			throws Exception {
		if (StringUtils.isEmpty(srcPath)) {
			throw new RuntimeException("参数不对");
		}
		CheckedOutputStream cos = null;
		ZipOutputStream zos = null;
		try {
			File srcFile = new File(srcPath);
			// 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
			if (srcFile.isDirectory()){
				String zipFileName = srcFile.getName()+".zip";
				String zipPath = srcFile.getParent();
				System.out.println();
				
				// 创建压缩文件保存的文件对象
				String zipFilePath = zipPath + File.separator + zipFileName;
				File zipFile = new File(zipFilePath);
				if (zipFile.exists()) {
					// 检测文件是否允许删除，如果不允许删除，将会抛出SecurityException
					SecurityManager securityManager = new SecurityManager();
					securityManager.checkDelete(zipFilePath);
					// 删除已存在的目标文件
					zipFile.delete();
				}

				cos = new CheckedOutputStream(new FileOutputStream(zipFile),
						new CRC32());
				zos = new ZipOutputStream(cos);

				// 如果只是压缩一个文件，则需要截取该文件的父目录
				String srcRootDir = srcPath;
				if (srcFile.isFile()) {
					int index = srcPath.lastIndexOf(File.separator);
					if (index != -1) {
						srcRootDir = srcPath.substring(0, index);
					}
				}
				// 调用递归压缩方法进行目录或文件压缩
				zip(srcRootDir, srcFile, zos);
				zos.flush();
			}
			
			
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (zos != null) {
					zos.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
