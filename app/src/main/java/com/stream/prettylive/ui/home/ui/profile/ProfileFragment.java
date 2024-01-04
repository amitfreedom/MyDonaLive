package com.stream.prettylive.ui.home.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.FragmentHomeBinding;
import com.stream.prettylive.databinding.FragmentProfileBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
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

import java.util.List;
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

    private void updateUI(UserDetailsModel userDetails) {
        try {
            binding.txtUsername.setText(userDetails.getUsername());
            binding.txtUid.setText("ID : "+String.valueOf(userDetails.getUid()));
            binding.txtCountry.setText(userDetails.getCountry_name());
            binding.txtCoin.setText(new Convert().prettyCount(Integer.parseInt(userDetails.getCoins())));
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
}