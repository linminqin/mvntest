package com.lmiky.platform.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import com.lmiky.platform.logger.util.LoggerUtils;

/**
 * 网络工具
 * @author lmiky
 * @date 2014年11月20日 下午3:23:35
 */
public class NetworkUtils {

	/**
	 * 获取本机IP,在linux下也有效
	 * @return
	 */
	public static String getLocalHostIP() {
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			LoggerUtils.logException(e);
			throw new Error(e);
		}
		while (netInterfaces.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress ip = (InetAddress) addresses.nextElement();
				if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {
					return ip.getHostAddress();
				}
			}
		}
		throw new Error("");
	}
}