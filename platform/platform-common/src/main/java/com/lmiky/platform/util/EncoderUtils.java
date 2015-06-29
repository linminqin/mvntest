package com.lmiky.platform.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.lmiky.platform.logger.util.LoggerUtils;

/**
 * 编码
 * @author lmiky
 * @date 2014-1-23
 */
public class EncoderUtils {
	public static final String DEFAULT_CODING = "UTF-8"; // 默认编码

	/**
	 * MD5加密
	 * @author lmiky
	 * @date 2014-1-23
	 * @param encodeStr
	 * @return
	 */
	public static String md5(String encodeStr) {
		return DigestUtils.md5Hex(encodeStr).toUpperCase();
	}

	/**
	 * 加密
	 * @author lmiky
	 * @date 2015年3月2日 上午11:08:32
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String aes128Encrypt(String content, String password) throws Exception {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");

			kgen.init(128, new SecureRandom(password.getBytes()));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器

			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

			byte[] result = cipher.doFinal(content.getBytes(DEFAULT_CODING));
			return parseByte2HexStr(result); // 加密
		} catch (Exception e) {
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 解密
	 * @author lmiky
	 * @date 2015年3月2日 上午11:08:16
	 * @param content
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public static String aes128Decrypt(String content, String password) throws Exception {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(password.getBytes(DEFAULT_CODING)));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器

			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化

			byte[] result = cipher.doFinal(toByte(content));
			return new String(result); // 加密
		} catch (Exception e) {
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 加密，与nodejs对应
	 * @author lmiky
	 * @date 2014-2-25
	 * @param content
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String aesEncryptForNodejs(String content, String key) throws Exception {
		try {
			byte[] input = content.getBytes(DEFAULT_CODING);

			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(key.getBytes(DEFAULT_CODING));
			SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skc);

			byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
			int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
			ctLength += cipher.doFinal(cipherText, ctLength);

			return parseByte2HexStr(cipherText);
		} catch (Exception e) {
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 解密，与nodejs对应
	 * @author lmiky
	 * @date 2014-2-25
	 * @param encrypted
	 * @param seed
	 * @return
	 * @throws Exception
	 */
	public static String aesDecryptForNodejs(String encrypted, String seed) throws Exception {
		try {
			byte[] keyb = seed.getBytes(DEFAULT_CODING);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(keyb);
			SecretKeySpec skey = new SecretKeySpec(thedigest, "AES");
			Cipher dcipher = Cipher.getInstance("AES");
			dcipher.init(Cipher.DECRYPT_MODE, skey);

			byte[] clearbyte = dcipher.doFinal(toByte(encrypted));
			return new String(clearbyte);
		} catch (Exception e) {
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 加密，与C#对应
	 * @author lmiky
	 * @date 2015年3月2日 上午11:04:03
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String aesEncryptForCsharp(String sSrc, String sKey) throws Exception {
		try {
			sKey = md5(sKey).toLowerCase().substring(16);	//key为16个长度
			SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(DEFAULT_CODING), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
			byte[] encrypted = cipher.doFinal(sSrc.getBytes(DEFAULT_CODING));

			return parseByte2HexStr(encrypted);
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 解密，与C#对应
	 * @author lmiky
	 * @date 2015年3月2日 上午11:05:09
	 * @param sSrc
	 * @param sKey
	 * @return
	 * @throws Exception
	 */
	public static String aesDecryptForCsharp(String sSrc, String sKey) throws Exception {
		try {
			sKey = md5(sKey).toLowerCase().substring(16);	//key为16个长度
			SecretKeySpec skeySpec = new SecretKeySpec(sKey.getBytes(DEFAULT_CODING), "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skeySpec);
			byte[] encrypted1 = toByte(sSrc);
			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original, DEFAULT_CODING);
			return originalString;
		} catch (Exception e) {
			LoggerUtils.logException(e);
			throw e;
		}
	}

	/**
	 * 字符串转字节数组
	 * @author lmiky
	 * @date 2014-2-25
	 * @param hexString
	 * @return
	 */
	private static byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++) {
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
		}
		return result;
	}

	/**
	 * 字节转16进制数组
	 * @author lmiky
	 * @date 2014-2-25
	 * @param buf
	 * @return
	 */
	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		String password = "abc";
//		String content = "AES加密算法测试数据12345679098gfhjghdfghfgh";
		System.out.println(System.currentTimeMillis());
//		String result = aesEncryptForCsharp(content, password);
//		System.out.println(result);
//		System.out.println(System.currentTimeMillis());
		aesDecryptForCsharp("f1094acd410007a62bc47baac205e214bcd4d0574af4e64c2696c16cae9f1136ae3ccdd2546e392023d8b52eba1bffd997c209a8992947f476c6683e9b7cf7ea", password);
		System.out.println(System.currentTimeMillis());
	}
}
