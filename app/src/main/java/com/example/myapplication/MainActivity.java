package com.example.myapplication;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.myapplication.controller.Axes;
import com.example.myapplication.controller.Buttons;
import com.example.myapplication.controller.Controller;
import com.example.myapplication.udp.UDPReceiver;
import com.example.myapplication.udp.UDPSender;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements InputManager.InputDeviceListener {
    private InputManager inputManager;

    //buttons
    private TextView aTV;
    private TextView bTV;
    private TextView xTV;
    private TextView yTV;
    private TextView dpadUpTV;
    private TextView dpadDownTV;
    private TextView dpadLeftTV;
    private TextView dpadRightTV;
    private TextView selectTV;
    private TextView startTV;
    private TextView modeTV;

    //sticks
    private TextView lxTV;
    private ProgressBar lxPB;
    private TextView lyTV;
    private ProgressBar lyPB;
    private TextView lstickTV;
    private TextView rxTV;
    private ProgressBar rxPB;
    private TextView ryTV;
    private ProgressBar ryPB;
    private TextView rstickTV;

    //triggers
    private TextView lTriggerTV;
    private ProgressBar lTriggerPB;
    private TextView rTriggerTV;
    private ProgressBar rTriggerPB;

    private Set<InputDevice> controllers;

    private Controller controller;

    private UDPSender udpSender;
    private UDPReceiver receiver;

    private Thread udpSenderThread;
    private Thread udpReceiverThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        inputManager = (InputManager) getSystemService(Context.INPUT_SERVICE);

        lxTV = findViewById(R.id.tv_LeftX);
        lxPB = findViewById(R.id.pb_LeftX);
        lyTV = findViewById(R.id.tv_LeftY);
        lyPB = findViewById(R.id.pb_LeftY);

        rxTV = findViewById(R.id.tv_RightX);
        rxPB = findViewById(R.id.pb_RightX);
        ryTV = findViewById(R.id.tv_RightY);
        ryPB = findViewById(R.id.pb_RightY);

        lTriggerTV = findViewById(R.id.tv_LTrigger);
        lTriggerPB = findViewById(R.id.pb_LTrigger);
        rTriggerTV = findViewById(R.id.tv_RTrigger);
        rTriggerPB = findViewById(R.id.pb_RTrigger);

        controllers = new HashSet<>();

        controller = new Controller();

        udpSender = new UDPSender();
        receiver = new UDPReceiver();

        // TODO start sender thread
        udpSenderThread = new Thread(udpSender);
        // TODO start receiver thread
        udpReceiverThread = new Thread(receiver);
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        InputDevice device = inputManager.getInputDevice(deviceId);
        if (device != null && (device.getSources() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            controllers.add(device);
        }
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        InputDevice device = inputManager.getInputDevice(deviceId);
        if (device != null && (device.getSources() & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD) {
            controllers.remove(device);
        }
    }

    // Handle controller buttons
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int key = event.getKeyCode();

        boolean value = (action == KeyEvent.ACTION_DOWN);

        switch (key) {
            case KeyEvent.KEYCODE_BUTTON_A:
                controller.setButton(Buttons.A, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_B:
                controller.setButton(Buttons.B, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_X:
                controller.setButton(Buttons.X, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_Y:
                controller.setButton(Buttons.Y, value);
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                controller.setButton(Buttons.DPAD_UP, value);
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                controller.setButton(Buttons.DPAD_DOWN, value);
                return true;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                controller.setButton(Buttons.DPAD_LEFT, value);
                return true;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                controller.setButton(Buttons.DPAD_RIGHT, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_L1:
                controller.setButton(Buttons.LB, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_R1:
                controller.setButton(Buttons.RB, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
                controller.setButton(Buttons.THUMBL, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                controller.setButton(Buttons.THUMBR, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_START:
                controller.setButton(Buttons.START, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                controller.setButton(Buttons.SELECT, value);
                return true;
            case KeyEvent.KEYCODE_BUTTON_MODE:
                controller.setButton(Buttons.MODE, value);
                return true;
        }

        udpSender.setMessage(controller.getMessage());

        return super.dispatchKeyEvent(event);
    }

    // Handle analog sticks and triggers
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Left stick
            double lX = event.getAxisValue(MotionEvent.AXIS_X);
            controller.setAxis(Axes.LEFTX, lX);
            updateTextView(lxTV, "Left X " + lX);
            updateProgressBar(lxPB, lX);

            double lY = event.getAxisValue(MotionEvent.AXIS_Y);
            controller.setAxis(Axes.LEFTY, lY);
            updateTextView(lyTV, "Left Y " + lY);
            updateProgressBar(lyPB, lY);

            // Right stick
            double rX = event.getAxisValue(MotionEvent.AXIS_Z);
            controller.setAxis(Axes.RIGHTX, rX);
            updateTextView(rxTV, "Right X: " + rX);
            updateProgressBar(rxPB, rX);

            double rY = event.getAxisValue(MotionEvent.AXIS_RZ);
            controller.setAxis(Axes.RIGHTY, rY);
            updateTextView(ryTV, "Right Y: " + rY);
            updateProgressBar(ryPB, rY);

            // Triggers
            double lt = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
            controller.setAxis(Axes.LTRIGGER, lt);
            updateTextView(lTriggerTV, "Left Trigger: " + lt);
            updateProgressBar(lTriggerPB, lt);

            double rt = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            controller.setAxis(Axes.RTRIGGER, rt);
            updateTextView(rTriggerTV, "Right Trigger: " + rt);
            updateProgressBar(rTriggerPB, rt);

            return true;
        }

        return super.onGenericMotionEvent(event);
    }

    public void updateTextView(TextView tv, String message) {
        tv.setText(message);
    }

    public void updateProgressBar(ProgressBar pb, double val) {
        int out = Math.toIntExact(Math.round(pb.getMax() * ((val + 1) / 2)));
        pb.setProgress(out);
    }
}