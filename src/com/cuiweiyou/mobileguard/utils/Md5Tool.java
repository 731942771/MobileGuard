package com.cuiweiyou.mobileguard.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码加密工具 
 */
public class Md5Tool {

	/** 加密次数 **/
	private static int step = 0;
	/** 加密结果 **/
	private static String md5pswd;
	/** 密码明文 **/
	private static String pswd;

	/** 执行加密，返回密文字符串 **/
	public static String domd5(String pd) {
		step++;

		/** 控制加密次数，小于3，限制在2次 **/	// 一般加密3次，cmd5网站破解即收费。银行密码一般加密15-30次
		if (step < 3) {
			try {
				// 信息摘要算法器
				MessageDigest digest = MessageDigest.getInstance("md5");

				// 密码明文
				pswd = pd;

				// 加密明文的字节码
				byte[] bs = digest.digest(pswd.getBytes());

				// 动态字符串
				StringBuffer buffer = new StringBuffer();

				// 动态字符串
				for (byte b : bs) {
					// 与 运算
					int nub = b & 0xff;	// 加盐：int nub = b & 0xfffffffffffffffffffff;//多加几个f

					// 返回字符串表示的无符号整数所表示以十六进制值
					String string = Integer.toHexString(nub);

					// 如果不足8位，补0
					if (string.length() == 1) {
						buffer.append("0");
					}

					// 保存加密后的十六进制密文
					buffer.append(string);

				}
				// 转文本
				md5pswd = buffer.toString();
				
				// 递归，多次加密
				domd5(md5pswd);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		} else {
			step = 0;	// 静态的，务必复位
			// System.out.println("次数：" + step);
		}
		
		return md5pswd;
	}
}
