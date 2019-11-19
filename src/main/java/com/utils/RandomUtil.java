package com.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
	
	/**
	 * 根据当前时间获取随机数
	 * @return
	 */
	public static final String getRandomByTime() {
		String[] array = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		StringBuffer s = new StringBuffer();
		Random random = new Random();
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String nowdate = dateFormat.format(date);
		s.append(nowdate.substring(0, 4));//截取年份
		s.append(nowdate.substring(5, 7));//截取月份
		s.append(nowdate.substring(8,10));//截取日
		for (int i = 0; i < 4; i++) {//随机追加4位随机数
			s.append(array[random.nextInt(10)]);
		}
		return s.toString();
	}
	
	/**
	 * 生成一个随机UUID
	 * @return
	 */
	public static final String getUUID() {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-","");
	}
}
