package com.stream.prettylive.ui.lucky_game.TruthOrDare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityTruthOrDareBinding;

public class TruthOrDareActivity extends AppCompatActivity {

    private ActivityTruthOrDareBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTruthOrDareBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        binding.bottleImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnBottle();
            }
        });
    }

    private void turnBottle() {
        Bottle bottle = new Bottle();
        int turnBottle = bottle.turn();

        binding.bottleImage.animate().setDuration(3000).rotationBy(turnBottle).withEndAction(new Runnable() {
            @Override
            public void run() {
                binding.bottleImage.setClickable(true);
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_refresh) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}