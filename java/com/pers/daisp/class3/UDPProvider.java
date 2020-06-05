package com.pers.daisp.class3;

import com.pers.daisp.util.LogUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.UUID;

public class UDPProvider {

    public static void main(String[] args) throws IOException {
        String sn = UUID.randomUUID().toString();
        Provider provider = new Provider(sn);

        provider.start();
        System.in.read();
        provider.close();
    }

    private static class Provider extends Thread {
        private final String sn;
        private Boolean done = false;
        private DatagramSocket ds = null;

        public Provider(String sn) {
            this.sn = sn;
        }

        @Override
        public void run() {
            LogUtil.log("UDPProvider started.");

            try {
                ds = new DatagramSocket(20000);

                while (!done) {
                    final byte[] buf = new byte[512];
                    DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);

                    ds.receive(receivePacket);
                    int dataLength = receivePacket.getLength();
                    String message = new String(receivePacket.getData(), 0, dataLength);
                    LogUtil.log("Receive from ip:" + receivePacket.getAddress().getHostAddress()
                            + " port:" + receivePacket.getPort() + " data:" + message);

                    int responsePort = MessgaeCreator.parsePort(message);
                    if (responsePort != -1) {
                        //回送数据
                        String ackMessage = MessgaeCreator.buildWithSn(sn);
                        byte[] ackBytes = ackMessage.getBytes();
                        DatagramPacket ackPacket = new DatagramPacket(ackBytes, ackBytes.length,
                                receivePacket.getAddress(), responsePort);
                        ds.send(ackPacket);
                    }
                }
            } catch (Exception e) {
            } finally {
                close();
            }
            LogUtil.log("Finished.");
        }

        private void close() {
            if (ds != null) {
                ds.close();
                ds = null;
            }
        }

        private void exit() {
            done = true;
        }
    }
}
