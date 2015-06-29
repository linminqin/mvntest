/**
 *
 * Copyright 2003-2007 Jive Software.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lmiky.platform.xmpp.smack.tcp;

import java.io.IOException;
import java.io.Writer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.ArrayBlockingQueueWithShutdown;

import com.lmiky.platform.constants.Constants;
import com.lmiky.platform.logger.util.LoggerUtils;
import com.lmiky.platform.util.BundleUtils;

/**
 * smack 4.0.7 去掉了keepAlive，所以会出现无心跳而断开的问题，本包在4.0.7的基础上加入了 smack 3.2.1的keepAlive代码
 * 添加keepAlive守护线程
 * 
 * Writes packets to a XMPP server. Packets are sent using a dedicated thread. Packet
 * interceptors can be registered to dynamically modify packets before they're actually
 * sent. Packet listeners can be registered to listen for all outgoing packets.
 *
 * @see XMPPConnection#addPacketInterceptor
 * @see XMPPConnection#addPacketSendingListener
 *
 * @author Matt Tucker
 */
class PacketWriter {
    public static final int QUEUE_SIZE = 500;

    private static final Logger LOGGER = Logger.getLogger(PacketWriter.class.getName());
    
    private static final int keepAliveInterval = BundleUtils.getIntValue(Constants.PROPERTIES_KEY_PLATFORM_FILE, "xmpp.keepAlive.interval");

    private final XMPPTCPKeepAliveConnection connection;
    private final ArrayBlockingQueueWithShutdown<Packet> queue = new ArrayBlockingQueueWithShutdown<Packet>(QUEUE_SIZE, true);

    private Thread writerThread;
    private Thread keepAliveThread;
    private Writer writer;

    volatile boolean done;

    AtomicBoolean shutdownDone = new AtomicBoolean(false);
    
    /**
     * Timestamp when the last stanza was sent to the server. This information is used
     * by the keep alive process to only send heartbeats when the connection has been idle.
     */
    private long lastActive = System.currentTimeMillis();

    /**
     * Creates a new packet writer with the specified connection.
     *
     * @param connection the connection.
     */
    protected PacketWriter(XMPPTCPKeepAliveConnection connection) {
        this.connection = connection;
        init();
    }

    /** 
    * Initializes the writer in order to be used. It is called at the first connection and also 
    * is invoked if the connection is disconnected by an error.
    */ 
    protected void init() {
        writer = connection.getWriter();
        done = false;
        shutdownDone.set(false);

        queue.start();
        writerThread = new Thread() {
            public void run() {
                writePackets(this);
            }
        };
        writerThread.setName("Smack Packet Writer (" + connection.getConnectionCounter() + ")");
        writerThread.setDaemon(true);
    }

    /**
     * Sends the specified packet to the server.
     *
     * @param packet the packet to send.
     * @throws NotConnectedException 
     */
    public void sendPacket(Packet packet) throws NotConnectedException {
        if (done) {
            throw new NotConnectedException();
        }

        try {
            queue.put(packet);
        }
        catch (InterruptedException ie) {
            throw new NotConnectedException();
        }
    }

    /**
     * Starts the packet writer thread and opens a connection to the server. The
     * packet writer will continue writing packets until {@link #shutdown} or an
     * error occurs.
     */
    public void startup() {
        writerThread.start();
    }
    
    /**
     * Starts the keep alive process. A white space (aka heartbeat) is going to be
     * sent to the server every 30 seconds (by default) since the last stanza was sent
     * to the server.
     */
    void startKeepAliveProcess() {
        // Schedule a keep-alive task to run if the feature is enabled. will write
        // out a space character each time it runs to keep the TCP/IP connection open.
        if (keepAliveInterval > 0) {
            KeepAliveTask task = new KeepAliveTask(keepAliveInterval);
            keepAliveThread = new Thread(task);
            task.setThread(keepAliveThread);
            keepAliveThread.setDaemon(true);
            keepAliveThread.setName("Smack Keep Alive (" + connection.getConnectionCounter() + ")");
            keepAliveThread.start();
        }
    }

    void setWriter(Writer writer) {
        this.writer = writer;
    }

