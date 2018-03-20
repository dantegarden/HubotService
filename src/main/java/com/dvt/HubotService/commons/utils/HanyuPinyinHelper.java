package com.dvt.HubotService.commons.utils;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinFormat;
import com.github.stuxuhai.jpinyin.PinyinHelper;

public class HanyuPinyinHelper {
	/**
     * 转换为有声调的拼音字符串
     * @param pinYinStr 汉字
     * @return 有声调的拼音字符串
     */
    public static String changeToMarkPinYin(String pinYinStr){
        String tempStr = null;
        try{
            tempStr =  PinyinHelper.convertToPinyinString(pinYinStr,  " ", PinyinFormat.WITH_TONE_MARK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return tempStr;

    }
	/**
     * 转换为数字声调字符串
     * @param pinYinStr 需转换的汉字
     * @return 转换完成的拼音字符串
     */
    public static String changeToNumberPinYin(String pinYinStr){
        String tempStr = null;
        try {
            tempStr = PinyinHelper.convertToPinyinString(pinYinStr, " ", PinyinFormat.WITH_TONE_NUMBER);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempStr;
    }
	/**
     * 转换为不带音调的拼音字符串
     * @param pinYinStr 需转换的汉字
     * @return 拼音字符串
     */
    public static String changeToTonePinYin(String pinYinStr){
        String tempStr = null;
        try{
            tempStr =  PinyinHelper.convertToPinyinString(pinYinStr, " ", PinyinFormat.WITHOUT_TONE);
        } catch (Exception e){
            e.printStackTrace();
        }
        return tempStr;
    }
    
    /**
     * 检查汉字是否为多音字
     * @param pinYinStr 需检查的汉字
     * @return true 多音字，false 不是多音字
     */
    public static boolean checkPinYin(char pinYinStr){
        boolean check  = false;
        try{
            check = PinyinHelper.hasMultiPinyin(pinYinStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }
    
    /**
     * 简体转换为繁体
     * @param pinYinStr
     * @return
     */
    public static String changeToTraditional(String pinYinStr){

        String tempStr = null;
        try {
            tempStr = ChineseHelper.convertToTraditionalChinese(pinYinStr);
        } catch (Exception e){
            e.printStackTrace();
        }
        return tempStr;
    }
    
    /**
     * 繁体转换为简体
     * @param pinYinSt
     * @return
     */
    public static String changeToSimplified(String pinYinSt){
        String tempStr = null;
        try{
            tempStr = ChineseHelper.convertToSimplifiedChinese(pinYinSt);
        } catch (Exception e){
            e.printStackTrace();
        }
        return tempStr;
    }
    
    /**
     * 转换为每个汉字对应拼音首字母字符串
     * @param pinYinStr 需转换的汉字
     * @return 拼音字符串
     */
    public static String changeToGetShortPinYin(String pinYinStr){
        String tempStr = null;
        try {
            tempStr = PinyinHelper.getShortPinyin(pinYinStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempStr;
    }
    
    public static boolean comparePinyin(String chineseStr1,String chineseStr2){
    	boolean isEqual = Boolean.FALSE;
    	String pinyinStr1 = HanyuPinyinHelper.changeToMarkPinYin(chineseStr1);//库里存的
    	String pinyinStr2 = HanyuPinyinHelper.changeToMarkPinYin(chineseStr2);//说的
    	if(StringUtils.equals(pinyinStr1,pinyinStr2)){
    		isEqual = Boolean.TRUE;
    	}else{
    		char[] chineseCharArray1 = chineseStr1.toCharArray();//中文字符集
    		char[] chineseCharArray2 = chineseStr2.toCharArray();//中文字符集
    		if(chineseCharArray1.length==chineseCharArray2.length){//库里存的和说的一样长
    			boolean flag[] = new boolean[chineseCharArray1.length];
    			Arrays.fill(flag, false);
    			
    			for (int i=0;i<chineseCharArray1.length;i++) {
    				if(chineseCharArray1[i]==chineseCharArray2[i]){
    					flag[i] = true;
    					continue;
    				}else if(HanyuPinyinHelper.checkPinYin(chineseCharArray1[i])){//可能是多音字
    					String[] duoyin = PinyinHelper.convertToPinyinArray(chineseCharArray1[i]);
    					String[] pinyin2 = pinyinStr2.split(" ");
    					for (int j = 0; j < duoyin.length; j++) {
    						if(duoyin[j].equals(pinyin2[i])){
    							flag[i] = true;
    							break;
    						}
    					}
    				}
    			}
    			
    			int countTrue = 0;
    			for (int i = 0; i < flag.length; i++) {
					if(flag[i]){
						countTrue++;
					}
				}
    			if(countTrue==flag.length){
    				isEqual = Boolean.TRUE;
    			}
    		}
    	}
    	return isEqual;
    }
    
    @Test
    public void test(){
    	System.out.println(HanyuPinyinHelper.comparePinyin("朱长道", "朱常道"));
    	System.out.println(HanyuPinyinHelper.changeToMarkPinYin("李倞"));
    }
}
