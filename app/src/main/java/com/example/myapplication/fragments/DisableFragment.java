package com.example.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.controller.ControlInfo;

public class DisableFragment extends Fragment {
    private ControlInfo controlInfo;

    public DisableFragment(ControlInfo controlInfo) {
        super(R.layout.fragment_disable);

        this.controlInfo = controlInfo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button button = view.findViewById(R.id.btn_Disable);

        button.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, new EnableFragment(controlInfo))
                    .addToBackStack(null)
                    .commit();
        });
    }
}
