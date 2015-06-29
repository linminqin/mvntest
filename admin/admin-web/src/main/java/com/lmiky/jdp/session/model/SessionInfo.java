package com.lmiky.jdp.session.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lmiky.admin.system.menu.model.SubMenu;
import com.lmiky.admin.system.menu.model.TopMenu;
import com.lmiky.admin.user.pojo.User;
import com.lmiky.admin.util.BundleUtils;
import com.lmiky.admin.web.constants.Constants;

/**
 * Session信息
 * @author lmiky
 * @date 2013-4-24
 */
public class SessionInfo implements Serializable {
	private static final long serialVersionUID = -585700322706341108L;
	
	private String sessionId;
	private User user;	//用户
	private Map<String, Boolean> authoritys = new HashMap<String, Boolean>();
	private Map<String, Boolean> menuFavoriteInfo = new HashMap<String, Boolean>();
	private String latelyOperateMenuId; // 最后操作的菜单ID
	private String systemUrlParamHistory = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_WEB_FILE, "system.url.param.history").toLowerCase();
	private String systemUrlParamNoHistory = BundleUtils.getStringValue(Constants.PROPERTIES_KEY_WEB_FILE, "system.url.param.history.noRemember").toLowerCase();
	private int systenUriPatternLength = Constants.SYSTEM_URI_PATTERN.length();
	//访问页面历史记录，提供“退后”按钮使用
	@SuppressWarnings("rawtypes")
	private Map<String, Map> urlParamHistorys = new LinkedHashMap<String, Map>();
	private int urlParamHistoryNum = BundleUtils.getIntValue(Constants.PROPERTIES_KEY_WEB_FILE, "system.url.param.history.num");
	//拥有权限的顶层菜单列表
	private List<TopMenu> topMenus;
	//最近操作菜单
	private List<SubMenu> latelyOpeMenus;

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return user == null ? null : user.getId();
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName() {
		return user == null ? null : user.getLoginName();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return user == null ? null : user.getPassword();
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return user == null ? null : user.getName();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
	
	/**
	 * @return the authoritys
	 */
	public Map<String, Boolean> getAuthoritys() {
		return authoritys;
	}

	/**
	 * 是否拥有权限
	 * @author lmiky
	 * @date 2013-5-24
	 * @param authorityKey
	 * @return 如果未设值，则返回null
	 */
	public Boolean isAuthority(String authorityKey) {
		return authoritys.get(authorityKey);
	}

	/**
	 * 设置是否拥有权限
	 * @author lmiky
	 * @date 2013-5-24
	 * @param authorityKey
	 * @param isAuthority
	 */
	public void setAuthority(String authorityKey, boolean isAuthority) {
		authoritys.put(authorityKey, isAuthority);
	}

	/**
	 * @return the menuFavoriteInfo
	 */
	public Map<String, Boolean> getMenuFavoriteInfo() {
		return menuFavoriteInfo;
	}

	/**
	 * 是否在收藏夹中
	 * @author lmiky
	 * @date 2013-12-8
	 * @param key
	 * @return 如果未设值，则返回null
	 */
	public Boolean isInMenuFavorite(String key) {
		return menuFavoriteInfo.get(key);
	}

	/**
	 * 设置收藏信息
	 * @author lmiky
	 * @date 2013-12-8
	 * @param key
	 * @param value
	 */
	public void setMenuFavoriteInfo(String key, Boolean value) {
		menuFavoriteInfo.put(key, value);
	}

	/**
	 * @return the latelyOperateMenuId
	 */
	public String getLatelyOperateMenuId() {
		return latelyOperateMenuId;
	}

	/**
	 * @param latelyOperateMenuId the latelyOperateMenuId to set
	 */
	public void setLatelyOperateMenuId(String latelyOperateMenuId) {
		this.latelyOperateMenuId = latelyOperateMenuId;
	}

	/**
	 * 添加历史记录参数
	 * @author lmiky
	 * @date 2014-1-20
	 * @param uri
	 * @param params
	 */
	@SuppressWarnings("rawtypes")
	public void AddUrlParamHistory(String uri, Map params) {
		// 截取掉诸如以下信息*.shtml;jsessionid=FDSAGDF43ASFSD654AF
		String requestUri = uri.substring(0, uri.lastIndexOf(Constants.SYSTEM_URI_PATTERN) + systenUriPatternLength).toLowerCase();
		boolean remember = false;
		// 是否需要记录
		for (String pattern : systemUrlParamNoHistory.split(",")) {
			if (requestUri.endsWith(pattern)) {
				return;
			}
		}
		for (String pattern : systemUrlParamHistory.split(",")) {
			if (requestUri.endsWith(pattern)) {
				remember = true;
				break;
			}
		}
		if (!remember) {
			return;
		}
		urlParamHistorys.put(requestUri, params);
		// 如果超出个数，删除最先记录的
		if (urlParamHistorys.size() >= urlParamHistoryNum) {
			Set<String> keys = urlParamHistorys.keySet();
			String key = keys.iterator().next();
			urlParamHistorys.remove(key);
		}
	}

	/**
	 * 获取历史记录参数
	 * @author lmiky
	 * @date 2014-1-20
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getUrlParamHistory(String uri) {
		// 截取掉诸如以下信息*.shtml;jsessionid=FDSAGDF43ASFSD654AF
		String requestUri = uri.substring(0, uri.lastIndexOf(Constants.SYSTEM_URI_PATTERN) + systenUriPatternLength);
		return urlParamHistorys.get(requestUri);
	}

	/**
	 * @return the topMenus
	 */
	public List<TopMenu> getTopMenus() {
		return topMenus;
	}

	/**
	 * @param topMenus the topMenus to set
	 */
	public void setTopMenus(List<TopMenu> topMenus) {
		this.topMenus = topMenus;
	}

	/**
	 * @return the latelyOpeMenus
	 */
	public List<SubMenu> getLatelyOpeMenus() {
		return latelyOpeMenus;
	}

	/**
	 * @param latelyOpeMenus the latelyOpeMenus to set
	 */
	public void setLatelyOpeMenus(List<SubMenu> latelyOpeMenus) {
		this.latelyOpeMenus = latelyOpeMenus;
	}

}
