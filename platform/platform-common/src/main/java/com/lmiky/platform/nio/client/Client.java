package com.lmiky.platform.nio.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;

import com.lmiky.platform.logger.util.LoggerUtils;


/**
 * 客户端
 * @author lmiky
 * @date 2014年11月20日 上午10:14:38
 */
public class Client {
	private String host;
	private int port;
	private EventLoopGroup group;
	private Channel ch;
	
	public Client(final String host, final int port, ChannelInitializer<? extends Channel> channelInitializer) throws IOException, InterruptedException {
		this.host = host;
		this.port = port;
		group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group).channel(NioSocketChannel.class).handler(channelInitializer);

		// 连接服务端
		ChannelFuture f = b.connect(host, port);
		f.addListener(new ChannelFutureListener() {
			/* (non-Javadoc)
			 * @see io.netty.util.concurrent.GenericFutureListener#operationComplete(io.netty.util.concurrent.Future)
			 */
			@Override
			public void operationComplete(ChannelFuture channelFuture) throws Exception {
				if (!channelFuture.isSuccess()) {
					LoggerUtils.info(String.format("创建NIO连接失败，host: %s，port: %d", host, port));
					throw new Exception("connection fail");
				} else {
					LoggerUtils.info(String.format("创建NIO连接成功，host: %s，port: %d", host, port));
				}
			}
		});
		ch = f.sync().channel();
	}

	/**
	 * 发送信息
	 * @author lmiky
	 * @date 2014年11月20日 上午10:32:47
	 * @param message
	 */
	public ChannelFuture writeAndFlush(Object message) {
		ChannelFuture f = ch.writeAndFlush(message);
		return f;
	}
	
	/**
	 * 销毁
	 * @author lmiky
	 * @date 2014年11月20日 上午10:14:59
	 */
	public void destroy() {
		group.shutdownGracefully();
	}
	
	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
