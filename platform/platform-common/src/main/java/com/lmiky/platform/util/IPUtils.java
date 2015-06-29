package com.lmiky.platform.util;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.lmiky.platform.cache.CacheFactory;
import com.lmiky.platform.cache.model.ObjectCache;
import com.lmiky.platform.cache.model.SimpleCacheData;
import com.lmiky.platform.json.util.JsonUtils;
import com.lmiky.platform.logger.util.LoggerUtils;

/**
 * IP地址工具类
 * @author lmiky
 * @date 2015年5月11日 下午2:18:09
 */
public class IPUtils {
	private static final String API_URL_TB = "http://ip.taobao.com/service/getIpInfo.php";
	private static final String API_URL_SINA = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
	private static final String RESULT_CHARSET = "UTF-8";
	// 参数名
	public static final String PARAMNAME_COUNTRY = "country";
	public static final String PARAMNAME_PROVINCE = "province";
	public static final String PARAMNAME_CITY = "city";

	// 缓存
	private static final CacheFactory cacheFactory = (CacheFactory) Environment.getBean("cacheFactory");
	private static final String CACHE_HEAD_IPLOCATION = "ipLocation.";
	private static final ObjectCache ipLocationCache = cacheFactory.getCache("ipLocationCache");

	/**
	 * 获取真实IP
	 * @author lmiky
	 * @date 2015年5月13日 下午4:15:49
	 * @param request
	 * @return
	 */
	public static String getRealIP(HttpServletRequest request) {
		String ip = request.getRemoteAddr();
		if("127.0.0.1".equalsIgnoreCase(ip) || "localhost".equalsIgnoreCase(ip) || ip.startsWith("192.168.")) {
			ip = request.getHeader("X-Real-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 获取定位
	 * @author lmiky
	 * @date 2015年5月12日 下午5:05:47
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> location(String ip) throws Exception {
		Map<String, String> locationMap = null;
		// 读取缓存
		SimpleCacheData<Map<String, String>> cache = (SimpleCacheData<Map<String, String>>) ipLocationCache.get(CACHE_HEAD_IPLOCATION + ip);
		if (cache != null) {
			return cache.getValue();
		}
		locationMap = sinaLocation(ip);
		// 设置缓存
		try {
			ipLocationCache.put(CACHE_HEAD_IPLOCATION + ip, new SimpleCacheData<Map<String, String>>(locationMap));
		} catch (Exception e) {
			LoggerUtils.logException(e);
		}
		return locationMap;
	}

	/**
	 * 淘宝
	 * @author lmiky
	 * @date 2015年5月11日 下午2:26:21
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> tbLocation(String ip) throws Exception {
		Map<String, String> ret = new HashMap<String, String>();
		// 请求
		String json = HttpUtils.get(API_URL_TB + "?ip=" + ip);
		// 解析数据，数据格式为
		// {"code":0,"data":{"country":"\u4e2d\u56fd","country_id":"CN","area":"\u534e\u4e1c","area_id":"300000","region":"\u798f\u5efa\u7701","region_id":"350000","city":"\u53a6\u95e8\u5e02","city_id":"350200","county":"","county_id":"-1","isp":"\u7535\u4fe1","isp_id":"100017","ip":"218.5.76.173"}}
		Map<String, Object> map = JsonUtils.fromJson(json, Map.class);
		Map<String, Object> dataMap = (Map<String, Object>) map.get("data");
		// 国家
		String country = dataMap.get("country").toString();
		country = URLDecoder.decode(country, RESULT_CHARSET);
		ret.put(PARAMNAME_COUNTRY, country);
		// 省份
		String province = dataMap.get("region").toString();
		province = URLDecoder.decode(province, RESULT_CHARSET);
		ret.put(PARAMNAME_PROVINCE, buildProvinceName(province));
		// 地市
		String city = dataMap.get("city").toString();
		city = URLDecoder.decode(city, RESULT_CHARSET);
		ret.put(PARAMNAME_CITY, buildCityName(city));

		return ret;
	}

	/**
	 * 新浪
	 * @author lmiky
	 * @date 2015年5月11日 下午2:29:17
	 * @param ip
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> sinaLocation(String ip) throws Exception {
		// ip = "113.102.134.2";
		Map<String, String> ret = new HashMap<String, String>();
		// 请求
		String json = HttpUtils.get(API_URL_SINA + "&ip=" + ip);
		Map<String, Object> map = JsonUtils.fromJson(json, Map.class);
		map = JsonUtils.fromJson(json, Map.class);
		// 国家
		String country = map.get("country").toString();
		country = URLDecoder.decode(country, RESULT_CHARSET);
		ret.put(PARAMNAME_COUNTRY, country);
		// 省份
		String province = map.get("province").toString();
		province = URLDecoder.decode(province, RESULT_CHARSET);
		ret.put(PARAMNAME_PROVINCE, buildProvinceName(province));
		// 地市
		String city = map.get("city").toString();
		city = URLDecoder.decode(city, RESULT_CHARSET);
		ret.put(PARAMNAME_CITY, buildCityName(city));

		return ret;
	}

	/**
	 * 构建省名
	 * @author lmiky
	 * @date 2015年5月11日 下午2:44:34
	 * @param provinceName
	 * @return
	 */
	private static String buildProvinceName(String provinceName) {
		if (!provinceName.endsWith("省") && !provinceName.endsWith("市") && !provinceName.endsWith("区")) {
			provinceName += "省";
		}
		return provinceName;
	}

	/**
	 * 构建市名
	 * @author lmiky
	 * @date 2015年5月11日 下午2:45:02
	 * @param cityName
	 * @return
	 */
	private static String buildCityName(String cityName) {
		if (!cityName.endsWith("市") && !cityName.endsWith("州") && !cityName.endsWith("区") && cityName.length() < 4) {
			cityName += "市";
		}
		return cityName;
	}

	public static void main(String[] args) throws Exception {
		String[] ips = { "218.5.76.173", "58.22.96.121", "218.86.126.226", "61.131.4.164", "121.34.145.197" };
		for (String ip : ips) {
			Map<String, String> map = IPUtils.location(ip);
			System.out.print(map.get(PARAMNAME_COUNTRY) + " ");
			System.out.print(map.get(PARAMNAME_PROVINCE) + " ");
			System.out.println(map.get(PARAMNAME_CITY));
		}
	}

}
