package com.example.myapplication;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver implements Runnable {

    public interface Listener {
        void onMessage(String message, String senderIp, int senderPort);
    }

    private static final int listenPort = 1000;
    private final Listener listener;
    private volatile boolean running = true;

    public UDPReceiver() {
        listener = new Listener() {
            @Override
            public void onMessage(String message, String senderIp, int senderPort) {

            }
        };
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket(listenPort)) {
            byte[] buffer = new byte[1024];

            while (running) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                // Wait for next packet (blocking)
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());
                String senderIp = packet.getAddress().getHostAddress();
                int senderPort = packet.getPort();

                // Pass the message to whoever wants it
                listener.onMessage(msg, senderIp, senderPort);
            }

        } catch (Exception e) {
            if (running) Log.d("UDP", String.valueOf(e));
        }
    }
}

