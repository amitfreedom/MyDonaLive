package com.stream.donalive.ui.auth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}