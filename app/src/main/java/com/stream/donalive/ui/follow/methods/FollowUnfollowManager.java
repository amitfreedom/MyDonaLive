package com.stream.donalive.ui.follow.methods;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FollowUnfollowManager {

    public static void followUser(String userId, String targetUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Add targetUserId to the current user's following list
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update("following." + targetUserId, true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("addOnSuccessListener123", "onSuccess: ");
                        // Successfully added targetUserId to the following list
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("addOnSuccessListener123", "Exception: "+e);
                        // Handle failure to add targetUserId to the following list
                    }
                });

        // Add userId to the target user's followers list
        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
        targetUserRef.update("followers." + userId, true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("addOnSuccessListener123", "onSuccess: ");
                        // Successfully added userId to the target user's followers list
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("addOnSuccessListener123", "Exception: "+e);
                        // Handle failure to add userId to the target user's followers list
                    }
                });



//        Map<String, Object> map = new HashMap<>();
//        map.put("targetUserId", targetUserId);
//        // Add targetUserId to the current user's following list
//        CollectionReference followingRef = db.collection("following").document(userId).set("ids",FieldValue.arrayUnion("213"));
//        followingRef.add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        })
;
        // Add userId to the target user's followers list
//        CollectionReference followersRef = db.collection("followers").document(targetUserId).collection("users");
//        followersRef.document(userId).set(true);
//        DocumentReference userRef = db.collection("users").document(userId);
//        DocumentReference targetUserRef = db.collection("users").document(targetUserId);
//
//        // Update the current user's following list
//        userRef.update("following." + targetUserId, true);
//
//        // Update the target user's followers list
//        targetUserRef.update("followers." + userId, true);
    }

    public void unfollowUser(String userId, String targetUserId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Remove targetUserId from the current user's following list
        CollectionReference followingRef = db.collection("following").document(userId).collection("users");
        followingRef.document(targetUserId).delete();

        // Remove userId from the target user's followers list
        CollectionReference followersRef = db.collection("followers").document(targetUserId).collection("users");
        followersRef.document(userId).delete();
    }
}

