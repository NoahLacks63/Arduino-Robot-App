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

    private Map<Integer, Integer> controllerState;

    public ControllerState() {
        controllerState = new LinkedHashMap<>();

        controllerState.put(A, 0);
        controllerState.put(B, 0);
        controllerState.put(X, 0);
        controllerState.put(Y, 0);
        controllerState.put(DPAD_UP, 0);
        controllerState.put(DPAD_LEFT, 0);
        controllerState.put(DPAD_RIGHT, 0);
        controllerState.put(DPAD_DOWN, 0);
        controllerState.put(LB, 0);
        controllerState.put(RB, 0);
        controllerState.put(LEFT_STICK, 0);
        controllerState.put(RIGHT_STICK, 0);
        controllerState.put(START, 0);
        controllerState.put(HOME, 0);
        controllerState.put(LEFT_Y, 0);
        controllerState.put(LEFT_X, 0);
        controllerState.put(RIGHT_Y, 0);
        controllerState.put(RIGHT_X, 0);
    }

    public void setButton(int button, boolean value) {
        if (value) {
            controllerState.put(button, 1);
        }
    }

    public void setAxis(int axis, double value) {
        controllerState.put(axis, (int) (value * 1000));
    }

    public byte[] getBytes() {
        ArrayList<Byte> result = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : controllerState.entrySet()) {
            Integer value = entry.getValue();
            result.add(value.byteValue());
        }

        byte[] byteArray = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            byteArray[i] = result.get(i);
        }

        return byteArray;
    }

}