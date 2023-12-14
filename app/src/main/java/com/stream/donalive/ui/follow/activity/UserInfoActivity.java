package com.stream.donalive.ui.follow.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stream.donalive.R;
import com.stream.donalive.databinding.ActivityUserInfoBinding;
import com.stream.donalive.global.AppConstants;
import com.stream.donalive.global.ApplicationClass;

import java.util.HashMap;
import java.util.Map;

public class UserInfoActivity extends AppCompatActivity {

    private ActivityUserInfoBinding binding;
    private String userId;
    private FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mFirestore = FirebaseFirestore.getInstance();

        userId = getIntent().getStringExtra("userId");
        Toast.makeText(this, ""+userId, Toast.LENGTH_SHORT).show();

        binding.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser(ApplicationClass.getSharedpref().getString(AppConstants.USER_ID),userId);

            }
        });
    }

    public void checkIfFriends(String userId1, String userId2) {
        mFirestore.collection("relationships")
                .whereEqualTo("followerId", userId1)
                .whereEqualTo("followedId", userId2)
                .whereEqualTo("status", "following")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // If the query result is not empty, users are friends
                    if (!queryDocumentSnapshots.isEmpty()) {
                        // Users are friends
                        // You can update UI or perform additional tasks
                    } else {
                        // Users are not friends
                        // You can update UI or perform additional tasks
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }


    public void unfollowUser(String followerId, String followedId) {
        mFirestore.collection("relationships")
                .whereEqualTo("followerId", followerId)
                .whereEqualTo("followedId", followedId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        documentSnapshot.getReference().delete()
                                .addOnSuccessListener(aVoid -> {
                                    // Relationship deleted successfully
                                    // You can update UI or perform additional tasks
                                })
                                .addOnFailureListener(e -> {
                                    // Handle the error
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }

    public void followUser(String followerId, String followedId) {
        Map<String, Object> relationshipData = new HashMap<>();
        relationshipData.put("followerId", followerId);
        relationshipData.put("followedId", followedId);
        relationshipData.put("status", "following");

        mFirestore.collection("relationships")
                .add(relationshipData)
                .addOnSuccessListener(documentReference -> {
                    // Relationship added successfully
                    // You can update UI or perform additional tasks
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}