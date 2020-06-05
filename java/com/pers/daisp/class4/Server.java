package com.pers.daisp.class4;

import java.io.IOException;
import java.net.*;

public class Server {
    private static final int PORT = 20000;

    public static void main(String[] args) {
    }

    private static void initServerSocket(ServerSocket socket) throws SocketException {
        //设置accept超时时间
        //socket.setSoTimeout(2000);
        //是否复用未完成关闭的Socket地址
        socket.setReuseAddress(true);
        //设置accept的socket接受发送缓冲器大小
        socket.setReceiveBufferSize(64 * 1024 * 1024);
        //设置性能参数：短连接、延迟、带宽的相对重要性
        socket.setPerformancePreferences(1, 1, 1);
    }

    private static ServerSocket createSocket() throws IOException {

        ServerSocket socket = new ServerSocket();

        initServerSocket(socket);
        //第二个参数为允许等待的连接队列
        socket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), PORT), 50);

        return socket;
    }
}
