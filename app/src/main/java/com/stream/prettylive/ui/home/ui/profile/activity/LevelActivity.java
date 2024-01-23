package com.stream.prettylive.ui.home.ui.profile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityLevelBinding;
import com.stream.prettylive.ui.home.ui.profile.adapter.LevelAdapter;
import com.stream.prettylive.ui.home.ui.profile.models.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelActivity extends AppCompatActivity {
    private ActivityLevelBinding binding;
    private LevelAdapter levelAdapter;
    private int basePrice =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLevelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        List<Level> levels = generateLevels();
        levelAdapter = new LevelAdapter(levels);
        binding.recyclerView.setAdapter(levelAdapter);

        binding.backPress.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private List<Level> generateLevels() {
        List<Level> levels = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            // Calculate the price based on the value of i
            int price = basePrice + ((i - 1) * 300000);
            levels.add(new Level(price,"200"));
        }
        return levels;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}