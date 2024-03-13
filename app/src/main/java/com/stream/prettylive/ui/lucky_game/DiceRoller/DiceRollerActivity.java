package com.stream.prettylive.ui.lucky_game.DiceRoller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityDiceRollerBinding;

public class DiceRollerActivity extends AppCompatActivity {

    private ActivityDiceRollerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDiceRollerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.rollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rollDice();
            }
        });
    }

    private void rollDice(){
        Dice dice = new Dice(6);
        int diceRoll = dice.roll();

        switch (diceRoll) {
            case 1:
                binding.diceImage.setImageResource(R.drawable.img_dice_1);
                showSnackbar(1);
                break;
            case 2:
                binding.diceImage.setImageResource(R.drawable.img_dice_2);
                showSnackbar(2);
                break;
            case 3:
                binding.diceImage.setImageResource(R.drawable.img_dice_3);
                showSnackbar(3);
                break;
            case 4:
                binding.diceImage.setImageResource(R.drawable.img_dice_4);
                showSnackbar(4);
                break;
            case 5:
                binding.diceImage.setImageResource(R.drawable.img_dice_5);
                showSnackbar(5);
                break;
            case 6:
                binding.diceImage.setImageResource(R.drawable.img_dice_6);
                showSnackbar(6);
                break;
        }
    }

    private void showSnackbar(int diceResult){
        String message = getString(R.string.dice_rolled) + " " + getString(R.string.result) + ": " + Integer.toString(diceResult);
        Snackbar snack = Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_SHORT);
        snack.show();
    }
}