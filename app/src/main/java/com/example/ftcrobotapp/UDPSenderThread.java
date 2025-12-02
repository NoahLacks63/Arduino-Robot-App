package com.example.ftcrobotapp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSenderThread extends Thread {
    private static final String HOST = "125.00.00";

    private static final int PORT_NUM = 0;

    private static InetAddress inetAddress;

    private static final DatagramSocket socket;

    static {
        try {
            socket = new DatagramSocket(PORT_NUM);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    public UDPSenderThread() {
        try {
            inetAddress = InetAddress.getByName(HOST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized DatagramPacket createPacket(byte[] message) {
        return new DatagramPacket(message, message.length, inetAddress, PORT_NUM);
    }

    public synchronized void sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
