package com.stream.donalive.ui.follow.methods;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirestoreHelper {

    private final FirebaseFirestore db;

    public FirestoreHelper() {
        // Initialize your Firestore instance
        db = FirebaseFirestore.getInstance();
    }

    public void fetchFollowingList(String userId, final FetchListCallback<List<String>> callback) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> followingList = (List<String>) documentSnapshot.get("following");
                if (followingList != null) {
                    callback.onSuccess(followingList);
                } else {
                    callback.onError("Following list is empty");
                }
            } else {
                callback.onError("User document does not exist");
            }
        }).addOnFailureListener(e -> {
            callback.onError("Failed to fetch following list: " + e.getMessage());
        });
    }

    public void fetchFollowersList(String userId, final FetchListCallback<List<String>> callback) {
        CollectionReference usersCollectionRef = db.collection("users");
        usersCollectionRef.whereArrayContains("following", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> followersList = new ArrayList<>();
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        followersList.add(documentSnapshot.getId());
                    }
                    if (!followersList.isEmpty()) {
                        callback.onSuccess(followersList);
                    } else {
                        callback.onError("Followers list is empty");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onError("Failed to fetch followers list: " + e.getMessage());
                });
    }

    // Define a callback interface to handle success or error in fetching lists
    public interface FetchListCallback<T> {
        void onSuccess(T result);

        void onError(String error);
    }
}

