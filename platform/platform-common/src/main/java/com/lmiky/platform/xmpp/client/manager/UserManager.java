package com.lmiky.platform.xmpp.client.manager;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

import com.lmiky.platform.util.HttpUtils;

/**
 * 账号
 * @author lmiky
 * @date 2015年3月16日 上午9:49:39
 */
public class UserManager extends ConnectionManager {
	
	//用户状态
	public static final int STATUS_NOTFOUND = 0;	//不存在
	public static final int STATUS_ONLINE = 1;	//在线
	public static final int STATUS_OFFLINE = 2;	//不在线
	//用户状态
	public static final String USER_STATUS_URL = "/plugins/presence/status";	//地址
	public static final int USER_STATUS_PORT = 9090;	//端口

	/**
	 * 获得连接并登陆
	 * @author lmiky
	 * @date 2015年3月16日 上午9:52:58
	 * @param userName
	 * @param password
	 * @return
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static XMPPConnection getConnection(String userName, String password) throws SmackException, IOException, XMPPException {
		synchronized(LOCK_OBJ) {
			if(CONN == null) {	//未创建连接
				login(userName, password);
				return CONN;
			}
			String currentLoginedUser = getLoginUser();	//当前连接登陆用户
			if(currentLoginedUser == null) {	//未有用户登陆
				login(userName, password);
			} else if(!currentLoginedUser.equals(userName)) {		//已有用户登陆且不是需要登陆的用户用户
				disconnect();	//断开连接
				login(userName, password);
			}
			return CONN;
		}
	}
	
	/**
	 * 获取当前登陆的用户
	 * @author lmiky
	 * @date 2015年3月16日 上午9:57:14
	 * @return
	 */
	public static String getLoginUser() {
		if(CONN == null) {
			return null;
		}
		if(!CONN.isAuthenticated()) {
			return null;
		}
		String user = CONN.getUser();
		if(!StringUtils.isBlank(user)) {
			return null;
		}
		return CONN.getUser().split("@")[0];
	}
	
	/**
	 * 登陆
	 * @author lmiky
	 * @date 2015年3月16日 上午9:53:09
	 * @param userName
	 * @param password
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static void login(String userName, String password) throws SmackException, IOException, XMPPException {
		getConnection().login(userName, password);
	}
	
	/**
	 * 登陆
	 * @author lmiky
	 * @date 2015年3月16日 上午10:00:26
	 * @param userName
	 * @param password
	 * @param reloginIfOtherAccount	如果有其他用户已登陆，则断开并用当前用户重新登陆
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static void login(String userName, String password, boolean reloginIfOtherAccount) throws SmackException, IOException, XMPPException {
		if(!reloginIfOtherAccount) {
			getConnection().login(userName, password);
		} else {
			String currentLoginedUser = getLoginUser();	//当前连接登陆用户
			if(currentLoginedUser == null) {	//未有用户登陆
				getConnection().login(userName, password);
			} else if(!currentLoginedUser.equals(userName)) {		//已有用户登陆且不是需要登陆的用户用户
				disconnect();	//断开连接
				getConnection().login(userName, password);
			}
		}
	}
	
	/**
	 * 上线
	 * @author lmiky
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 * @date 2015年3月16日 上午9:54:35
	 */
	public static void online() throws SmackException, IOException, XMPPException {
		Presence presence = new Presence(Presence.Type.available);  
		getConnection().sendPacket(presence);
	}
	
	/**
	 * 下线
	 * @author lmiky
	 * @throws XMPPException 
	 * @throws IOException 
	 * @throws SmackException 
	 * @date 2015年3月16日 上午9:54:27
	 */
	public static void offline() throws SmackException, IOException, XMPPException {
		Presence presence = new Presence(Presence.Type.unavailable);  
		getConnection().sendPacket(presence);
	}
	
	/**
	 * 注册
	 * @author lmiky
	 * @date 2015年3月16日 下午12:02:19
	 * @param userName
	 * @param password
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static void register(String userName, String password) throws SmackException, IOException, XMPPException {
		register(userName, password, null);
	}
	
	/**
	 * 注册
	 * @author lmiky
	 * @date 2015年3月16日 下午12:02:28
	 * @param userName
	 * @param password
	 * @param attributes 其他属性
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static void register(String userName, String password, Map<String, String> attributes) throws SmackException, IOException, XMPPException {
		AccountManager accountManager = AccountManager.getInstance(getConnection());
		if(attributes != null & !attributes.isEmpty()) {
			accountManager.createAccount(userName, password, attributes);
		} else {
			accountManager.createAccount(userName, password);
		}
	}
	
	/**
	 * 获取用户状态
	 * @author lmiky
	 * @date 2015年3月16日 下午2:17:47
	 * @param user 被查看的用户名
	 * @return
	 * @return
	 * @throws Exception 
	 */
	public static int getStatus(String user) throws Exception {
		return getStatus(user, null);
	}
	
	/**
	 * 获取用户状态
	 * 需要在openfire安装Presence Service 插件
	 * 服务器设置 -> Presence Service 中如果设置Presence visibility为Anyone，则允许所有查看状态，如果设置为Subscribed ，则只允许好友查看状态
	 * @author lmiky
	 * @date 2015年3月16日 下午2:17:47
	 * @param user 被查看的用户名
	 * @param opeUser	当前操作用户
	 * @return 0：用户不存在或没有权限查看，1=在线，2=离线
	 * @return
	 * @throws Exception 
	 */
	public static int getStatus(String user, String opeUser) throws Exception {
		String url = String.format("http://%s:%d%s?jid=%s@%s&type=xml%s", HOST, USER_STATUS_PORT, USER_STATUS_URL, user, SERVERNAME, (StringUtils.isBlank(opeUser) ? "" : ("&req_jid=" + opeUser + "@" + SERVERNAME)));
		String result = HttpUtils.get(url);
		if (result.indexOf("type=\"unavailable\"") >= 0) {	//不在线
			return STATUS_OFFLINE;
		} else if (result.indexOf("priority") >= 0 || result.indexOf("id=\"") >= 0) {		//在线
			return STATUS_ONLINE;
		} 
//		if (result.indexOf("type=\"error\"") >= 0) {
//			return STATUS_NOTFOUND;
//		}
		return STATUS_NOTFOUND;
	}
}
