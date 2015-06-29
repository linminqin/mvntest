package com.lmiky.platform.util;

import java.util.UUID;

/**
 * UUID生成器
 * @author lmiky
 * @date 2013-4-24
 */
public class UUIDGenerator {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/**
	 * 生成UUID字符串
	 * @author lmiky
	 * @date 2013-4-24
	 * @return
	 */
	public static String generateString() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 短UUID
	 * 本算法利用62个可打印字符，通过随机生成32位UUID，由于UUID都为十六进制，所以将UUID分成8组，每4个为一组，然后通过模62操作，结果作为索引取出字符。
	 * @author lmiky
	 * @date 2015年5月7日 下午3:38:19
	 * @return
	 */
	public static String generateShortString() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = generateString();
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

}
