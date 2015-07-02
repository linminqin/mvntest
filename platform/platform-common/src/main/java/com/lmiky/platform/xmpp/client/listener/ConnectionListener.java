package com.lmiky.platform.xmpp.client.listener;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;

import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.xmpp.client.manager.ConnectionManager;

/**
 * 连接侦听器
 * @author lmiky
 * @date 2015年3月16日 下午5:51:42
 */
public class ConnectionListener implements org.jivesoftware.smack.ConnectionListener {
	
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connected(org.jivesoftware.smack.XMPPConnection)
	 */
	public void connected(XMPPConnection connection) {
		LoggerUtils.debug("xmpp 成功连接 (" + connection.getConnectionCounter() + ")");
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#authenticated(org.jivesoftware.smack.XMPPConnection)
	 */
	public void authenticated(XMPPConnection connection) {
		LoggerUtils.debug("xmpp 成功登陆 (" + connection.getConnectionCounter() + ": " + connection.getUser() + ")");
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
	 */
	public void connectionClosed() {
//		try {
//			ConnectionManager.disconnect();
//		} catch (NotConnectedException e) {
//			LoggerUtils.logException(e);
//		}
		
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
	 */
	public void connectionClosedOnError(Exception e) {
		LoggerUtils.error("xmpp connectionClosedOnError");
		LoggerUtils.logException(e);
		try {
			ConnectionManager.disconnect();
		} catch (NotConnectedException nce) {
			LoggerUtils.logException(nce);
		}
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectingIn(int)
	 */
	public void reconnectingIn(int seconds) {
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectionSuccessful()
	 */
	public void reconnectionSuccessful() {
		
	}

	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.ConnectionListener#reconnectionFailed(java.lang.Exception)
	 */
	public void reconnectionFailed(Exception e) {
		LoggerUtils.error("xmpp reconnectionFailed");
		LoggerUtils.logException(e);
		try {
			ConnectionManager.disconnect();
		} catch (NotConnectedException nce) {
			LoggerUtils.logException(nce);
		}
	}
}
