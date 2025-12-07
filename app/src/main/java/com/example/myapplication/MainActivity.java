package com.example.myapplication;

import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.controller.Axes;
import com.example.myapplication.controller.Buttons;
import com.example.myapplication.controller.ControlInfo;
import com.example.myapplication.fragments.EnableFragment;
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
    private TextView lbTV;
    private TextView rbTV;
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

    private ControlInfo controlInfo;

    private UDPSender udpSender;
    private UDPReceiver receiver;

    private Thread udpSenderThread;
    private Thread udpReceiverThread;

    private final Handler handler = new Handler();
    private final Runnable hideRunnable = this::hideSystemUI;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        hideSystemUI();

        inputManager = (InputManager) getSystemService(Context.INPUT_SERVICE);

        aTV = findViewById(R.id.tv_AVal);
        bTV = findViewById(R.id.tv_BVal);
        xTV = findViewById(R.id.tv_XVal);
        yTV = findViewById(R.id.tv_YVal);
        dpadUpTV = findViewById(R.id.tv_DpadUpVal);
        dpadDownTV = findViewById(R.id.tv_DpadDownVal);
        dpadLeftTV = findViewById(R.id.tv_DpadLeftVal);
        dpadRightTV = findViewById(R.id.tv_DpadRightVal);
        lbTV = findViewById(R.id.tv_LBVal);
        rbTV = findViewById(R.id.tv_RBVal);
        selectTV = findViewById(R.id.tv_SelectVal);
        startTV = findViewById(R.id.tv_StartVal);
        modeTV = findViewById(R.id.tv_ModeVal);
        lstickTV = findViewById(R.id.tv_LeftStickVal);
        rstickTV = findViewById(R.id.tv_RightStickVal);

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

        controlInfo = new ControlInfo();

        udpSender = new UDPSender();
        receiver = new UDPReceiver();

        // TODO start sender thread
        udpSenderThread = new Thread(udpSender);
        // TODO start receiver thread
        udpReceiverThread = new Thread(receiver);

        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, new EnableFragment(controlInfo))
                .commit();
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

        Log.d("CONTROLLER", "Key: " + key);
        Log.d("CONTROLLER", "Val: " + value);

        TextView tv;

        switch (key) {
            case KeyEvent.KEYCODE_BUTTON_A:
                controlInfo.setButton(Buttons.A, value);
                tv = aTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_B:
                controlInfo.setButton(Buttons.B, value);
                tv = bTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_X:
                controlInfo.setButton(Buttons.X, value);
                tv = xTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_Y:
                controlInfo.setButton(Buttons.Y, value);
                tv = yTV;
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                controlInfo.setButton(Buttons.DPAD_UP, value);
                tv = dpadUpTV;
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                controlInfo.setButton(Buttons.DPAD_DOWN, value);
                tv = dpadDownTV;
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                controlInfo.setButton(Buttons.DPAD_LEFT, value);
                tv = dpadLeftTV;
                break;
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                controlInfo.setButton(Buttons.DPAD_RIGHT, value);
                tv = dpadRightTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_L1:
                controlInfo.setButton(Buttons.LB, value);
                tv = lbTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_R1:
                controlInfo.setButton(Buttons.RB, value);
                tv = rbTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
                controlInfo.setButton(Buttons.THUMBL, value);
                tv = lstickTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                controlInfo.setButton(Buttons.THUMBR, value);
                tv = rstickTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_START:
                controlInfo.setButton(Buttons.START, value);
                tv = startTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                controlInfo.setButton(Buttons.SELECT, value);
                tv = selectTV;
                break;
            case KeyEvent.KEYCODE_BUTTON_MODE:
                controlInfo.setButton(Buttons.MODE, value);
                tv = modeTV;
                break;
            default:
                return super.dispatchKeyEvent(event);
        }
        updateTextView(tv, value ? "\uD83D\uDFE9" : "\uD83D\uDFE5");

        udpSender.setMessage(controlInfo.getMessage());

        return true;
    }

    // Handle analog sticks and triggers
    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & InputDevice.SOURCE_JOYSTICK) == InputDevice.SOURCE_JOYSTICK &&
                event.getAction() == MotionEvent.ACTION_MOVE) {

            // Left stick
            double lX = event.getAxisValue(MotionEvent.AXIS_X);
            controlInfo.setAxis(Axes.LEFTX, lX);
            updateTextView(lxTV, "Left X " + String.format("%.2f", lX));
            updateProgressBar(lxPB, lX);

            double lY = event.getAxisValue(MotionEvent.AXIS_Y);
            controlInfo.setAxis(Axes.LEFTY, lY);
            updateTextView(lyTV, "Left Y " + String.format("%.2f", lY));
            updateProgressBar(lyPB, lY);

            // Right stick
            double rX = event.getAxisValue(MotionEvent.AXIS_Z);
            controlInfo.setAxis(Axes.RIGHTX, rX);
            updateTextView(rxTV, "Right X: " + String.format("%.2f", rX));
            updateProgressBar(rxPB, rX);

            double rY = event.getAxisValue(MotionEvent.AXIS_RZ);
            controlInfo.setAxis(Axes.RIGHTY, rY);
            updateTextView(ryTV, "Right Y: " + String.format("%.2f", rY));
            updateProgressBar(ryPB, rY);

            // Triggers
            double lt = event.getAxisValue(MotionEvent.AXIS_LTRIGGER);
            controlInfo.setAxis(Axes.LTRIGGER, lt);
            updateTextView(lTriggerTV, "Left Trigger: " + String.format("%.2f", lt));
            updateProgressBar(lTriggerPB, lt);

            double rt = event.getAxisValue(MotionEvent.AXIS_RTRIGGER);
            controlInfo.setAxis(Axes.RTRIGGER, rt);
            updateTextView(rTriggerTV, "Right Trigger: " + String.format("%.2f", rt));
            updateProgressBar(rTriggerPB, rt);

            boolean hUp = event.getAxisValue(MotionEvent.AXIS_HAT_X) == -1;
            boolean hDown = event.getAxisValue(MotionEvent.AXIS_HAT_X) == 1;
            boolean hLeft = event.getAxisValue(MotionEvent.AXIS_HAT_Y) == -1;
            boolean hRight = event.getAxisValue(MotionEvent.AXIS_HAT_Y) == 1;

            controlInfo.setButton(Buttons.DPAD_UP, hUp);
            updateTextView(dpadUpTV, hUp ? "\uD83D\uDFE9" : "\uD83D\uDFE5");
            controlInfo.setButton(Buttons.DPAD_DOWN, hDown);
            updateTextView(dpadDownTV, hDown ? "\uD83D\uDFE9" : "\uD83D\uDFE5");

            controlInfo.setButton(Buttons.DPAD_LEFT, hLeft);
            updateTextView(dpadLeftTV, hLeft ? "\uD83D\uDFE9" : "\uD83D\uDFE5");
            controlInfo.setButton(Buttons.DPAD_RIGHT, hRight);
            updateTextView(dpadRightTV, hRight ? "\uD83D\uDFE9" : "\uD83D\uDFE5");

            return true;
        }

        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        handler.removeCallbacks(hideRunnable);     // cancel pending hides
        handler.postDelayed(hideRunnable, 2000);   // re-hide after 2 seconds idle
        return super.dispatchTouchEvent(event);
    }


    public void updateTextView(TextView tv, String message) {
        tv.setText(message);
    }

    public void updateProgressBar(ProgressBar pb, double val) {
        int out = Math.toIntExact(Math.round(pb.getMax() * ((val + 1) / 2)));
        pb.setProgress(out);
    }

    private void hideSystemUI() {
        getWindow().setDecorFitsSystemWindows(false);

        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
            controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
    }

}