    /**
     * Shuts down the packet writer. Once this method has been called, no further
     * packets will be written to the server.
     */
    public void shutdown() {
        done = true;
        queue.shutdown();
        synchronized(shutdownDone) {
            if (!shutdownDone.get()) {
                try {
                    shutdownDone.wait(connection.getPacketReplyTimeout());
                }
                catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, "shutdown", e);
                }
            }
        }
    }

    /**
     * Returns the next available packet from the queue for writing.
     *
     * @return the next packet for writing.
     */
    private Packet nextPacket() {
        if (done) {
            return null;
        }

        Packet packet = null;
        try {
            packet = queue.take();
        }
        catch (InterruptedException e) {
            // Do nothing
        }
        return packet;
    }

    private void writePackets(Thread thisThread) {
        try {
            // Open the stream.
            openStream();
            // Write out packets from the queue.
            while (!done && (writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    writer.write(packet.toXML().toString());

                    if (queue.isEmpty()) {
                        writer.flush();
                        // Keep track of the last time a stanza was sent to the server
                        lastActive = System.currentTimeMillis();
                    }
                }
            }
            // Flush out the rest of the queue. If the queue is extremely large, it's possible
            // we won't have time to entirely flush it before the socket is forced closed
            // by the shutdown process.
            try {
                while (!queue.isEmpty()) {
                    Packet packet = queue.remove();
                    writer.write(packet.toXML().toString());
                }
                writer.flush();
            }
            catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception flushing queue during shutdown, ignore and continue", e);
            }

            // Delete the queue contents (hopefully nothing is left).
            queue.clear();

            // Close the stream.
            try {
                writer.write("</stream:stream>");
                writer.flush();
            }
            catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception writing closing stream element", e);

            }
            finally {
                try {
                    writer.close();
                }
                catch (Exception e) {
                    // Do nothing
                }
            }

            shutdownDone.set(true);
            synchronized(shutdownDone) {
                shutdownDone.notify();
            }
        }
        catch (IOException ioe) {
            // The exception can be ignored if the the connection is 'done'
            // or if the it was caused because the socket got closed
            if (!(done || connection.isSocketClosed())) {
                shutdown();
                connection.notifyConnectionError(ioe);
            }
        }
    }

    /**
     * Sends to the server a new stream element. This operation may be requested several times
     * so we need to encapsulate the logic in one place. This message will be sent while doing
     * TLS, SASL and resource binding.
     *
     * @throws IOException If an error occurs while sending the stanza to the server.
     */
    void openStream() throws IOException {
        StringBuilder stream = new StringBuilder();
        stream.append("<stream:stream");
        stream.append(" to=\"").append(connection.getServiceName()).append("\"");
        stream.append(" xmlns=\"jabber:client\"");
        stream.append(" xmlns:stream=\"http://etherx.jabber.org/streams\"");
        stream.append(" version=\"1.0\">");
        writer.write(stream.toString());
        writer.flush();
    }

    /**
     * A TimerTask that keeps connections to the server alive by sending a space
     * character on an interval.
     */
    private class KeepAliveTask implements Runnable {

        private int delay;
        private Thread thread;

        public KeepAliveTask(int delay) {
            this.delay = delay;
        }

        protected void setThread(Thread thread) {
            this.thread = thread;
        }
        
        public void run() {
            try {
                // Sleep 15 seconds before sending first heartbeat. This will give time to
                // properly finish TLS negotiation and then start sending heartbeats.
                Thread.sleep(15000);
            }
            catch (InterruptedException ie) {
                // Do nothing
            }
            while (!done && keepAliveThread == thread) {
                synchronized (writer) {
                    // Send heartbeat if no packet has been sent to the server for a given time
                    if (System.currentTimeMillis() - lastActive >= delay) {
                        try {
                            writer.write(" ");
                            writer.flush();
                        }
                        catch (Exception e) {
                           LoggerUtils.logException(e);
                        }
                    }
                }
                try {
                    // Sleep until we should write the next keep-alive.
                    Thread.sleep(delay);
                }
                catch (InterruptedException ie) {
                    // Do nothing
                }
            }
        }
    }
}
