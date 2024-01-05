package com.stream.prettylive.streaming.functions;

import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddStreamInfo {
    private static final String TAG = "AddStreamInfo";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference streamCollection = db.collection("streams");

    public void addStreamInfo(String mainStreamID, String uid ,String userID, String userName, String image) {
        // Create a Map to hold the stream information
        Map<String, Object> streamInfo = new HashMap<>();
        streamInfo.put("mainStreamID", mainStreamID);
        streamInfo.put("uid", uid);
        streamInfo.put("userID", userID);
        streamInfo.put("userName", userName);
        streamInfo.put("image", image);

        // Add the stream information to Firestore
        DocumentReference streamDocRef = streamCollection.document(mainStreamID);
        streamDocRef.set(streamInfo)
                .addOnSuccessListener(aVoid -> {
                    // Handle success
                    Log.i("Firestore123", "Stream information added successfully for mainStreamID: " + mainStreamID);
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firestore123", "Error adding stream information for mainStreamID: " + mainStreamID, e);
                });
    }
}
