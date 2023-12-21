package com.stream.donalive.ui.home.ui.profile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityLiveHistoryBinding;
import com.stream.donalive.ui.home.ui.profile.LiveHistoryAdapter;

public class LiveHistoryActivity extends AppCompatActivity {

    private ActivityLiveHistoryBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setAdapter();
        binding.imgBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void setAdapter() {
        LiveHistoryAdapter liveHistoryAdapter = new LiveHistoryAdapter();
        binding.rvLiveHistory.setAdapter(liveHistoryAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}