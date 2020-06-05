package com.pers.daisp.class4;

import com.pers.daisp.util.LogUtil;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class Client {
    private static final int PORT = 20000;
    private static final int LOCAL_PORT = 20001;

    public static void main(String[] args) throws Exception {
        Socket socket = createSocket();

        initSocket(socket);

        socket.connect(new InetSocketAddress(Inet4Address.getLocalHost(), PORT));

        LogUtil.log("服务端启动完成.");

        //同Class2代码

    }

    private static void initSocket(Socket socket) throws SocketException {
        //设置读取超时时间为2秒
        socket.setSoTimeout(2000);
        //是否复用未完成关闭的Socket地址
        socket.setReuseAddress(true);
        //是否开启Nagle算法
        socket.setTcpNoDelay(false);
        //是否需要在长时间无数据响应时发送确认数据(类似心跳包)，时间大约为2小时
        socket.setKeepAlive(true);
        //对于close操作行为的处理，默认关闭时立即返回，底层操作系统接管输出流，将缓冲区数据发送完成
        //true 关闭最长阻塞第二个参数毫秒，随后缓冲区数据抛弃，直接发送RST结束命令到对方，无需等待2MSL
        socket.setSoLinger(true, 20);
        //设置紧急数据是否内敛,socket.sendUrgentData(1);
        socket.setOOBInline(true);
        //设置接受发送缓冲器大小
        socket.setSendBufferSize(64 * 1024 * 1024);
        socket.setReceiveBufferSize(64 * 1024 * 1024);
        //设置性能参数：短连接、延迟、带宽的相对重要性
        socket.setPerformancePreferences(1, 1, 1);
    }

    private static Socket createSocket() throws IOException {
        /*
        //无代理
        Socket socket = new Socket(Proxy.NO_PROXY);
        //有代理
        Proxy proxy = new Proxy(Proxy.Type.HTTP,
                 new InetSocketAddress(Inet4Address.getByName("www.baidu.com"), 8080));
        Socket socket = new Socket(proxy);
        //新建一个套接字，直接连接到本地20000的服务器上
        Socket socket = new Socket("localhost", PORT);
        Socket socket = new Socket(Inet4Address.getLocalHost(), PORT);
        //新建一个套接字，直接连接到本地20000的服务器上,并且绑定20001端口
        Socket socket = new Socket(Inet4Address.getLocalHost(), PORT,
                                   Inet4Address.getLocalHost(), LOCAL_PORT);
        备注：不用再执行connect()
         */
        Socket socket = new Socket();
        socket.bind(new InetSocketAddress(Inet4Address.getLocalHost(), LOCAL_PORT));

        return socket;
    }
}
