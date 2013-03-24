package com.github.julman99.jesync.net;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import com.github.julman99.jesync.core.LockEngine;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;


/**
 * Server creator
 * @author Julio Viera
 */
public class Server {

    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() {
        // Configure the server.
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));

        // Set up the pipeline factory.
        bootstrap.setPipelineFactory(new ServerPipelineFactory(new LockEngine()));

        // Bind and start to accept incoming connections.
        bootstrap.bind(new InetSocketAddress(port));
    }

}