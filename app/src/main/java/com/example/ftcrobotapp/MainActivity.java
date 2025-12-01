package com.example.ftcrobotapp;

import android.os.Bundle;
import android.service.controls.Control;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int[] controllerIDs = getGameControllerIds().stream().mapToInt(Integer::intValue).toArray();
    }

    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = dev.getSources();

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }

    // records and updates controller state
    ControllerState controllerState = new ControllerState();

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean handled = true;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BUTTON_A:
                        controllerState.setButton(ControllerState.A, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        controllerState.setButton(ControllerState.B, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_X:
                        controllerState.setButton(ControllerState.X, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y:
                        controllerState.setButton(ControllerState.Y, true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        controllerState.setButton(ControllerState.DPAD_UP, true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        controllerState.setButton(ControllerState.DPAD_LEFT, true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        controllerState.setButton(ControllerState.DPAD_RIGHT, true);
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        controllerState.setButton(ControllerState.DPAD_DOWN, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_L1:
                        controllerState.setButton(ControllerState.LB, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_R1:
                        controllerState.setButton(ControllerState.RB, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBL:
                        controllerState.setButton(ControllerState.LEFT_STICK, true);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBR:
                        controllerState.setButton(ControllerState.RIGHT_STICK, true);
                        break;

                    default:
                        handled = false;
                        break;
                }
            }
            if (handled) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        boolean handled = true;
        if ((event.getSource() & InputDevice.SOURCE_GAMEPAD)
                == InputDevice.SOURCE_GAMEPAD) {
            if (event.getRepeatCount() == 0) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BUTTON_A:
                        controllerState.setButton(ControllerState.A, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_B:
                        controllerState.setButton(ControllerState.B, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_X:
                        controllerState.setButton(ControllerState.X, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_Y:
                        controllerState.setButton(ControllerState.Y, false);
                        break;
                    case KeyEvent.KEYCODE_DPAD_UP:
                        controllerState.setButton(ControllerState.DPAD_UP, false);
                        break;
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        controllerState.setButton(ControllerState.DPAD_LEFT, false);
                        break;
                    case KeyEvent.KEYCODE_DPAD_RIGHT:
                        controllerState.setButton(ControllerState.DPAD_RIGHT, false);
                        break;
                    case KeyEvent.KEYCODE_DPAD_DOWN:
                        controllerState.setButton(ControllerState.DPAD_DOWN, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_L1:
                        controllerState.setButton(ControllerState.LB, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_R1:
                        controllerState.setButton(ControllerState.RB, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBL:
                        controllerState.setButton(ControllerState.LEFT_STICK, false);
                        break;
                    case KeyEvent.KEYCODE_BUTTON_THUMBR:
                        controllerState.setButton(ControllerState.RIGHT_STICK, false);
                        break;

                    default:
                        handled = false;
                        break;
                }
            }
            if (handled) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        // Check that the event came from a game controller
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK
                && event.getAction() == MotionEvent.ACTION_MOVE) {

            // Process all historical movement samples in the batch
            final int historySize = event.getHistorySize();

            // Process the movements starting from the
            // earliest historical position in the batch
            for (int i = 0; i < historySize; i++) {
                // Process the event at historical position i
                processJoystickInput(event, i);
            }

            // Process the current movement sample in the batch (position -1)
            processJoystickInput(event, -1);
            return true;
        }

        return super.onGenericMotionEvent(event);
    }

    private static float getCenteredAxis(MotionEvent event, InputDevice device, int axis, int historyPos) {
        final InputDevice.MotionRange range = device.getMotionRange(axis, event.getSource());

        // A joystick at rest does not always report an absolute position of
        // (0,0). Use the getFlat() method to determine the range of values
        // bounding the joystick axis center.
        if (range != null) {
            final float flat = range.getFlat();
            final float value = historyPos < 0 ?
                    event.getAxisValue(axis):
                    event.getHistoricalAxisValue(axis, historyPos);

            // Ignore axis values that are within the 'flat' region of the
            // joystick axis center.
            if (Math.abs(value) > flat) {
                return value;
            }
        }

        return 0;
    }

    private void processJoystickInput(MotionEvent event, int historyPos) {

        InputDevice inputDevice = event.getDevice();

        float lX = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_X, historyPos);
        float lY = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Y, historyPos);

        float rX = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_Z, historyPos);
        float rY = getCenteredAxis(event, inputDevice, MotionEvent.AXIS_RZ, historyPos);

        float lTrigger = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
        float rTrigger = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);

        controllerState.setAxis(ControllerState.LEFT_X, lX);
        controllerState.setAxis(ControllerState.LEFT_Y, lY);

        controllerState.setAxis(ControllerState.RIGHT_X, rX);
        controllerState.setAxis(ControllerState.RIGHT_Y, rY);

        controllerState.setAxis(ControllerState.LEFT_TRIGGER, lTrigger);
        controllerState.setAxis(ControllerState.RIGHT_TRIGGER, rTrigger);
    }
}