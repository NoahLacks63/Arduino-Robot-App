package com.example.myapplication.controller;

public class ControlInfo {

    private boolean[] buttonState;
    private double[] axisState;

    public ControlInfo() {
        buttonState = new boolean[Buttons.values().length];
        for (Buttons button : Buttons.values()) {
            buttonState[button.ordinal()] = false;
        }

        axisState = new double[Axes.values().length];
        for (Axes axis : Axes.values()) {
            axisState[axis.ordinal()] = 0.0;
        }
    }

    public boolean getButton(Buttons button) {
        return buttonState[button.ordinal()];
    }

    public void setButton(Buttons button, boolean value) {
        buttonState[button.ordinal()] = value;
    }

    public double getAxis(Axes axis) {
        return axisState[axis.ordinal()];
    }

    public void setAxis(Axes axis, double value) {
        axisState[axis.ordinal()] = value;
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
            boolean val = buttonState[key.ordinal()];

            if (val) {
                int byteIndex = i / 8;
                int bitPos = i % 8;
                out[byteIndex] |= (byte) (1 << bitPos);
            }
        }

        // ---- PACK SHORTS ----
        for (int i = 0; i < numShorts; i++) {
            Axes key = shortKeys[i];
            short s = (short) Math.round(axisState[key.ordinal()] * 32767);

            out[numFlagBytes + i*2]     = (byte)(s & 0xFF);        // low byte
            out[numFlagBytes + i*2 + 1] = (byte)((s >> 8) & 0xFF); // high byte
        }

        return out;
    }

}