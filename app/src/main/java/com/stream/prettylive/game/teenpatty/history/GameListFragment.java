package com.stream.prettylive.game.teenpatty.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentGameHistoryBinding;
import com.stream.prettylive.databinding.FragmentGameListBinding;
import com.stream.prettylive.game.teenpatty.adapter.GameListAdapter;
import com.stream.prettylive.game.teenpatty.adapter.HistoryListAdapter;
import com.stream.prettylive.game.teenpatty.models.GameList;
import com.stream.prettylive.game.teenpatty.models.HistoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameListFragment extends BottomSheetDialogFragment {
private FragmentGameListBinding binding;
    FirebaseFirestore mbase;
    GameListAdapter gameListAdapter;

    public interface OnGameSelectedListener {
        void onGameSelected(GameList game);
    }

    private OnGameSelectedListener mListener;

    public GameListFragment(OnGameSelectedListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        ViewGroup.LayoutParams params = binding.mainView.getLayoutParams();
//        params.height = (int) (height/3.5);
//        binding.mainView.setLayoutParams(params);
        init();
    }
    void init(){
        mbase= FirebaseFirestore.getInstance();
        CollectionReference gamesRef = mbase.collection("game_list");
        List<GameList> gameList = new ArrayList<>();
        // Get the documents in the "games" collection
        gamesRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    gameList.clear();
                    for (DocumentSnapshot document : task.getResult()) {

                        if (Boolean.TRUE.equals(document.getBoolean("active"))){
                            GameList game = document.toObject(GameList.class);
                            gameList.add(game);


                        }

                    }
                }
                gameListAdapter=new GameListAdapter(gameList, requireActivity(), new GameListAdapter.Select() {
                    @Override
                    public void onPress(GameList gameList) {
                        mListener.onGameSelected(gameList);
                        Objects.requireNonNull(getDialog()).dismiss();
                    }
                });
                Done();
            }
        });
    }
    void Done()
    {
        binding.gamelist.setAdapter(gameListAdapter);
    }

}