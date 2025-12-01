package com.example.ftcrobotapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ControllerState {
    public static final int A = 0;
    public static final int B = 1;
    public static final int X = 2;
    public static final int Y = 3;
    public static final int DPAD_UP = 4;
    public static final int DPAD_LEFT = 5;
    public static final int DPAD_RIGHT = 6;
    public static final int DPAD_DOWN = 7;
    public static final int LB = 8;
    public static final int RB = 9;
    public static final int LEFT_STICK = 10;
    public static final int RIGHT_STICK = 11;
    public static final int START = 12;
    public static final int HOME = 13;
    public static final int LEFT_Y = 14;
    public static final int LEFT_X = 15;
    public static final int RIGHT_Y = 16;
    public static final int RIGHT_X = 17;
    public static final int RIGHT_TRIGGER = 18;
    public static final int LEFT_TRIGGER = 19;

    private final Map<Integer, Byte> controllerState;
    private final Map<Integer, Short> axisState;

    public ControllerState() {
        controllerState = new LinkedHashMap<>();

        controllerState.put(A, (byte) 0);
        controllerState.put(B, (byte) 0);
        controllerState.put(X, (byte) 0);
        controllerState.put(Y, (byte) 0);
        controllerState.put(DPAD_UP, (byte) 0);
        controllerState.put(DPAD_LEFT, (byte) 0);
        controllerState.put(DPAD_RIGHT, (byte) 0);
        controllerState.put(DPAD_DOWN, (byte) 0);
        controllerState.put(LB, (byte) 0);
        controllerState.put(RB, (byte) 0);
        controllerState.put(LEFT_STICK, (byte) 0);
        controllerState.put(RIGHT_STICK, (byte) 0);
        controllerState.put(START, (byte) 0);
        controllerState.put(HOME, (byte) 0);

        axisState = new LinkedHashMap<>();

        axisState.put(LEFT_Y, (short) 0);
        axisState.put(LEFT_X, (short) 0);
        axisState.put(RIGHT_Y, (short) 0);
        axisState.put(RIGHT_X, (short) 0);
        axisState.put(LEFT_TRIGGER, (short) 0);
        axisState.put(RIGHT_TRIGGER, (short) 0);
    }

    public byte getButton(int button) {
        return controllerState.get(button);
    }
    public void setButton(int button, boolean value) {
        if (value) {
            controllerState.put(button, (byte) 1);
        } else {
            controllerState.put(button, (byte) 0);
        }
        System.out.println("Button: " + String.valueOf(button) + String.valueOf(value));
    }

    public void setAxis(int axis, double value) {
        axisState.put(axis, (short) (value * 1000));
        System.out.println("Axis: " + String.valueOf(axis) + String.valueOf(value));
    }

    public byte[] getBytes() {
        ArrayList<Byte> result = new ArrayList<>();

        for (Map.Entry<Integer, Byte> entry : controllerState.entrySet()) {
            result.add(entry.getValue());
        }

        byte[] byteArray = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            byteArray[i] = result.get(i);
        }

        return byteArray;
    }

}