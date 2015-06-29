package com.lmiky.platform.xmpp.client.manager;

import java.io.IOException;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import com.lmiky.platform.util.BundleUtils;
import com.lmiky.platform.xmpp.client.listener.ConnectionListener;
import com.lmiky.platform.xmpp.smack.tcp.XMPPTCPKeepAliveConnection;

/**
 * 连接
 * @author lmiky
 * @date 2015年3月13日 下午3:10:41
 */
public class ConnectionManager {
	public static final String SERVERNAME = BundleUtils.getStringContextValue("xmpp.servername");	//服务名
	public static final String HOST = BundleUtils.getStringContextValue("xmpp.host");							//域名
	public static final int PORT = BundleUtils.getIntContextValue("xmpp.port");										//端口
	
	protected static XMPPConnection CONN = null;
	
	protected static final Object LOCK_OBJ = new Object();	//加锁
	
	/**
	 * 初始化
	 * @author lmiky
	 * @date 2015年3月13日 下午3:21:33
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	private static void initConnection() throws SmackException, IOException, XMPPException {
		ConnectionConfiguration config = new ConnectionConfiguration(HOST, PORT, SERVERNAME);
        config.setCompressionEnabled(true);	//是否启用压缩
		//config.setReconnectionAllowed(false);  	// 允许自动连接  
		config.setSendPresence(false);//是否告诉服务器自己的状态:：如果需要处理离线消息，则不能告诉
		config.setSecurityMode(SecurityMode.disabled);	//否则会报DNS错误
        config.setDebuggerEnabled(false);	//是否启用调试
//        config.setReconnectionAllowed(true);
        Roster.setDefaultSubscriptionMode(Roster.SubscriptionMode.manual); //手工处理所有好友申请请求
		CONN = new XMPPTCPKeepAliveConnection(config);
		CONN.addConnectionListener(new ConnectionListener());
		CONN.connect();
	}

	/**
	 * 获取XMPP连接
	 * @author lmiky
	 * @date 2015年3月13日 下午3:54:08
	 * @return
	 * @throws SmackException
	 * @throws IOException
	 * @throws XMPPException
	 */
	public static XMPPConnection getConnection() throws SmackException, IOException, XMPPException {
		synchronized(LOCK_OBJ) {
			if(CONN == null) {
				initConnection();
			}
			if(!CONN.isConnected()) {
				CONN.connect();
			}
			return CONN;
		}
	}

	/**
	 * 断开连接
	 * @author lmiky
	 * @date 2015年3月13日 下午3:53:25
	 * @throws NotConnectedException
	 */
	public static void disconnect() throws NotConnectedException {
		synchronized(LOCK_OBJ) {
			if(CONN != null) {
				if(CONN.isConnected()) {
					CONN.disconnect();
				}
				CONN = null;
			}
		}
	}
}
