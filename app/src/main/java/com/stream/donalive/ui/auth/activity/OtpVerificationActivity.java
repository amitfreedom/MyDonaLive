package com.stream.donalive.ui.auth.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityOtpVerificationBinding;

public class OtpVerificationActivity extends AppCompatActivity {
    private ActivityOtpVerificationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOtpVerificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding= null;
    }
}