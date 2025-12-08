package com.example.myapplication.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver implements Runnable {
    // SINGLETON
    private static UDPReceiver udpReceiver;
    public static UDPReceiver getInstance() {
        if (udpReceiver == null) {
            udpReceiver = new UDPReceiver();
        }

        return udpReceiver;
    }

    private enum messageContents {
        //First byte
        VOLTAGE_LOW,
        VOLTAGE_HIGH
        //Last byte
    }

    public interface Listener {
        void onMessage(byte[] message, String senderIp, int senderPort);
    }

    private static final int listenPort = 1000;
    private final Listener listener;

    private volatile boolean running = true;
    private volatile boolean paused = true;

    private volatile byte[] message = new byte[0];

    public UDPReceiver() {
        listener = new Listener() {
            @Override
            public void onMessage(byte[] message, String senderIp, int senderPort) {
                UDPReceiver.this.message = message;
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
                if (paused) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                    // Wait for next packet (blocking)
                    socket.receive(packet);

                    byte[] msg = packet.getData();
                    String senderIp = packet.getAddress().getHostAddress();
                    int senderPort = packet.getPort();

                    // Pass the message to whoever wants it
                    listener.onMessage(msg, senderIp, senderPort);
                }
            }

        } catch (Exception e) {
            if (running) Log.d("UDP", String.valueOf(e));
        }
    }
    // TODO message receiver interpreter
    public double getRobotVoltage() {
        // Little-endian short read
        int low  = message[messageContents.VOLTAGE_LOW.ordinal()] & 0xFF;
        int high = message[messageContents.VOLTAGE_HIGH.ordinal()] & 0xFF;

        short s = (short) ((high << 8) | low);

        // Convert back to double
        return s / 32767.0;
    }

    public byte[] getMessage() {
        return message;
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }
}

