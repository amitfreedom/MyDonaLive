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

import java.util.Arrays;
import java.util.HashMap;
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
        // Update the current user's following list
        DocumentReference currentUserRef = db.collection("users").document(currentUserId);
        currentUserRef.set(new HashMap<String, Object>() {{
            put("following", Arrays.asList(targetUserId));
        }}, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        // Update the target user's followers list
        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
        targetUserRef.set(new HashMap<String, Object>() {{
            put("followers", Arrays.asList(currentUserId));
        }}, SetOptions.merge());
    }

    public void unfollowUser(String currentUserId, String targetUserId) {
        // Remove the targetUserId from the current user's following list
        DocumentReference currentUserRef = db.collection("users").document(currentUserId);
        currentUserRef.update("following", FieldValue.arrayRemove(targetUserId));

        // Remove the currentUserId from the target user's followers list
        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
        targetUserRef.update("followers", FieldValue.arrayRemove(currentUserId));
    }
}

