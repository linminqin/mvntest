package com.lmiky.platform.util;


/**
 * 流水号生成器
 * @author lmiky
 * @date 2014年11月20日 下午3:23:27
 */
public class SerialGenerator {
	private static long baseTime = 138850560000l; // 基准时间(2014/01/01 00:00:00.00)，毫秒取到十位
	private static long currentTime = (long) (System.currentTimeMillis() / 10) - baseTime; // 当前时间
	private static int count = 0; // 计数
	// 加上ip，确保同一局域网内不同的机器（分布式部署）不会出现一样的数据
	private static long ip = ((long) Math.round(Math.pow(10, 15))) * (Integer.parseInt(NetworkUtils.getLocalHostIP().substring(NetworkUtils.getLocalHostIP().lastIndexOf('.') + 1)) % 1000);

	/**
	 * 生成流水号，每10毫秒内至少10000个以内的ID不重复,200年内不重复,长度18~20位
	 * @return
	 */
	public synchronized static String generateSerial() {
		long time = (long) (System.currentTimeMillis() / 10) - baseTime;
		//没10毫秒重置一次
		if (time != currentTime) {
			count = 0;
			currentTime = time;
		}
		int random = (int)(Math.random() * 100);	//后面加上两位随机数，用以迷惑，简单的防止别人根据最后一位（比如01）获取到其他流水号（比如02）
		System.out.println(ip);
		System.out.println(currentTime * 10000 );
		System.out.println(count);
		return (ip + currentTime * 10000 + (++count)) + "" + (random < 10 ? "0" + random : random);
	}

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		System.out.println(SerialGenerator.generateSerial());
	}

}
