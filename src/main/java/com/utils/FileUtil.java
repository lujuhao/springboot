package com.utils;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * @author ljh
 */
public class FileUtil {
	
	/**
	 * 上传用户头像文件至服务器
	 * @param file 要上传的文件
	 * @return
	 */
	public static String uploadUserHeadImg(MultipartFile file){
		String headImgPath="";//数据库保存的头像路径
		
		if (null != file) {
			String sourceFileName = file.getOriginalFilename();//获取源文件名称
			String imgType = sourceFileName.substring(sourceFileName.indexOf("."));//获取上传的文件类型(.jpg/.png..)
			String randomName = RandomUtil.getRandomByTime()+imgType;//根据当前时间随机生成一个文件名称
			String uploadPath = SysConfigContants.USER_HEADIMG_PATH;//获取上传路径
			File uploadFile = new File(uploadPath,randomName);
			if (!uploadFile.getParentFile().exists()) {
				uploadFile.mkdirs();
			}
			try {
				file.transferTo(uploadFile);
				headImgPath=SysConfigContants.USER_HEADIMG_PATH + randomName;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return headImgPath;
	}
}
