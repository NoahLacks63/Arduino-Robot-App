package com.example.myapplication.udp;

import android.util.Log;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender implements Runnable{
    private static final String HOST = "125.00.00";
    private static final int PORT = 1234;

    private boolean running = true;

    private volatile byte[] message = new byte[0];

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(HOST);

            while (running) {
                byte[] buffer = message;

                DatagramPacket packet =
                        new DatagramPacket(buffer, buffer.length, address, PORT);

                socket.send(packet);
                Thread.sleep(100); // send every 100ms
            }

        } catch (Exception e) {
            Log.d("UDP", e.toString());
        }
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
