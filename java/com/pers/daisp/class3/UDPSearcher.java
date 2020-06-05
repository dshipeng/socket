package com.pers.daisp.class3;

import com.pers.daisp.util.LogUtil;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class UDPSearcher {
    private static final int LISTEN_PORT = 30000;

    public static void main(String[] args) throws Exception {
        LogUtil.log("UDPSearcher started.");
        Listener listener = listen();
        send();
        LogUtil.log("UDPSearch finished.");
        System.in.read();
        List<Device> devices = listener.getDeviceListAndClose();
        for (Device device : devices) {
            LogUtil.log("Device:" + device.toString());
        }
    }

    private static Listener listen() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, countDownLatch);
        listener.start();
        countDownLatch.await();
        return listener;
    }

    private static void send() throws Exception {
        LogUtil.log("UDPSearcher sendBroadcast started.");

        DatagramSocket ds = new DatagramSocket();


        String sendMessage = MessgaeCreator.buildWithPort(LISTEN_PORT);
        byte[] ackBytes = sendMessage.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(ackBytes, ackBytes.length);
        //广播地址
        sendPacket.setAddress(InetAddress.getByName("255.255.255.255"));
        sendPacket.setPort(20000);
        ds.send(sendPacket);

        LogUtil.log("UDPSearcher sendBroadcast finished.");
        ds.close();
    }

    private static class Device{
        final int port;
        final String ip;
        final String sn;

        private Device(int port, String ip, String sn) {
            this.port = port;
            this.ip = ip;
            this.sn = sn;
        }

        @Override
        public String toString() {
            return "Device{" +
                    "port=" + port +
                    ", ip='" + ip + '\'' +
                    ", sn='" + sn + '\'' +
                    '}';
        }
    }

    private static class Listener extends Thread {
        private final int listenPort;
        private final CountDownLatch countDownLatch;
        private final List<Device> deviceList = new ArrayList<>();
        private Boolean done = false;
        private DatagramSocket ds = null;

        public Listener(int listenPort, CountDownLatch countDownLatch) {
            this.listenPort = listenPort;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            //通知启动
            countDownLatch.countDown();

            try {
                ds = new DatagramSocket(listenPort);
                while (!done) {
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

                    ds.receive(receivePacket);
                    int dataLength = receivePacket.getLength();
                    String message = new String(receivePacket.getData(), 0, dataLength);
                    LogUtil.log("UDPSearcher receive from ip:" + receivePacket.getAddress().getHostAddress()
                            + " port:" + receivePacket.getPort() + " data:" + message);
                    String sn = MessgaeCreator.parseSn(message);
                    if (sn != null) {
                        Device device = new Device(receivePacket.getPort(), receivePacket.getAddress().getHostAddress(), sn);
                        deviceList.add(device);
                    }
                }
            } catch (Exception e) {

            } finally {
                close();
            }
            LogUtil.log("UDPSearcher listener finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        public List<Device> getDeviceListAndClose() {
            done = true;
            close();
            return deviceList;
        }
    }
}
