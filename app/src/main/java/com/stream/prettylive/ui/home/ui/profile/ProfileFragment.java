package com.stream.prettylive.ui.home.ui.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentHomeBinding;
import com.stream.prettylive.databinding.FragmentProfileBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.streaming.internal.ZEGOLiveAudioRoomManager;
import com.stream.prettylive.ui.follow.activity.FollowFollowingActivity;
import com.stream.prettylive.ui.follow.methods.FirestoreHelper;
import com.stream.prettylive.ui.home.ui.profile.activity.HostRegistrationFormActivity;
import com.stream.prettylive.ui.home.ui.profile.activity.LiveHistoryActivity;
import com.stream.prettylive.ui.home.ui.profile.activity.UpdateUserDetailsActivity;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.startup.activity.OnboardingActivity;
import com.stream.prettylive.ui.utill.Constant;
import com.stream.prettylive.ui.utill.Convert;
import com.stream.prettylive.ui.vip.VIPActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private FragmentProfileBinding binding;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);

        init();
        fetchFollowingUserList();
        fetchFollowersUserList();
        fetchUserDetails(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID));

//        updateLevel(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),50000,1);

    }

    private void init() {
        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent mainIntent = new Intent(getActivity(), OnboardingActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                getActivity().finish();
            }
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), UpdateUserDetailsActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.btnHostRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), HostRegistrationFormActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.btnLiveHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), LiveHistoryActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.btnVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), VIPActivity.class);
                startActivity(mainIntent);
            }
        });
        binding.btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), FollowFollowingActivity.class);
                mainIntent.putExtra("type","following");
                startActivity(mainIntent);
            }
        });
        binding.btnFollowers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), FollowFollowingActivity.class);
                mainIntent.putExtra("type","follow");
                startActivity(mainIntent);
            }
        });
    }

    private void fetchUserDetails(String userId) {

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
                        updateUI(userDetails);
                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(UserDetailsModel userDetails) {
        try {
            binding.txtUsername.setText(userDetails.getUsername());
            binding.txtUid.setText("ID : "+String.valueOf(userDetails.getUid()));
            binding.txtCountry.setText(userDetails.getCountry_name());
            binding.txtLevel.setText("Lv"+userDetails.getLevel());
            binding.txtCoin.setText(new Convert().prettyCount(Integer.parseInt(userDetails.getCoins())));
//            binding.txtCoin.setText(new Convert().prettyCount(Integer.parseInt("150000")));
            binding.txtDiamond.setText(new Convert().prettyCount(Integer.parseInt(userDetails.getDiamond())));
            // Load image
            if (Objects.equals(userDetails.getImage(), "")){
                Glide.with(getActivity())
                        .load(Constant.USER_PLACEHOLDER_PATH)
                        .into(binding.profileImage);
            }else {
                Glide.with(getActivity())
                        .load(userDetails.getImage())
                        .into(binding.profileImage);
            }
            if (!Objects.equals(userDetails.getImage(), "")) {
                ZEGOLiveAudioRoomManager.getInstance().updateUserAvatarUrl(userDetails.getImage(),(userAvatarUrl, errorInfo) -> {

                    Log.i("3456789", "userAvatarUrl: "+userAvatarUrl);

                });
            }

            updateLevel(userDetails.getUserId(),Long.parseLong(userDetails.getDiamond()),Integer.parseInt(userDetails.getLevel()));
        }catch (Exception e){
            Log.i(TAG, "updateUI: "+e);
        }

    }



    private void fetchFollowingUserList() {
        new FirestoreHelper().fetchFollowingList(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID), new FirestoreHelper.FetchListCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                int followingCount = result.size();
                binding.txtFollowingCount.setText(String.valueOf(followingCount));
            }

            @Override
            public void onError(String error) {
//                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                // Handle error: Show an error message or perform necessary actions
            }
        });
    }

    private void fetchFollowersUserList() {
        new FirestoreHelper().fetchFollowersList(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID), new FirestoreHelper.FetchListCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                int followersCount = result.size();
                binding.txtFollowersCount.setText(String.valueOf(followersCount));
            }

            @Override
            public void onError(String error) {
//                Toast.makeText(getActivity(), ""+error, Toast.LENGTH_SHORT).show();
                // Handle error: Show an error message or perform necessary actions
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateLevel(String userId,long currentCoins,int currentLevel) {
        if (currentCoins >= calculateCoinsForNextLevel(currentLevel)) {
            int newLevel = currentLevel + 1;
        try {
            db = FirebaseFirestore.getInstance();
            CollectionReference liveDetailsRef = db.collection(Constant.LOGIN_DETAILS);

            // Create a query to find the document with the given userId
            Query query = liveDetailsRef.whereEqualTo("userId", userId);

            query.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // Get the document ID for the matched document
                        String documentId = document.getId();

                        Map<String, Object> updateDetails = new HashMap<>();
                        updateDetails.put("level", String.valueOf(newLevel));

                        // Update the liveType field from 0 to 1
                        liveDetailsRef.document(documentId)
                                .update(updateDetails)
                                .addOnSuccessListener(aVoid -> {
                                })
                                .addOnFailureListener(e -> {
                                });
                    }
                } else {
                    Log.e("UpdateLiveType", "Error getting documents: ", task.getException());
                }
            });

        }catch (Exception e){

        }
        }
    }
    private long calculateCoinsForNextLevel(int currentLevel) {
        // Customize this method based on your level-up logic
        return (currentLevel + 1) * 300000; // Example: 150,000 coins per level
    }
}