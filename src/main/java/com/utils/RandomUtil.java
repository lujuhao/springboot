package com.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

public class RandomUtil {
	
	/**
	 * 根据当前时间获取随机数
	 * @return
	 */
	public static final String getRandomByTime() {
		StringBuffer s = new StringBuffer();
		Random random = new Random();
		LocalDateTime dateTime = LocalDateTime.now();
		//精确到毫秒
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSS");
		s.append(dateTime.format(dateTimeFormatter));
		for (int i = 0; i < 4; i++) {//随机追加4位随机数
			s.append(random.nextInt(10));
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
