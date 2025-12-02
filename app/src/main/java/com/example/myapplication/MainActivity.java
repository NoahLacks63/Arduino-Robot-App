package com.example.myapplication;

import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private TextView output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ensure Activity receives key events
        output.setFocusable(true);
        output.setFocusableInTouchMode(true);
        output.requestFocus();
    }

    Controller controller = new Controller();

    // Handle controller buttons
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (updateButton(keyCode, true)) {
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (updateButton(keyCode, false)) {
            return true;
        } else {
            return super.onKeyUp(keyCode, event);
        }
    }

    private boolean updateButton(int keyCode, boolean value) {
        switch (keyCode) {
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

        return false;
    }

    // Handle analog sticks and triggers
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Left stick
            controller.setAxis(Axes.LEFTX, event.getAxisValue(MotionEvent.AXIS_X));
            controller.setAxis(Axes.LEFTY, event.getAxisValue(MotionEvent.AXIS_Y));

            // Right stick
            controller.setAxis(Axes.RIGHTX, event.getAxisValue(MotionEvent.AXIS_Z));
            controller.setAxis(Axes.RIGHTY, event.getAxisValue(MotionEvent.AXIS_RZ));

            // Triggers
            controller.setAxis(Axes.LTRIGGER, event.getAxisValue(MotionEvent.AXIS_LTRIGGER));
            controller.setAxis(Axes.RTRIGGER, event.getAxisValue(MotionEvent.AXIS_RTRIGGER));

            return true;
        }

        return super.onGenericMotionEvent(event);
    }
}