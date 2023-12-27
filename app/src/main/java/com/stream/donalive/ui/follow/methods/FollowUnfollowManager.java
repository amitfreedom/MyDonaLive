package com.stream.donalive.ui.follow.methods;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FollowUnfollowManager {

    public interface Select{
        void onSuccess(String status);
    }

    Select select;

    public FollowUnfollowManager(Select select) {
        this.select = select;
    }

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public void followUser(String currentUserId, String targetUserId) {

        // Get a reference to the current user's document
        DocumentReference currentUserRef = db.collection("users").document(currentUserId);
        currentUserRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> followingList = (List<String>) documentSnapshot.get("following");

                if (followingList == null) {
                    followingList = new ArrayList<>();
                }

                // Add the new user to the following list if not already present
                if (!followingList.contains(targetUserId)) {
                    followingList.add(targetUserId);

                    // Update the "following" field in the document
                    currentUserRef.update("following", followingList)
                            .addOnSuccessListener(aVoid -> {
                                select.onSuccess("You have successfully followed the user");
                            })
                            .addOnFailureListener(e -> {
                                select.onSuccess("Failed to update following list: " + e.getMessage());
                            });
                } else {
                    // The user is already in the following list
                    select.onSuccess("You are already following this user");
                }
            } else {
//                select.onSuccess("User document does not exist");
                // Update the current user's following list
//                DocumentReference currentUserRef = db.collection("users").document(currentUserId);
                currentUserRef.set(new HashMap<String, Object>() {{
                    put("following", Arrays.asList(targetUserId));
                }}).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        select.onSuccess("You have successfully following");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        select.onSuccess("Something went wrong"+e);
                    }
                });
            }
        }).addOnFailureListener(e -> {
            select.onSuccess("Failed to fetch user data: " + e.getMessage());
        });


        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
        targetUserRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> followersList = (List<String>) documentSnapshot.get("followers");

                if (followersList == null) {
                    followersList = new ArrayList<>();
                }

                // Add the new user to the following list if not already present
                if (!followersList.contains(currentUserId)) {
                    followersList.add(currentUserId);

                    // Update the "following" field in the document
                    targetUserRef.update("followers", followersList)
                            .addOnSuccessListener(aVoid -> {
//                                select.onSuccess("You have successfully followed the user");
                            })
                            .addOnFailureListener(e -> {
//                                select.onSuccess("Failed to update following list: " + e.getMessage());
                            });
                } else {
                    // The user is already in the following list
                    select.onSuccess("You are already following this user");
                }
            } else {
//                select.onSuccess("User document does not exist");
                // Update the current user's following list
//                DocumentReference currentUserRef = db.collection("users").document(currentUserId);
                targetUserRef.set(new HashMap<String, Object>() {{
                    put("followers", Arrays.asList(currentUserId));
                }}).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
//                        select.onSuccess("You have successfully following");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        select.onSuccess("Something went wrong"+e);
                    }
                });
            }
        }).addOnFailureListener(e -> {
            select.onSuccess("Failed to fetch user data: " + e.getMessage());
        });






        // Update the target user's followers list
//        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
//        targetUserRef.set(new HashMap<String, Object>() {{
//            put("followers", Arrays.asList(currentUserId));
//        }});
    }

    public void unfollowUser(String currentUserId, String targetUserId) {
        // Remove the targetUserId from the current user's following list
        DocumentReference currentUserRef = db.collection("users").document(currentUserId);
        currentUserRef.update("following", FieldValue.arrayRemove(targetUserId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                select.onSuccess("You have successfully unFollow");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                select.onSuccess("Something went wrong"+e);
            }
        });

        // Remove the currentUserId from the target user's followers list
        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
        targetUserRef.update("followers", FieldValue.arrayRemove(currentUserId));
    }
}

