package com.stream.prettylive.ui.home.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentGamesBinding;
import com.stream.prettylive.game.teenpatty.BottomSheetGameFragment;
import com.stream.prettylive.game.teenpatty.adapter.GameListAdapter;
import com.stream.prettylive.game.teenpatty.models.GameList;
import com.stream.prettylive.ui.home.ui.home.adapter.ActiveUserAdapter;
import com.stream.prettylive.ui.home.ui.home.adapter.GameAllAdapter;
import com.stream.prettylive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GamesFragment extends Fragment implements GameAllAdapter.OnSelectedGameSelectedListener {

    private FragmentGamesBinding binding;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    GameAllAdapter gameAllAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGamesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection(Constant.GAME_ALL_LIST)
//                .orderBy("startTime", Query.Direction.DESCENDING)
//                .whereEqualTo("liveStatus","online")

//                .whereNotEqualTo("userId",ApplicationClass.getSharedpref().getString(AppConstants.USER_ID))
                .limit(5);

        init1();
    }

    private void init1() {
        gameAllAdapter = new GameAllAdapter(mQuery, this) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerAllGame.setVisibility(View.GONE);
//                    binding.viewEmpty.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerAllGame.setVisibility(View.VISIBLE);
//                    binding.viewEmpty.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
            }


        };
        binding.recyclerAllGame.setAdapter(gameAllAdapter);
    }

    @Override
    public void onSelectedGameSelected(DocumentSnapshot user) {

        if (Objects.equals(user.getString("title"), "Teen Patty") && user.getBoolean("active")) {
            showBottomSheetDialog();
        }
        else if (Objects.equals(user.getString("title"), "Fruits loops")  && user.getBoolean("active")) {
            Toast.makeText(requireActivity(), "coming soon...", Toast.LENGTH_SHORT).show();
        }

    }

    public void showBottomSheetDialog() {
        BottomSheetGameFragment bottomSheetDialogFragment = new BottomSheetGameFragment();
        bottomSheetDialogFragment.show(requireActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }


    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (gameAllAdapter != null) {
            gameAllAdapter.startListening();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (gameAllAdapter != null) {
            gameAllAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (gameAllAdapter != null) {
            gameAllAdapter.stopListening();
        }
//        binding=null;
    }
}