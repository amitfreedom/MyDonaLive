package com.stream.donalive.ui.follow.methods;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FirestoreUtils {

    public static void checkFollowStatus(String currentUserId, String targetUserId, final FollowStatusCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference currentUserRef = db.collection("users").document(currentUserId);

        currentUserRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                List<String> followingList = (List<String>) documentSnapshot.get("following");

                if (followingList != null && followingList.contains(targetUserId)) {
                    // The current user is following the target user
                    callback.onFollowStatus(true);
                } else {
                    // The current user is not following the target user
                    callback.onFollowStatus(false);
                }
            } else {
                // Handle the case when the document doesn't exist
                callback.onFollowStatus(false);
            }
        }).addOnFailureListener(e -> {
            // Handle the failure case
            callback.onFollowStatus(false);
        });
    }

    // Define the FollowStatusCallback interface
    public interface FollowStatusCallback {
        void onFollowStatus(boolean isFollowing);
    }
}
