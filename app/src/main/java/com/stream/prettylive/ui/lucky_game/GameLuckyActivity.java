package com.stream.prettylive.ui.lucky_game;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityGameLuckyBinding;
import com.stream.prettylive.game.teenpatty.BottomSheetGameFragment;
import com.stream.prettylive.ui.lucky_game.DiceRoller.DiceRollerActivity;
import com.stream.prettylive.ui.lucky_game.HeadsOrTails.HeadsOrTailsActivity;
import com.stream.prettylive.ui.lucky_game.TruthOrDare.TruthOrDareActivity;

import java.util.ArrayList;

public class GameLuckyActivity extends AppCompatActivity {
    private ActivityGameLuckyBinding binding;
    private ArrayList<GamesLuckyModel> gamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGameLuckyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gamesList = new ArrayList<>();

        GamesLuckyModel teen_patti = new GamesLuckyModel(0, getString(R.string.teen_patti), R.drawable.teen_patti);
        gamesList.add(teen_patti);

        GamesLuckyModel fruits = new GamesLuckyModel(1, getString(R.string.fruite_loops), R.drawable.fruits);
        gamesList.add(fruits);

        GamesLuckyModel coin = new GamesLuckyModel(2, getString(R.string.head_or_tails), R.drawable.img_coin);
        gamesList.add(coin);

        GamesLuckyModel dice = new GamesLuckyModel(3, getString(R.string.dice_roller), R.drawable.img_dice);
        gamesList.add(dice);

        GamesLuckyModel bottle = new GamesLuckyModel(4, getString(R.string.truth_or_dare), R.drawable.img_bottle);
        gamesList.add(bottle);

        GamesLuckyModel spin = new GamesLuckyModel(5, getString(R.string.wheels_of_fortune), R.drawable.img_spin);
        gamesList.add(spin);

        GamesLuckyModel randomNumber = new GamesLuckyModel(6, getString(R.string.random_number), R.drawable.img_number_seven);
        gamesList.add(randomNumber);

        GamesLuckyModel slotMachine = new GamesLuckyModel(7, getString(R.string.slot_machine), R.drawable.img_slot_machine);
        gamesList.add(slotMachine);



//        binding.mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        GamesLuckyAdapter gamesAdapter = new GamesLuckyAdapter(gamesList, new GamesLuckyAdapter.Select() {
            @Override
            public void selectGame(int gameId) {
                if (gameId == 0) {
                    showBottomSheetDialog();
                }
                else if (gameId == 1) {
                    Toast.makeText(GameLuckyActivity.this, "coming soon...", Toast.LENGTH_SHORT).show();
                }else if (gameId == 2) {
                    Intent headsOrTailsIntent = new Intent(GameLuckyActivity.this, HeadsOrTailsActivity.class);
                    startActivity(headsOrTailsIntent);
                }else if (gameId == 3) {
                    Intent diceIntent = new Intent(GameLuckyActivity.this, DiceRollerActivity.class);
                    startActivity(diceIntent);
                }else if (gameId == 4) {
                    Intent diceIntent = new Intent(GameLuckyActivity.this, TruthOrDareActivity.class);
                    startActivity(diceIntent);
                }
                else {
//                    Toast.makeText(GameLuckyActivity.this, "coming soon...", Toast.LENGTH_SHORT).show();
                }
//                switch (gameId) {
//                    case 0:
//                        showBottomSheetDialog();
////                        Intent diceIntent = new Intent(context, DiceRoller.class);
////                        context.startActivity(diceIntent);
//                        break;
//                    case 4:
////                        Intent diceIntent = new Intent(context, DiceRoller.class);
////                        context.startActivity(diceIntent);
//                        break;
//                    case 3:
//                        Intent headsOrTailsIntent = new Intent(context, HeadsOrTails.class);
//                        context.startActivity(headsOrTailsIntent);
//                        break;
//                    case 5:
////                        Intent truthOrDareIntent = new Intent(context, TruthOrDare.class);
////                        context.startActivity(truthOrDareIntent);
//                        break;
//                    default:
//                        break;
//                }
            }
        });
        binding.mainRecyclerView.setAdapter(gamesAdapter);

        binding.imgBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void showBottomSheetDialog() {
        BottomSheetGameFragment bottomSheetDialogFragment = new BottomSheetGameFragment();
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}