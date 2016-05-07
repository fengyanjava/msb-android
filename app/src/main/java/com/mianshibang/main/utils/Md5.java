package com.mianshibang.main.utils;

import java.security.MessageDigest;

public class Md5 {

	public static String md5(String input) {
		try {
			MessageDigest md;
			md = MessageDigest.getInstance("MD5"); // 同样可以使用SHA1
			// 计算md5函数
			md.update(input.getBytes("UTF-8"));
			byte[] bytes = md.digest();
			
			StringBuilder sb = new StringBuilder();
			for (byte b : bytes) {
				if ((b & 0xFF) < 0x10) {
					sb.append("0");
				}
				sb.append(Integer.toHexString(b & 0xff));
			}
			return sb.toString();
		} catch (Exception e) {
			return input;
		}
	}
}
