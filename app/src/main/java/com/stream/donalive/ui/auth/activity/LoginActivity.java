package com.stream.donalive.ui.auth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}