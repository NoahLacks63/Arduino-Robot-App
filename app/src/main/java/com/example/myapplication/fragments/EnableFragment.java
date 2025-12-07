package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.controller.ControlInfo;
import com.example.myapplication.controller.RobotState;

public class EnableFragment extends Fragment {
    private ControlInfo controlInfo;

    public EnableFragment(ControlInfo controlInfo) {
        super(R.layout.fragment_enable);

        this.controlInfo = controlInfo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.btn_Disable);

        button.setOnClickListener(v -> {
            controlInfo.setRobotState(RobotState.ENABLED, true);

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new DisableFragment(controlInfo))
                    .commit();
        });
    }
}
