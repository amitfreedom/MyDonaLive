package com.stream.donalive.ui.home.ui.profile;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.stream.donalive.R;
import com.stream.donalive.databinding.FragmentHomeBinding;
import com.stream.donalive.databinding.FragmentProfileBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;
import com.stream.donalive.ui.auth.activity.LoginActivity;
import com.stream.donalive.ui.home.HomeActivity;
import com.stream.donalive.ui.home.ui.profile.activity.EditProfileActivity;
import com.stream.donalive.ui.home.ui.profile.activity.HostRegistrationFormActivity;
import com.stream.donalive.ui.home.ui.profile.activity.LiveHistoryActivity;
import com.stream.donalive.ui.home.ui.profile.activity.UpdateUserDetailsActivity;
import com.stream.donalive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.donalive.ui.startup.activity.OnboardingActivity;
import com.stream.donalive.ui.utill.Constant;
import com.stream.donalive.ui.vip.VIPActivity;

import java.util.Objects;


public class ProfileFragment extends Fragment {

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
//        fetchUserDetails("sRRvN0AQndXsdvXAfKKJhV3nTin2");
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

                    // Now userDetailsList contains UserDetails objects from Firestore
                    // Use the list as needed (e.g., display in UI, perform operations)
                });

//        Query query = usersRef.whereEqualTo("userId", userId);
//
//        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (DocumentSnapshot document : task.getResult()) {
//                        userDetails = document.toObject(UserDetailsModel.class);
//                        updateUI(userDetails);
//
////                        username = document.getString("username");
//                    }
//                } else {
//                    // Handle failure
//                    Log.e("MainActivity", "Error getting user document: ", task.getException());
//                }
//            }
//        });
    }

    private void updateUI(UserDetailsModel userDetails) {
        binding.txtUsername.setText(userDetails.getUsername());
        binding.txtUid.setText("ID : "+String.valueOf(userDetails.getUid()));
        binding.txtCountry.setText(userDetails.getCountry_name());
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