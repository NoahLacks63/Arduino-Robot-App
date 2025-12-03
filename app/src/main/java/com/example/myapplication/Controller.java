package com.example.myapplication;

import java.util.LinkedHashMap;
import java.util.Map;

public class Controller {

    private Map<Buttons, Boolean> buttonState;
    private Map<Axes, Double> axisState;

    public Controller() {
        buttonState = new LinkedHashMap<>();

        for (Buttons button : Buttons.values()) {
            buttonState.put(button, false);
        }

        for (Axes axis : Axes.values()) {
            axisState.put(axis, 0.0);
        }
    }

    public boolean getButton(Buttons button) {
        return buttonState.get(button);
    }

    public void setButton(Buttons button, boolean value) {
        buttonState.put(button, value);
    }

    public double getAxis(Axes axis) {
        return axisState.get(axis);
    }

    public void setAxis(Axes axis, double value) {
        axisState.put(axis, value);
    }

    public byte[] getMessage() {
        Buttons [] boolKeys = Buttons.values();
        Axes[] shortKeys = Axes.values();

        int numFlags = boolKeys.length;
        int numFlagBytes = (numFlags + 7) / 8;

        int numShorts = shortKeys.length;
        int numShortBytes = numShorts * 2;

        byte[] out = new byte[numFlagBytes + numShortBytes];

        // ---- PACK BOOLEAN FLAGS ----
        for (int i = 0; i < numFlags; i++) {
            Buttons key = boolKeys[i];
            boolean val = buttonState.getOrDefault(key, false);

            if (val) {
                int byteIndex = i / 8;
                int bitPos = i % 8;
                out[byteIndex] |= (byte) (1 << bitPos);
            }
        }

        // ---- PACK SHORTS ----
        for (int i = 0; i < numShorts; i++) {
            Axes key = shortKeys[i];
            short s = (short) Math.round(axisState.getOrDefault(key, 0.0) * 32767);

            out[numFlagBytes + i*2]     = (byte)(s & 0xFF);        // low byte
            out[numFlagBytes + i*2 + 1] = (byte)((s >> 8) & 0xFF); // high byte
        }

        return out;
    }

}