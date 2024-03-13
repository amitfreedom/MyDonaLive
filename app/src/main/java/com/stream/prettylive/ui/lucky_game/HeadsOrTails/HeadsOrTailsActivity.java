package com.stream.prettylive.ui.lucky_game.HeadsOrTails;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityHeadsOrTailsBinding;

public class HeadsOrTailsActivity extends AppCompatActivity {

    private ActivityHeadsOrTailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHeadsOrTailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.coinImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snack = Snackbar.make(v, R.string.flip_a_coin, Snackbar.LENGTH_SHORT);
                snack.show();
                flipCoin();
            }
        });
    }

    private void flipCoin() {
        Coin coin = new Coin();
        int coinFlip = coin.flip();

        binding.coinImage.animate().setDuration(1000).rotationXBy(1800f).withEndAction(new Runnable() {
            @Override
            public void run() {
                binding.coinImage.setClickable(true);
                if (coinFlip == 1) {
                    binding.coinImage.setImageResource(R.drawable.img_coin);
                    binding.headsOrTailsResultText.setText(getString(R.string.tails));
                } else if (coinFlip == 2) {
                    binding.coinImage.setImageResource(R.drawable.img_coin_heads);
                    binding.headsOrTailsResultText.setText(getString(R.string.heads));
                }
            }
        }).start();
    }
}