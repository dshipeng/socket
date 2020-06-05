package com.pers.daisp.class2;

import com.pers.daisp.util.LogUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(3000);
        socket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 3000), 3000);

        LogUtil.log("连接上服务端成功，服务端地址：" + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
        LogUtil.log("本地地址：" + socket.getLocalAddress().getHostAddress() + ":" + socket.getLocalPort());
        try {
            message(socket);
        } catch (Exception e) {
            LogUtil.log(e.getMessage());
        }

        socket.close();
        LogUtil.log("客户端已退出");
    }

    private static void message(Socket socket) throws IOException {
        try (
                //获取键盘输入流
                InputStream inputStream = System.in;
                BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));

                //socket输出流
                OutputStream outputStream = socket.getOutputStream();
                PrintStream socketWriter = new PrintStream(outputStream);

                //socket输入流
                InputStream socketInputStream = socket.getInputStream();
                BufferedReader socketReader = new BufferedReader(new InputStreamReader(socketInputStream))
                ) {
            while (true) {
                String message = input.readLine();
                socketWriter.println(message);
                LogUtil.log("发送消息：" + message);

                String ackMessage = socketReader.readLine();
                LogUtil.log("收到消息：" + ackMessage);
                if ("bye".equals(ackMessage))  {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
