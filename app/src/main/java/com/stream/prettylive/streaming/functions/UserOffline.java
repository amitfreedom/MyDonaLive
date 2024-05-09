package com.stream.prettylive.streaming.functions;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.stream.prettylive.ui.utill.Constant;

import java.util.concurrent.atomic.AtomicBoolean;

public class UserOffline{

    public static boolean isUserOffline(String userId) {
        AtomicBoolean isOffline = new AtomicBoolean(false);

        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = FirebaseFirestore.getInstance().collection(Constant.LIVE_DETAILS);

        // Create a query to find the document with the given userId
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        // Flag to indicate if the user is offline

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the liveStatus field from the document
                    String liveStatus = document.getString("liveStatus");

                    // Check if the user is offline
                    if ("offline".equals(liveStatus)) {
                        Log.i("CheckUserStatus", "User with ID " + userId + " is offline.");
                        // Set the flag to true if the user is offline
                        isOffline.set(true);
                        // Perform actions if the user is offline
                    } else {
                        Log.i("CheckUserStatus", "User with ID " + userId + " is online.");
                        // Perform actions if the user is online
                        isOffline.set(false);
                    }
                }
            } else {
                Log.e("CheckUserStatus", "Error getting documents: ", task.getException());
            }
        });

        // Return the offline status
        return isOffline.get();
    }
}
