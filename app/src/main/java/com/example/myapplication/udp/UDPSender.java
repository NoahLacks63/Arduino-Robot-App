package com.example.myapplication.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class UDPSender implements Runnable{
    // SINGLETON
    private static UDPSender udpSender;
    public static UDPSender getInstance() {
        if (udpSender == null) {
            udpSender = new UDPSender();
        }

        return udpSender;
    }

    public static final String HOST = "192.168.4.1";
    private static final int PORT = 2390;

    private volatile boolean running = true;
    private volatile boolean paused = false;

    private volatile byte[] message = new byte[0];

    private InetAddress address;

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try {
            address = InetAddress.getByName(HOST);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        try (DatagramSocket socket = new DatagramSocket()) {
            while (running) {
                if (!paused) {
                    byte[] buffer = message;

                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, PORT);

                    socket.send(packet);
                    Thread.sleep(100); // send every 100ms
                }
            }

        } catch (Exception e) {
            Log.d("UDP", e.toString());
        }
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public byte[] getMessage() {
        return message;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
        try {
            address = InetAddress.getByName(HOST);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
