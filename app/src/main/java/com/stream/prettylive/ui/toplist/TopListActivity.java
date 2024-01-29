package com.stream.prettylive.ui.toplist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityTopListBinding;
import com.stream.prettylive.ui.search.activity.SearchUserActivity;
import com.stream.prettylive.ui.toplist.activity.TopUsersSendReceiveCoinsActivity;

public class TopListActivity extends AppCompatActivity {
    private ActivityTopListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.btnTopReceiver.setOnClickListener(v -> {
            Intent intent = new Intent(this, TopUsersSendReceiveCoinsActivity.class);
            startActivity(intent);
        });
        binding.btnTopSender.setOnClickListener(v -> {
            Intent intent = new Intent(this, TopUsersSendReceiveCoinsActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}