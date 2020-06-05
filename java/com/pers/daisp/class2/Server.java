package com.pers.daisp.class2;

import com.pers.daisp.util.LogUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] arg) throws Exception {
        ServerSocket serverSocket = new ServerSocket(3000);
        LogUtil.log("服务端已启动，端口3000，等待客户端连接....");
        while (true) {
            Socket clientSocket = serverSocket.accept();
            LogUtil.log("客户端连接成功：" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
            new ClientHandler(clientSocket).start();
        }
    }
}


class ClientHandler extends Thread {
    private Socket client;

    public ClientHandler(Socket socket) {
        super();
        this.client = socket;
    }

    @Override
    public void run() {
        try (
            //socket输出流
            OutputStream outputStream = client.getOutputStream();
            PrintStream printWriter = new PrintStream(outputStream);

            //socket输入流
            InputStream socketInputStream = client.getInputStream();
            BufferedReader socketBufferReader = new BufferedReader(new InputStreamReader(socketInputStream))) {

            while (true) {
                String receiveMessage = socketBufferReader.readLine();
                LogUtil.log("收到消息：" + receiveMessage);
                String ackMessage;
                if ("bye".equals(receiveMessage)) {
                    ackMessage = "bye";
                } else {
                    ackMessage = String.valueOf(receiveMessage.length());
                }
                printWriter.println(ackMessage);
                LogUtil.log("发送消息：" + ackMessage);
                if (ackMessage.equals("bye")) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            LogUtil.log("客户端：" + client.getInetAddress().getHostAddress() + ":" + client.getPort() + "已退出");
        }
    }
}
