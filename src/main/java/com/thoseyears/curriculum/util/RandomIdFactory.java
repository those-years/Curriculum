package com.thoseyears.curriculum.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class RandomIdFactory {
	private static final long UserLength = 4;
	private static final long CIdLength = 7;

	public static String nowDate() {
		SimpleDateFormat time= new SimpleDateFormat("yyyyMMdd");
    	Date date = new Date();//
        String nowtime = time.format(date);
        return nowtime;
	}
	/**
	* @author:  ljx
	* @methodsName: getWorkLogId
	* @description: ����ĳһҳ�Ĺ�����־
	* @param:   
	* @return: String
	* @Time:   2020-4-19 20:10
	* @throws: 
	*/
	public static String getCompetitionId() {
		long min = 1,max = 9;
		for (int i = 1; i < UserLength; i++) {
			min *= 10;
			max *= 10;
		}
		long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
		return nowDate()+String.valueOf(rangeLong);
	}
	/**
	* @author:  ljx
	* @methodsName: getClockId
	* @description: 创建打卡日志唯一id־
	* @param:   
	* @return: String
	* @Time:   2020-5-20 21:50
	* @throws: 
	*/
	public static String getUserId() {
		long min = 1,max = 9;
		for (int i = 1; i < UserLength; i++) {
			min *= 10;
			max *= 10;
		}
		long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
		return nowDate()+String.valueOf(rangeLong);
	}
	/**
	 * @author:  ljx
	 * @methodsName: getcId
	 * @description: 创建打卡日志唯一id־
	 * @param:
	 * @return: String
	 * @Time:   2020-5-20 21:50
	 * @throws:
	 */
	public static String getcId() {
		long min = 1,max = 9;
		for (int i = 1; i < CIdLength; i++) {
			min *= 10;
			max *= 10;
		}
		long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min ;
		return nowDate()+String.valueOf(rangeLong);
	}

}
