package com.stream.prettylive.ui.follow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.prettylive.R;
import com.stream.prettylive.databinding.ActivityFollowFollowingBinding;
import com.stream.prettylive.global.AppConstants;
import com.stream.prettylive.global.ApplicationClass;
import com.stream.prettylive.ui.follow.adapter.FollowFollowingAdapter;
import com.stream.prettylive.ui.follow.methods.FirestoreHelper;
import com.stream.prettylive.ui.home.ui.profile.models.UserDetailsModel;
import com.stream.prettylive.ui.utill.Constant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FollowFollowingActivity extends AppCompatActivity {
    private static final String TAG = "FollowFollowingActivity";
    private ActivityFollowFollowingBinding binding;
    private CollectionReference usersRef;
    private FirebaseFirestore db;
    private FollowFollowingAdapter followFollowingAdapter;

    private String type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFollowFollowingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        type = getIntent().getStringExtra("type");
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);

        if (Objects.equals(type, "following")){
            binding.titleText.setText("Following");
            fetchFollowingUserList();
        }else {
            binding.titleText.setText("Followers");
            fetchFollowersUserList();
        }



        binding.backPress.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void fetchFollowingUserList() {
        new FirestoreHelper().fetchFollowingList(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID), new FirestoreHelper.FetchListCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                Log.i("onSuccess123", "onSuccess: "+result);
                if (result.size()>0){
                    binding.notFound.setVisibility(View.GONE);
                }else {
                    binding.notFound.setVisibility(View.VISIBLE);
                }
                getUserDetailsByUserId(result);
            }

            @Override
            public void onError(String error) {
                binding.notFound.setVisibility(View.VISIBLE);
                Toast.makeText(FollowFollowingActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                // Handle error: Show an error message or perform necessary actions
            }
        });
    }

    private void fetchFollowersUserList() {
        new FirestoreHelper().fetchFollowersList(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID), new FirestoreHelper.FetchListCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> result) {
                Log.i("onSuccess123", "onSuccess: "+result);
                if (result.size()>0){
                    binding.notFound.setVisibility(View.GONE);
                }else {
                    binding.notFound.setVisibility(View.VISIBLE);
                }
                getUserDetailsByUserId(result);
            }

            @Override
            public void onError(String error) {
                binding.notFound.setVisibility(View.VISIBLE);
                Toast.makeText(FollowFollowingActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                // Handle error: Show an error message or perform necessary actions
            }
        });
    }

    private void getUserDetailsByUserId(List<String> result) {
        usersRef.whereIn("userId", result)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    List<UserDetailsModel> userDetailsList = new ArrayList<>();

                    for (DocumentSnapshot document : value) {
                        UserDetailsModel userDetails = document.toObject(UserDetailsModel.class);
                        if (userDetails != null) {
                            userDetailsList.add(userDetails);
                        }
                    }

                    // Now userDetailsList contains UserDetails objects from Firestore
                    // Use the list as needed (e.g., display in UI, perform operations)
                    // For example:
                    updateUI(userDetailsList);
                });


    }

    private void updateUI(List<UserDetailsModel> userDetailsList) {
        followFollowingAdapter = new FollowFollowingAdapter(userDetailsList, new FollowFollowingAdapter.Select() {
            @Override
            public void onSelectUser(UserDetailsModel detailsModel) {
                Intent intent = new Intent(FollowFollowingActivity.this, UserInfoActivity.class);
                intent.putExtra("userId", detailsModel.getUserId());
                intent.putExtra("username", detailsModel.getUsername());
                intent.putExtra("image", detailsModel.getImage());
                intent.putExtra("uid", String.valueOf(detailsModel.getUid()));
                intent.putExtra("level", detailsModel.getLevel());
                startActivity(intent);
            }
        });
        binding.rvFollowingList.setAdapter(followFollowingAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}